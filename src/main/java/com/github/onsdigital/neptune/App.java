package com.github.onsdigital.neptune;

import com.github.onsdigital.neptune.gremlin.Config;
import com.github.onsdigital.neptune.gremlin.GremlinTask;
import com.github.onsdigital.neptune.gremlin.GremlinTaskExecutor;
import com.github.onsdigital.neptune.gremlin.PopulateExample;

import java.io.IOException;

import static com.github.onsdigital.neptune.logging.LogBuilder.logBuilder;

/**
 * Simple command line app with beginner examples of inserting data into a graph database via Gremlin.
 */
public class App {

    static final String HELP = "help";
    static final String DROP = "drop";
    static final String IMPORT = "import";

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
            task = new PopulateExample();
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
}
