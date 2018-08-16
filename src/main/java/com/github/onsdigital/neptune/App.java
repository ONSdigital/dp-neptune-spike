package com.github.onsdigital.neptune;

import static com.github.onsdigital.neptune.logging.LogBuilder.logBuilder;


/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        logBuilder().info("running AWS Gremlin example");

        if (args.length != 2) {
            logBuilder().warn("expected host and port but non provided, existing app");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        logBuilder().addParameter("host", host).addParameter("port", port).info("connection parameters");

        new GremlinExample(host, port).run();
    }
}
