package com.github.onsdigital.neptune;

import com.github.onsdigital.neptune.gremlin.Config;
import com.github.onsdigital.neptune.gremlin.GremlinTask;
import com.github.onsdigital.neptune.gremlin.GremlinTaskExecutor;
import com.github.onsdigital.neptune.gremlin.PopulateExample;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

/**
 * Simple command line app with beginner examples of inserting data into a graph database via Gremlin.
 */
public class App {

    static final String HELP = "help";
    static final String DROP = "drop";
    static final String IMPORT = "import";

    public static void main(String[] args) throws Exception {
        Options options = buildOptions();
        CommandLine line = parseCli(options, args);

        GremlinTask task = null;
        Config config = null;

        if (line.hasOption(HELP)) {
            displayHelpMenu(options);
        } else if (line.hasOption(IMPORT)) {
            config = new Config(line.getOptionValues(IMPORT));
            task = new PopulateExample();
        } else if (line.hasOption(DROP)) {
            config = new Config(line.getOptionValues(DROP));
            task = new PopulateExample();
        }

        if (null == task) {
            displayHelpMenu(options);
        }

        executeGremlinTasl(task, config);
    }

    private static void executeGremlinTasl(GremlinTask task, Config config) throws IOException {
        try (GremlinTaskExecutor executor = new GremlinTaskExecutor(config)) {
            executor.execGremlinTask(task);
        }
    }

    private static Options buildOptions() {
        Options options = new Options();
        options.addOption("help", "list available commands and options");

        options.addOption(Option.builder("drop")
                .desc("drop all the nodes and relationships from the graph")
                .hasArgs()
                .numberOfArgs(2)
                .argName("host> <port")
                .build());

        options.addOption(Option.builder("import")
                .desc("populate the graph with some example data")
                .hasArgs()
                .numberOfArgs(2)
                .argName("host> <port")
                .build());
        return options;
    }

    private static CommandLine parseCli(Options options, String[] args) throws ParseException {
        return new DefaultParser().parse(options, args);
    }

    private static void displayHelpMenu(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(200);
        formatter.printHelp("Go-Go-Gremlin", options);
        System.exit(0);
    }
}
