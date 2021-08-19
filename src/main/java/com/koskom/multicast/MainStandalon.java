package com.koskom.multicast;

import org.apache.camel.main.Main;

public class MainStandalon extends Main {
    public static void main(String[] args) throws Exception {
        MainStandalon application = new MainStandalon();
        application.run();
    }

    @Override
    protected void doInit() throws Exception {
        this.configure().addRoutesBuilder(AtlasMapBuilder.class);
        super.doInit();
    }
}
