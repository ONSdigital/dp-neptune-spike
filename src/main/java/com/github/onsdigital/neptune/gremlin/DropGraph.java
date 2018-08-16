package com.github.onsdigital.neptune.gremlin;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

import static com.github.onsdigital.neptune.logging.LogBuilder.logBuilder;

public class DropGraph extends GremlinTask {

    public DropGraph(String neptuneHost, int neptunePort) {
        super(neptuneHost, neptunePort);
    }

    @Override
    public void executeTask(GraphTraversalSource g) {
        logBuilder().info("dropping graph vertices...");
        g.V().drop().iterate();
        logBuilder().info("drop graph vertices complete");
    }

    @Override
    String getName() {
        return "drop";
    }
}
