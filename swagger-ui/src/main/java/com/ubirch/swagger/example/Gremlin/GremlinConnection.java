package com.ubirch.swagger.example.Gremlin;

import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0;
import org.apache.tinkerpop.gremlin.process.traversal.Bindings;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerIoRegistryV3d0;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0;

import java.lang.invoke.MethodHandles;

public class GremlinConnection {

    private static final String LABEL = "label";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String OUT_V = "outV";
    private static final String IN_V = "inV";

    protected JanusGraph janusgraph;
    protected static Cluster cluster;
    protected static Client client;
    protected Configuration conf;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());


    public static void main(String[] args) {

        GryoMessageSerializerV3d0 serializer = new GryoMessageSerializerV3d0(GryoMapper.build().addRegistry(TinkerIoRegistryV3d0.instance()));
        cluster = Cluster.build().addContactPoint("localhost").port(8182).serializer(serializer).create();
        client = cluster.connect();
        Graph graph = EmptyGraph.instance();
        GraphTraversalSource g = graph.traversal().withRemote(DriverRemoteConnection.using(cluster));

        final Bindings b = Bindings.instance();

        Vertex v1 = g.addV(b.of(LABEL, "pays")).property(NAME, b.of(NAME, "Douce France"))
                .property(ID, b.of(ID, 1)).next();
        Vertex v2 = g.addV(b.of(LABEL, "pays")).property(NAME, b.of(NAME, "Ignoble Anglias"))
                .property(ID, b.of(ID, 2)).next();

        g.V(b.of(OUT_V, v1)).as("a").V(b.of(IN_V, v2)).addE(b.of(LABEL, "conquis")).from("a").iterate();

        cluster.close();

    }

}
