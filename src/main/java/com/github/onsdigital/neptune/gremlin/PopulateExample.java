package com.github.onsdigital.neptune.gremlin;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;

public class PopulateExample implements GremlinTask {

    private static final String ID1 = "CustomID1";
    private static final String ID2 = "CustomID2";

    @Override
    public void execute(GraphTraversalSource g) {
        g.addV("Person")
                .property("name", "Justin")
                .next();

        g.addV("Custom Label")
                .property(T.id, ID1)
                .property("name", "Custom id vertex 1")
                .next();

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
    }
}
