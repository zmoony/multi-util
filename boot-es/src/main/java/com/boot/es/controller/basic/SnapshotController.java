package com.boot.es.controller.basic;

import com.boot.es.annotation.LogDescribe;
import com.boot.es.common.R;
import com.boot.es.dao.SnapshotDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.admin.cluster.repositories.verify.VerifyRepositoryResponse;
import org.elasticsearch.action.admin.cluster.snapshots.status.SnapshotStatus;
import org.elasticsearch.cluster.metadata.RepositoryMetadata;
import org.elasticsearch.repositories.RepositoryCleanupResult;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.snapshots.RestoreInfo;
import org.elasticsearch.snapshots.SnapshotInfo;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * API创建备份
 *
 * @author yuez
 * @since 2023/3/24
 */
@RestController
@Api(value = "snapshot操作集合", tags = "备份操作集合")
@LogDescribe
@RequestMapping("/snapshot")
public class SnapshotController {
    private SnapshotDao snapshotDao;

    public SnapshotController(SnapshotDao snapshotDao) {
        this.snapshotDao = snapshotDao;
    }

    /*** 仓库的处理 ***/
    @ApiOperation(value = "创建仓库", nickname = "创建仓库")
    @PostMapping("/repository/create")
    public R createSnapshotRepository(
            @RequestParam(value = "type", defaultValue = "fs") String type,
            @RequestParam(value = "location") String location,
            String repository) throws Exception {
        boolean flag = snapshotDao.createSnapshotRepository(type, location, repository);
        return flag?R.ok().message("创建仓库成功"):R.error();
    }

    @ApiOperation(value = "验证仓库", nickname = "验证仓库")
    @PostMapping("/repository/verify")
    public R verifySnapshotRepository(String repository) throws IOException {
        List<VerifyRepositoryResponse.NodeView> nodeViews = snapshotDao.verifySnapshotRepository(repository);
        return R.ok().data(nodeViews);
    }

    @ApiOperation(value = "查看仓库", nickname = "查看仓库")
    @PostMapping("/repository/list")
    public R listSnapshotRepository(String repository) throws IOException {
        List<RepositoryMetadata> repositoryMetadata = snapshotDao.listSnapshotRepository(repository);
        //RepositoryMetadata 不能 被序列号（属性都是内部，外部不能访问）
        List<String> list = repositoryMetadata.stream().map(r -> r.name()).collect(Collectors.toList());
        return R.ok().data(list);
    }

    @ApiOperation(value = "删除仓库", nickname = "删除仓库")
    @PostMapping("/repository/delete")
    public R deleteSnapshotRepository(String repository) throws IOException {
        boolean b = snapshotDao.deleteSnapshotRepository(repository);
        return b?R.ok().message("删除仓库成功"):R.error();
    }

    @ApiOperation(value = "清理仓库", nickname = "清理仓库")
    @PostMapping("/repository/clean")
    public R cleaneSnapshotRepository(String repository) throws IOException {
        RepositoryCleanupResult repositoryCleanupResult = snapshotDao.cleaneSnapshotRepository(repository);
        return R.ok().data(Collections.singletonList(repositoryCleanupResult));
    }

    /*** 快照的处理 ***/
    @ApiOperation(value = "创建快照", nickname = "创建快照")
    @PostMapping("/create")
    public R createSnapshot(String repository, String snap, String[] indices) throws IOException {
        RestStatus snapshot = snapshotDao.createSnapshot(repository, snap, indices);
        return R.ok().setCode(snapshot.getStatus());
    }

    @ApiOperation(value = "删除快照", nickname = "删除快照")
    @PostMapping("/delete")
    public R deleteSnapshot(String repository, String snap) throws IOException {
        boolean b = snapshotDao.deleteSnapshot(repository, snap);
        return b?R.ok():R.error();
    }

    @ApiOperation(value = "恢复快照", nickname = "恢复快照")
    @PostMapping("/restore")
    public R restoreSnapshot(String repository, String snap, String... indices) throws IOException {
        RestoreInfo restoreInfo = snapshotDao.restoreSnapshot(repository, snap, indices);
        return R.ok().data(Collections.singletonList(restoreInfo));
    }

    @ApiOperation(value = "查看快照", nickname = "查看快照")
    @PostMapping("/info")
    public R infoSnapshot(String repository, String snap, Integer size, String sort, String after) throws IOException {
        List<SnapshotInfo> snapshotInfos = snapshotDao.infoSnapshot(repository, snap, size, sort, after);
        return R.ok().data(snapshotInfos);
    }

    @ApiOperation(value = "查看快照状态", nickname = "查看快照状态")
    @PostMapping("/status")
    public R statusSnapshot(String repository, String snap) throws IOException {
        List<SnapshotStatus> snapshotStatuses = snapshotDao.statusSnapshot(repository, snap);
        return R.ok().data(snapshotStatuses);
    }

    @ApiOperation(value = "克隆快照", nickname = "克隆快照")
    @PostMapping("/clone")
    public R cloneSnapshot(String repository, String srcSnap, String destSnap, String[] indices) throws IOException {
        boolean b = snapshotDao.cloneSnapshot(repository, srcSnap, destSnap, indices);
        return b?R.ok():R.error();
    }


}
