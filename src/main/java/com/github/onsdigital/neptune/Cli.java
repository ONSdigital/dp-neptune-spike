package com.github.onsdigital.neptune;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Cli {

    private Options options;
    private CommandLine line;

    public Cli(String[] args) throws ParseException {
        this.options = new Options();
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

        options.addOption(Option.builder("example")
                .desc("run the aws example")
                .hasArgs()
                .numberOfArgs(2)
                .argName("host> <port")
                .build());

        this.line = new DefaultParser().parse(options, args);
    }

    public void displayHelpAndExit() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(200);
        formatter.printHelp("Go-Go-Gremlin", options);
        System.exit(0);
    }

    public Options getOptions() {
        return this.options;
    }

    public CommandLine getLine() {
        return this.line;
    }
}
