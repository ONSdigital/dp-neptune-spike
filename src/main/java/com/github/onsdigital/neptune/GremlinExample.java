package com.github.onsdigital.neptune;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;

import static com.github.onsdigital.neptune.logging.LogBuilder.logBuilder;

public class GremlinExample {

    private static final String ID1 = "CustomID1";
    private static final String ID2 = "CustomID2";

    private String neptuneHost;
    private int neptunePort;

    public GremlinExample(String neptuneHost, int neptunePort) {
        this.neptuneHost = neptuneHost;
        this.neptunePort = neptunePort;
    }

    public void run() {
        logBuilder().info("running gremlin example");

        Cluster cluster = Cluster.build()
                .port(neptunePort)
                .addContactPoint(neptuneHost)
                .create();

        try (GraphTraversalSource g = EmptyGraph.instance()
                .traversal()
                .withRemote(DriverRemoteConnection
                        .using(cluster))) {

            logBuilder().info("adding person");
            g.addV("Person")
                    .property("name", "Justin")
                    .next();

            logBuilder().info("adding Custom Label 1");
            g.addV("Custom Label")
                    .property(T.id, ID1)
                    .property("name", "Custom id vertex 1")
                    .next();

            logBuilder().info("adding Custom Label 2");
            g.addV("Custom Label")
                    .property(T.id, ID2)
                    .property("name", "Custom id vertex 2")
                    .next();

            g.addE("Edge Label")
                    .from(g.V(ID1))
                    .to(g.V(ID2))
                    .next();

            g.V().valueMap()
                    .forEachRemaining(e -> System.out.println(e));

            logBuilder().info("example completed successfully");


        } catch (Exception e) {
            logBuilder().error(e, "something went wrong");
        } finally {
            logBuilder().info("closing cluster");
            cluster.close();
        }
    }
}
