package com.boot.es.dao.impl;

import com.boot.es.dao.SnapshotDao;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.cluster.repositories.cleanup.CleanupRepositoryRequest;
import org.elasticsearch.action.admin.cluster.repositories.cleanup.CleanupRepositoryResponse;
import org.elasticsearch.action.admin.cluster.repositories.delete.DeleteRepositoryRequest;
import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesRequest;
import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesResponse;
import org.elasticsearch.action.admin.cluster.repositories.put.PutRepositoryRequest;
import org.elasticsearch.action.admin.cluster.repositories.verify.VerifyRepositoryRequest;
import org.elasticsearch.action.admin.cluster.repositories.verify.VerifyRepositoryResponse;
import org.elasticsearch.action.admin.cluster.snapshots.clone.CloneSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.delete.DeleteSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.get.GetSnapshotsRequest;
import org.elasticsearch.action.admin.cluster.snapshots.get.GetSnapshotsResponse;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.status.SnapshotStatus;
import org.elasticsearch.action.admin.cluster.snapshots.status.SnapshotsStatusRequest;
import org.elasticsearch.action.admin.cluster.snapshots.status.SnapshotsStatusResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.SnapshotClient;
import org.elasticsearch.cluster.metadata.RepositoryMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.repositories.RepositoryCleanupResult;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.snapshots.RestoreInfo;
import org.elasticsearch.snapshots.SnapshotInfo;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

/**
 * Snapshot 快照备份
 *
 * @author yuez
 * @since 2023/3/24
 */
@Repository
public class SnapshotDaoImpl implements SnapshotDao {
    private SnapshotClient snapshot;

    public SnapshotDaoImpl(RestHighLevelClient restHighLevelClient) {
        this.snapshot = restHighLevelClient.snapshot();
    }

    @Override
    public boolean createSnapshotRepository(String type, String location,String repository) throws Exception {
        PutRepositoryRequest request = new PutRepositoryRequest(repository)
                .type(type)
                .settings(Settings.builder().put("location",location).build());
        AcknowledgedResponse response = snapshot.createRepository(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    @Override
    public List<VerifyRepositoryResponse.NodeView> verifySnapshotRepository(String repository) throws IOException {
        VerifyRepositoryRequest request = new VerifyRepositoryRequest(repository)
                .timeout(TimeValue.timeValueMinutes(1));
        VerifyRepositoryResponse response = snapshot.verifyRepository(request, RequestOptions.DEFAULT);
        List<VerifyRepositoryResponse.NodeView> nodes = response.getNodes();
        return nodes;
    }

    @Override
    public List<RepositoryMetadata> listSnapshotRepository(String repository) throws IOException {
        GetRepositoriesRequest request;
        if(StringUtils.isNotEmpty(repository)){
            String[] repositories = new String[]{repository};
            request = new GetRepositoriesRequest(repositories);
        }else {
            request = new GetRepositoriesRequest();
        }
        GetRepositoriesResponse response = snapshot.getRepository(request, RequestOptions.DEFAULT);
        List<RepositoryMetadata> repositories1 = response.repositories();
        return repositories1;
    }

    @Override
    public boolean deleteSnapshotRepository(String repository) throws IOException {
        DeleteRepositoryRequest request = new DeleteRepositoryRequest(repository).timeout(TimeValue.timeValueMinutes(1));
        AcknowledgedResponse response = snapshot.deleteRepository(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    @Override
    public RepositoryCleanupResult cleaneSnapshotRepository(String repository) throws IOException {
        CleanupRepositoryRequest request = new CleanupRepositoryRequest(repository).timeout(TimeValue.timeValueMinutes(1));
        CleanupRepositoryResponse response = snapshot.cleanupRepository(request, RequestOptions.DEFAULT);
        return response.result();
    }

    @Override
    public boolean cloneSnapshot(String repository, String srcSnap, String destSnap, String... indices) throws IOException {
        CloneSnapshotRequest request = new CloneSnapshotRequest(repository,srcSnap,destSnap,indices);
        AcknowledgedResponse response = snapshot.clone(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    @Override
    public RestStatus createSnapshot(String repository, String snap, String[] indices) throws IOException {
        CreateSnapshotRequest request = new CreateSnapshotRequest(repository,snap);
        if(ArrayUtils.isNotEmpty(indices)){
            request.indices(indices);
        }
        request.includeGlobalState(false);
        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        request.waitForCompletion(true);//一直等待到成功
        CreateSnapshotResponse response = snapshot.create(request, RequestOptions.DEFAULT);
        return response.status();
    }

    @Override
    public List<SnapshotInfo> infoSnapshot(String repository, String snap, Integer size, String sort, String after) throws IOException {
        GetSnapshotsRequest request = new GetSnapshotsRequest();
        request.repository(repository);
        if (StringUtils.isNotEmpty(snap)) {
            request.snapshots(new String[]{snap});
        }
        request.verbose(true);
        request.ignoreUnavailable(false);
        GetSnapshotsResponse response = snapshot.get(request, RequestOptions.DEFAULT);
        return response.getSnapshots();
    }

    @Override
    public List<SnapshotStatus> statusSnapshot(String repository, String snap) throws IOException {
        SnapshotsStatusRequest request = new SnapshotsStatusRequest();
        request.repository(repository);
        request.snapshots(new String[]{snap});
        request.ignoreUnavailable(true);
        SnapshotsStatusResponse response = snapshot.status(request, RequestOptions.DEFAULT);
        return response.getSnapshots();
    }

    @Override
    public boolean deleteSnapshot(String repository, String snap) throws IOException {
        DeleteSnapshotRequest request = new DeleteSnapshotRequest(repository);
        request.snapshots(snap);
        AcknowledgedResponse response = snapshot.delete(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    @Override
    public RestoreInfo restoreSnapshot(String repository, String snap, String[] indices) throws IOException {
        RestoreSnapshotRequest request = new RestoreSnapshotRequest(repository, snap);
        request.indices(indices);
        request.renamePattern("index_(.+)");
        request.renameReplacement("restored_index_$1");
        request.includeGlobalState(false);
        request.includeAliases(false);
        request.indicesOptions(new IndicesOptions(EnumSet.of(IndicesOptions.Option.IGNORE_UNAVAILABLE),EnumSet.of(IndicesOptions.WildcardStates.OPEN)));
        request.waitForCompletion(true);
        RestoreSnapshotResponse response = snapshot.restore(request, RequestOptions.DEFAULT);
        return response.getRestoreInfo();
    }
}
