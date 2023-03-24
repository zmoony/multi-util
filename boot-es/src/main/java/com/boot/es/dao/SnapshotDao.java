package com.boot.es.dao;

import org.elasticsearch.action.admin.cluster.repositories.verify.VerifyRepositoryResponse;
import org.elasticsearch.action.admin.cluster.snapshots.status.SnapshotStatus;
import org.elasticsearch.cluster.metadata.RepositoryMetadata;
import org.elasticsearch.repositories.RepositoryCleanupResult;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.snapshots.RestoreInfo;
import org.elasticsearch.snapshots.SnapshotInfo;

import java.io.IOException;
import java.util.List;

public interface SnapshotDao {
    boolean createSnapshotRepository(String type, String location,String repository) throws Exception;

    List<VerifyRepositoryResponse.NodeView> verifySnapshotRepository(String repository) throws IOException;

    List<RepositoryMetadata> listSnapshotRepository(String repository) throws IOException;

    boolean deleteSnapshotRepository(String repository) throws IOException;

    RepositoryCleanupResult cleaneSnapshotRepository(String repository) throws IOException;

    boolean cloneSnapshot(String repository, String srcSnap, String destSnap, String... indices) throws IOException;

    RestStatus createSnapshot(String repository, String snap, String[] indices) throws IOException;

    List<SnapshotInfo> infoSnapshot(String repository, String snap, Integer size, String sort, String after) throws IOException;


    List<SnapshotStatus> statusSnapshot(String repository, String snap) throws IOException;

    boolean deleteSnapshot(String repository, String snap) throws IOException;

    RestoreInfo restoreSnapshot(String repository, String snap, String[] indices) throws IOException;
}
