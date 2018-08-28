package com.github.onsdigital.neptune.gremlin;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import static com.github.onsdigital.neptune.logging.LogBuilder.logBuilder;

public class CodeList implements GremlinTask{

    @Override
    public void execute(GraphTraversalSource g) {
        Graph graph = g.getGraph();
        Vertex v1 = graph.addVertex(T.label, "_code_list", "name", "gibson-guitars", "edition", "2018", "label", "fender");
        logBuilder().info("created vertex ID: " + v1.id());
    }
}
