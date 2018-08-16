package com.github.onsdigital.neptune.gremlin;

public class Config {
    private String host;
    private int port;

    public Config(String[] options) {
        if (options == null || options.length != 2) {
            throw new IllegalArgumentException("expected host and port");
        }
        this.host = options[0];
        this.port = Integer.parseInt(options[1]);
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }
}
