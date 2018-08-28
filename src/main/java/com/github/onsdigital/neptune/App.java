package com.github.onsdigital.neptune;

import com.github.onsdigital.neptune.gremlin.Config;
import com.github.onsdigital.neptune.gremlin.DropGraph;
import com.github.onsdigital.neptune.gremlin.GremlinTask;
import com.github.onsdigital.neptune.gremlin.GremlinTaskExecutor;
import com.github.onsdigital.neptune.gremlin.PopulateExample;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;

import java.io.IOException;

import static com.github.onsdigital.neptune.logging.LogBuilder.logBuilder;

/**
 * Simple command line app with beginner examples of inserting data into a graph database via Gremlin.
 */
public class App {

    static final String HELP = "help";
    static final String DROP = "drop";
    static final String IMPORT = "import";
    static final String EXAMPLE = "example";

    public static void main(String[] args) throws Exception {
        Cli cli = new Cli(args);

        GremlinTask task = null;
        Config config = null;

        if (cli.getLine().hasOption(HELP)) {
            cli.displayHelpAndExit();
        } else if (cli.getLine().hasOption(IMPORT)) {
            config = new Config(cli.getLine().getOptionValues(IMPORT));
            task = new PopulateExample();
        } else if (cli.getLine().hasOption(DROP)) {
            config = new Config(cli.getLine().getOptionValues(DROP));
            task = new DropGraph();
        }

        if (null == task) {
            cli.displayHelpAndExit();
        }

        executeGremlinTask(task, config);
    }

    private static void executeGremlinTask(GremlinTask task, Config config) throws IOException {
        try (GremlinTaskExecutor executor = new GremlinTaskExecutor(config)) {
            executor.execGremlinTask(task);
        } catch (Exception e) {
            logBuilder().error(e, "execute Gremlin task failure.");
        }
    }

    public static void example(Config cfg) {
        Cluster.Builder builder = Cluster.build();
        builder.addContactPoint(cfg.getHost());
        builder.port(cfg.getPort());

        Cluster cluster = builder.create();

        GraphTraversalSource g = EmptyGraph.instance().traversal().withRemote(DriverRemoteConnection.using(cluster));

        g.addV("Person").property("Name", "Justin").next();

        // Add a vertex with a user-supplied ID.
        g.addV("Custom Label").property(T.id, "CustomId1").property("name", "Custom id vertex 1").next();
        g.addV("Custom Label").property(T.id, "CustomId2").property("name", "Custom id vertex 2").next();

        g.addE("Edge Label").from(g.V("CustomId1")).to(g.V("CustomId2")).next();

        // This gets the vertices, only.
        GraphTraversal t = g.V().limit(3).valueMap();

        t.forEachRemaining(
                e -> System.out.println(e)
        );

        cluster.close();
    }
}
