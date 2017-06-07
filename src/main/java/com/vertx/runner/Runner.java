package com.vertx.runner;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class Runner {

    private static final String CORE_DIR = "vertx-pub-sub";
    private static final String JAVA_DIR = CORE_DIR + "/src/main/java/";

    public static void runClustered(Class clazz) {
        runApplication(JAVA_DIR, clazz, new VertxOptions().setClustered(true), null);
    }

    public static void runClustered(Class clazz, VertxOptions options) {
        runApplication(JAVA_DIR, clazz, options.setClustered(true), null);
    }

    public static void runApplication(Class clazz) {
        runApplication(JAVA_DIR, clazz, new VertxOptions().setClustered(false), null);
    }

    public static void runApplication(Class clazz, DeploymentOptions options) {
        runApplication(JAVA_DIR, clazz, new VertxOptions().setClustered(false), options);
    }

    public static void runApplication(String exampleDir, Class clazz, VertxOptions options, DeploymentOptions deploymentOptions) {
        runExample(exampleDir + clazz.getPackage().getName().replace(".", "/"), clazz.getName(), options, deploymentOptions);
    }

    public static void runScriptExample(String prefix, String scriptName, VertxOptions options) {
        File file = new File(scriptName);
        String dirPart = file.getParent();
        String scriptDir = prefix + dirPart;
        runExample(scriptDir, scriptDir + "/" + file.getName(), options, null);
    }

    public static void runExample(String exampleDir, String verticleID, VertxOptions options, DeploymentOptions deploymentOptions) {
        if (options == null) {
            // Default parameter
            options = new VertxOptions();
        }

        try {

            File current = new File(".").getCanonicalFile();
            if (exampleDir.startsWith(current.getName()) && !exampleDir.equals(current.getName())) {
                exampleDir = exampleDir.substring(current.getName().length() + 1);
            }
        } catch (IOException e) {
            // Ignore it.
        }

        System.setProperty("vertx.cwd", exampleDir);
        Consumer<Vertx> runner = vertx -> {
            try {
                if (deploymentOptions != null) {
                    vertx.deployVerticle(verticleID, deploymentOptions);
                } else {
                    vertx.deployVerticle(verticleID);
                }
            } catch (Throwable t) {
            }
        };
        if (options.isClustered()) {
            Vertx.clusteredVertx(options, res -> {
                if (res.succeeded()) {
                    Vertx vertx = res.result();
                    runner.accept(vertx);
                } else {
                }
            });
        } else {
            Vertx vertx = Vertx.vertx(options);
            runner.accept(vertx);
        }
    }

    private Runner() {
    }
}
