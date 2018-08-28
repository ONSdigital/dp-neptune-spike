package com.github.onsdigital.neptune.gremlin;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

import static com.github.onsdigital.neptune.logging.LogBuilder.logBuilder;

public class CodeList implements GremlinTask{

    @Override
    public void execute(GraphTraversalSource g) {
        Object ID = g.addV("_code_list")
                .property("name", "gibson-guitars")
                .property("edition", "2018")
                .property("label", "fender")
                .next()
                .id();

        logBuilder().info("CODE LIST ID " + ID.toString());
    }
}
