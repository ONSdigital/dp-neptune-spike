package com.github.onsdigital.neptune;

import com.github.onsdigital.neptune.gremlin.AbstractCommand;
import com.github.onsdigital.neptune.gremlin.DropGraph;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static com.github.onsdigital.neptune.logging.LogBuilder.logBuilder;


/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        logBuilder().info("running AWS Gremlin example");

        Options options = new Options();
        options.addOption("help", "list available commands and options");

        options.addOption(Option.builder("drop")
                .desc("drop all the nodes and relationships from the graph")
                .hasArgs()
                .numberOfArgs(2)
                .argName("host> <port")
                .build());


        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        AbstractCommand cmd = null;

        if (line.hasOption("help")) {
            printCliHelp(options);
            System.exit(0);
        }

        if (line.hasOption("drop")) {
            String[] opts = line.getOptionValues("clean");
            cmd = new DropGraph(opts[0], Integer.parseInt(opts[1]));
        }

        cmd.connectAndExecute();
    }

    private static void printCliHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(200);
        formatter.printHelp("Go-Go-Gremlin", options);
    }
}
