package com.github.onsdigital.neptune.gremlin;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

@FunctionalInterface
public interface GremlinTask {

    void execute(GraphTraversalSource g);
}
