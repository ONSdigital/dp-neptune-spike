package com.github.onsdigital.neptune;

import org.apache.commons.lang3.StringUtils;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.github.onsdigital.neptune.logging.LogBuilder.logBuilder;

/**
 * Simple command line app with beginner examples of inserting data into a graph database via Gremlin.
 */
public class App {

    static final String HELP = "help";
    static final String QUERY = "query";
    static final String PROMPT = "grem-query> ";

    static Cluster cluster;
    static Client gremCli;

    public static void main(String[] args) throws Exception {
        Cli cli = new Cli(args);

        if (cli.getLine().hasOption(HELP)) {
            cli.displayHelpAndExit();
        }

        if (cli.getLine().hasOption(QUERY)) {
            cluster = Cluster.build()
                    .addContactPoint("localhost")
                    .port(8182)
                    .create();
            gremCli = cluster.connect();

            scanForInput();
            System.exit(0);
        }

        cli.displayHelpAndExit();
    }

    private static void scanForInput() throws ExecutionException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.print(PROMPT);
            String line = sc.nextLine();

            if (line.equalsIgnoreCase("exit")) {
                closeAndExit();
                exit = true;
            } else if (StringUtils.isNoneEmpty(line)) {
                try {
                    execQuery(line);
                } catch (Exception e) {
                    logBuilder().error(e, "error happened");
                    exit = true;
                    closeAndExit();
                }
            }
        }
    }

    private static void execQuery(String query) throws ExecutionException, InterruptedException {
        CompletableFuture<List<Result>> r = gremCli.submit(query).all();
        r.get().stream().forEach(item -> System.out.println("> " + item.getString()));
    }

    private static void closeAndExit() {
        System.out.print(PROMPT + "exiting");
        cluster.close();
        gremCli.close();
    }
}

