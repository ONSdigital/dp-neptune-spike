package com.github.onsdigital.neptune.gremlin;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;

import java.io.Closeable;
import java.io.IOException;

import static com.github.onsdigital.neptune.logging.LogBuilder.logBuilder;

public class GremlinTaskExecutor implements Closeable {

    protected Cluster cluster;
    protected String neptuneHost;
    protected int neptunePort;

    public GremlinTaskExecutor(Config config) {
        this.cluster = Cluster.build()
                .addContactPoint(neptuneHost)
                .port(neptunePort)
                .create();

        this.neptuneHost = config.getHost();
        this.neptunePort = config.getPort();
    }

    public void execGremlinTask(GremlinTask task) {
        try (GraphTraversalSource g = EmptyGraph.instance()
                .traversal()
                .withRemote(DriverRemoteConnection
                        .using(cluster))) {
            task.execute(g);
        } catch (Exception e) {
            logBuilder().error(e, "something went wrong");
        }
    }

    @Override
    public void close() throws IOException {
        cluster.close();
    }
}
