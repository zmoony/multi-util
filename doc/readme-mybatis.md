## mybatis-plus
> mapper里不能写注释 <br>
> 转义   where <![CDATA[ jj.bjdhsj > #{startTime} and jj.bjdhsj <= #{endTime} ]]>
## pom
```xml
 <!--mybatis-plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.1</version>
        </dependency>
```
## 配置文件
```yaml
#mybatis-plus配置
mybatis-plus:
  typeAliasesPackage: com.wiscom.polo
  mapper-locations: classpath:mapper/**/*Mapper.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl #开启sql日志
```

## 启动类
```java
@MapperScan("com.wiscom.repository") //扫描mapper接口
```
## mapper接口
```java
@Repository
public interface PoliceDealMapper {
    /**
     * 获取处警最大的修改时间
     * @return
     */
    String getMaxEditTime();
}
```
## mapper.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.wiscom.repository.PoliceReceiveMapper">
    <select id="getMaxEditTime" resultType="java.lang.String">
        select toYYYYMMDDhhmmss(MAX(edit_time))
        from police_receive pr
    </select>
     <update id="updateInfo" parameterType="com.wiscom.pojo.cds.CdsPoliceReceiveInfo">
        alter table police_receive
        update status=#{item.status},
        content=#{item.content},
        feedback_time = toDateTime(#{item.feedbackTime}),
        arrive_time = toDateTime(#{item.arriveTime})
        where object_id=#{item.objectId}
    </update>
</mapper>
```
## 注解
```java
@Select("selectl.*,m.idWiscomTollgateIDfrompsmp_report_lanelleftjoinpsmp_report_tollgate_mappermonl.tollgateid=m.stk_id${ew.customSqlSegment}")
List<WiscomStkLane>selectRelation(IPage<WiscomStkLane>page,@Param(Constants.WRAPPER)QueryWrapper<WiscomStkLane>queryWrapper);
```
### 实体类
```java
@Data
@AllArgsConstructor
@TableName(value = "PSP_APP_USER")
public class PspAppUser {
    @TableId(value = "USER_ID", type = IdType.INPUT)
    private BigDecimal userId;

    @TableField(value = "USER_GROUP_ID")
    private BigDecimal userGroupId;
}

```
### 分页
```java
 IPage<WiscomReportSyncLane> userPage = new Page<>(lane.getPageNumber(), lane.getPageSize());//参数一是当前页，参数二是每页个数
 List<WiscomStkLane> records = this.getBaseMapper().selectRelationBySub(userPage, customsql).getRecords();
---
@Select("select l.*,m.id WiscomTollgateID,s.subscribeid,nvl2(s.subscribeid,1,0) status " +
            "from psmp_report_lane l  " +
            "left join psmp_report_tollgate_mapper m on l.tollgateid = m.stk_id " +
            "left join psmp_report_sync_lane s on l.tollgateid = s.tollgateid and l.direction =s.direction and l.laneno = s.laneno " +
            "${customsql}")
    IPage<WiscomStkLane> selectRelationBySub(IPage<WiscomReportSyncLane> page,  @Param("customsql") String customsql) ;

```