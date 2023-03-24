
# 数据备份
* 将数据导出成文本文件，比如通过 elasticdump、esm 等工具将存储在 Elasticsearch 中的数据导出到文件中。
* 备份 elasticsearch data 目录中文件的形式来做快照，借助 Elasticsearch 中 snapshot 接口实现的功能。
## 流程
[mermaid 语法](https://zhuanlan.zhihu.com/p/139166407)
~~~mermaid
graph LR
    创建仓库 --> 创建快照 -->恢复备份
subgraph ahead[提前手动创建]
    创建备份目录 --> 创建仓库
end
~~~
## wiscom示例
```
PUT /_snapshot/zyj_backup
{
  "type": "fs",
  "settings": {
    "location": "/backup"
  }
}

GET _snapshot/_all
{
  "zyj_backup" : {//仓库名
    "type" : "fs",
    "settings" : {
      "location" : "/backup"
    }
  }
}

指定索引名备份
PUT _snapshot/kcy_backup/index_vehicle_n2023.03.24?wait_for_completion=true
{
  "indices": "index_vehicle_n",
  "ignore_unavailable": true, -- the snapshot ignores missing data streams and indices
  "include_global_state": false
}

全备份
PUT _snapshot/kcy_backup/backup2023.03.24?wait_for_completion=true
{
  "ignore_unavailable": true,
  "include_global_state": false
}
```

## 语句备份

### 1. 设置备份目录

>1. 修改elasticsearch.yml
配置文件elasticsearch.yml中添加  path.repo: ["/home/gocode/app/backup/elk"]，重启ES。
```shell
mkdir -p /home/gocode/app/backup/elk
chmod 755 /home/gocode/app/backup/elk
chown es:es /home/gocode/app/backup/elk  //给es用户目录权限
```

### 2. 创建仓库

>1. 备份数据之前，要创建一个仓库来保存数据，仓库的类型支持Shared filesystem, Amazon S3, HDFS、Azure Cloud选择。
创建了一个备份仓库名为datasvr 存储目录为/home/gocode/app/backup/elk 的备份仓库。
```
curl -H "Content-Type: application/json" -XPUT ' http://127.0.0.1:9200/_snapshot/datasvr' -d '
{
    "type": "fs",
    "settings": {
        "location": "/home/gocode/app/backup/elk",
        "compress": true
    }
}'
```
#### 2.1 验证仓库
```http request
POST _snapshot/zyj_backup/_verify
{
  "nodes" : {
    "waJF2Nx_RVadc5TY0ZLoaQ" : {
      "name" : "wiscom-12"
    }
  }
}
-- 不存在
POST _snapshot/zyj_backup2/_verify
{
  "error" : {
    "root_cause" : [
      {
        "type" : "repository_missing_exception",
        "reason" : "[zyj_backup2] missing"
      }
    ],
    "type" : "repository_missing_exception",
    "reason" : "[zyj_backup2] missing"
  },
  "status" : 404
}

```

### 3. 创建快照

> 一个仓库可以拥有同一个集群的多个快照。在一个集群中快照拥有一个唯一名字作为标识。在仓库 datasvr 中创建名字为 snapshot_1 的快照。
<em style='color:red'>同步执行,加wait_for_completion 标志,备份完成后才返回，如果数据量大的话，会花很长时间
</em>
```http request
curl -H "Content-Type:application/json" -XPUT '127.0.0.1:9200/_snapshot/datasvr/snapshot_1' -d '
{
    "indices": "index_1,index_2"
}'
备份名称为snapshot_1 (自行定义备份名称)
```
#### 3.1 克隆快照
>将部分或全部快照克隆到新快照中。
```http request
PUT /_snapshot/my_repository/source_snapshot/_clone/target_snapshot
{
  "indices": "index_a,index_b"
}

```

### 4. 查看仓库信息

> http://172.20.32.241:9200/_snapshot/datasvr/

### 5. 查看快照信息

>浏览器中查看备份仓库某个快照信息<br>
http://172.20.32.241:9200/_snapshot/datasvr/snapshot_1 <br>
浏览器中查看备份仓库所有快照信息<br>
http://172.20.32.241:9200/_snapshot/datasvr/_all
```http request
GET /_snapshot/my_repository/my_snapshot
GET /_snapshot/my_repository/snapshot*?size=2&sort=name
GET /_snapshot/my_repository/snapshot*?size=2&sort=name&after=c25hcHNob3RfMixteV9yZXBvc2l0b3J5LHNuYXBzaG90XzI=
=====
{
  "snapshots": [
    {
      "snapshot": "snapshot_2",
      "uuid": "vdRctLCxSketdKb54xw67g",
      "repository": "my_repository",
      "version_id": <version_id>,
      "version": <version>,
      "indices": [],
      "data_streams": [],
      "feature_states": [],
      "include_global_state": true,
      "state": "SUCCESS",
      "start_time": "2020-07-06T21:55:18.129Z",
      "start_time_in_millis": 1593093628850,
      "end_time": "2020-07-06T21:55:18.129Z",
      "end_time_in_millis": 1593094752018,
      "duration_in_millis": 0,
      "failures": [],
      "shards": {
        "total": 0,
        "failed": 0,
        "successful": 0
      }
    }
  ],
  "total": 1,
  "remaining": 0
}
```

### 6. 删除快照

>curl -X DELETE "localhost:9200/_snapshot/datasvr/snapshot_1"
```http request
curl -X DELETE "localhost:9200/_snapshot/my_repository/snapshot_2,snapshot_3?pretty"
{
  "acknowledged" : true
}
```

### 7. 删除仓库

>仓库被注销时，ElasticSearch 只删除仓库存储快照的引用位置，快照本身没有被删除并且在原来的位置<br>
curl -X DELETE "localhost:9200/_snapshot/datasvr"

#### 7.1 清理仓库
> POST /_snapshot/my_repository/_cleanup <br>
> {
"results": {
"deleted_bytes": 20,
"deleted_blobs": 5
}
}

### 8. 集群备份

>注意： 以上是单机的备份方法，集群的备份恢复方法和单机模式一样，只不过需要增加一个集群共享目录用来存放备份数据，使所有节点可访问。
```shell
# 1) 配置集群共享目录
 yum install sshfs
# 2) 创建集群共享目录，并将各节点备份目录挂载到共享目录
# 创建集群共享目录
 mkdir -p /home/backup/elk_share
# 共享目录挂载
 sshfs root@172.20.32.239:/home/backup/elk_share /home/backup/elk  -o allow_other
#3) 数据备份恢复与单机备份恢复一样，参考单机备份恢复
```

### 9. 数据恢复

> curl -H "Content-Type:application/json" -XPOST '127.0.0.1:9200/_snapshot/datasvr/snapshot_1/_restore'
```http request
curl -X POST "localhost:9200/_snapshot/my_repository/snapshot_2/_restore?wait_for_completion=true&pretty" -H 'Content-Type: application/json' -d'
{
  "indices": "index_1,index_2",
  "ignore_unavailable": true,
  "include_global_state": false,
  "rename_pattern": "index_(.+)",
  "rename_replacement": "restored_index_$1",
  "include_aliases": false
}
'
```
## JAVA-API
