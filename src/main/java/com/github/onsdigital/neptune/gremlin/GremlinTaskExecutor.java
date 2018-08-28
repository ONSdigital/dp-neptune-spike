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
    protected GraphTraversalSource g;

    public GremlinTaskExecutor(Config config) {
        this.neptuneHost = config.getHost();
        this.neptunePort = config.getPort();

        this.cluster = Cluster.build()
                .addContactPoint(neptuneHost)
                .port(neptunePort)
                .create();

        this.g = EmptyGraph.instance()
                .traversal()
                .withRemote(DriverRemoteConnection
                        .using(cluster));
    }

    public void execGremlinTask(GremlinTask task) {
        logBuilder().addParameter("host", neptuneHost)
                .addParameter("port", neptunePort)
                .info("connection configuration");

        try {
            task.execute(g);
        } catch (Exception e) {
            logBuilder().error(e, "execGremlinTask something went wrong");
        }
    }

    @Override
    public void close() throws IOException {
        logBuilder().info("attempting to close graph traversal");
        try {
            this.g.close();
        } catch (Exception e) {
            logBuilder().error(e, "error closing graph traversal");
        }

        logBuilder().info("closing Neptine cluster");
        cluster.close();
    }
}
