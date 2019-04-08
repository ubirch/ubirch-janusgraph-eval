package com.ubirch.swagger.example.Gremlin;

import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.ubirch.swagger.example.Structure.VertexStruct;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0;
import org.apache.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngine;
import org.apache.tinkerpop.gremlin.process.traversal.Bindings;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerIoRegistryV3d0;
import org.apache.tinkerpop.gremlin.util.Gremlin;
import org.apache.tinkerpop.gremlin.util.function.Lambda;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.graphdb.management.JanusGraphManager;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*; //important
import java.awt.*;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import scala.collection.JavaConverters;
import scala.collection.Map$;
import scala.Predef;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.concurrent.JavaConversions;

import javax.script.ScriptEngine;
import javax.script.ScriptException;


public class GremlinConnection {

    private static final String LABEL = "label";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String OUT_V = "outV";
    private static final String IN_V = "inV";
    private static final String TEST = "test";
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    protected static Cluster cluster;
    protected static Client client;
    protected JanusGraph janusgraph;
    protected Configuration conf;

    public static com.ubirch.swagger.example.Structure.VertexStruct[] getAllVertexes(){

        GryoMessageSerializerV3d0 serializer = new GryoMessageSerializerV3d0(GryoMapper.build().addRegistry(JanusGraphIoRegistry.getInstance()));
        //GryoMessageSerializerV3d0 serializer = new GryoMessageSerializerV3d0(GryoMapper.build().addRegistry(TinkerIoRegistryV3d0.instance()));
        cluster = Cluster.build().addContactPoint("localhost").port(8182).serializer(serializer).create();
        client = cluster.connect();
        Graph graph = EmptyGraph.instance();
        GraphTraversalSource g = graph.traversal().withRemote(DriverRemoteConnection.using(cluster));


        GremlinPipeline pipe = new GremlinPipeline();
        pipe.start(g.V().has(T.label, "transaction").valueMap());
        List l = pipe.toList();


        ArrayList<Map<String, String>> list = new ArrayList();


        for (Object o : l){
            Map<Object, Object> hmTmp = (HashMap) o;
            list.add(hmTmp.entrySet().stream().collect(Collectors.toMap(
                    entry -> (String) entry.getKey(),
                    entry ->  entry.getValue().toString()
            )));
        }

        // Create list of vertexes
        int i = 0;
        VertexStruct[] listVertexes = new com.ubirch.swagger.example.Structure.VertexStruct[l.size()];
        for(Object s: list){
            // Intellij shows an error but it does compile normally
            listVertexes[i] = new com.ubirch.swagger.example.Structure.VertexStruct("transaction", JavaConverters.mapAsScalaMapConverter(list.get(i)).asScala().toMap(
                    Predef.conforms()
            ));
            i++;
        }

        cluster.close();
        return listVertexes;
    }
    
    public static VertexStruct getVertexById(int id){
        GryoMessageSerializerV3d0 serializer = new GryoMessageSerializerV3d0(GryoMapper.build().addRegistry(TinkerIoRegistryV3d0.instance()));
        cluster = Cluster.build().addContactPoint("localhost").port(8182).serializer(serializer).create();
        client = cluster.connect();
        Graph graph = EmptyGraph.instance();
        GraphTraversalSource g = graph.traversal().withRemote(DriverRemoteConnection.using(cluster));

        GremlinPipeline pipe = new GremlinPipeline();
        pipe.start(g.V().has(T.label, "transaction").has("id", id).valueMap());
        List l = pipe.toList();

        if(l.isEmpty()){
            cluster.close();
            return null;
        }

        logger.info("l.toString() = " + l.toString());

        Map<Object, Object> propertiesHM = (HashMap) l.get(0);
        Map<String, String> stuff = propertiesHM.entrySet().stream().collect(Collectors.toMap(
                entry -> (String) entry.getKey(),
                entry ->  entry.getValue().toString()));

        VertexStruct v = new com.ubirch.swagger.example.Structure.VertexStruct("transaction",  JavaConverters.mapAsScalaMapConverter(stuff).asScala().toMap(
                Predef.conforms()
        ));

        cluster.close();
        return v;
    }

/**
Add two vertexes to the database and link them together
*/
    public static String addTwoVertexes(String nameOfVertex1, int id1, String nameOfVertex2, int id2) throws ScriptException {
        GryoMessageSerializerV3d0 serializer = new GryoMessageSerializerV3d0(GryoMapper.build().addRegistry(TinkerIoRegistryV3d0.instance()));
        cluster = Cluster.build().addContactPoint("localhost").port(8182).serializer(serializer).create();
        client = cluster.connect();
        Graph graph = EmptyGraph.instance();
//        GraphTraversalSource g = graph.traversal().withRemote(DriverRemoteConnection.using(cluster));
        GraphTraversalSource g = JanusGraphFactory.open("/Users/benoit/Documents/stage/tinkerPop/Project/ubirch-janusgraph-eval/janusgraph-0.3.1/conf/gremlin-server/janusgraph-cql-es-server.properties").traversal().withRemote(DriverRemoteConnection.using(cluster));

        final Bindings b = Bindings.instance();
        logger.info("areVertexesLinked(id1, id2, g) = " + areVertexesLinked(id1, id2, g));

        if(!areBothIdsAlreadyTaken(id1, id2, g)){
            logger.info("both IDs are already taken, those vertexes exist and are linked");
            cluster.close();
            return null;
        }


        Vertex v1 = g.addV(b.of(LABEL, "transaction")).property(NAME, b.of(NAME, nameOfVertex1)).property(ID, b.of(ID, id1)).next();

        Vertex v2 = g.addV(b.of(LABEL, "transaction")).property(NAME, nameOfVertex2).property(ID, id2).next();
        g.V(b.of(OUT_V, v1)).as("a").V(b.of(IN_V, v2)).addE(b.of(LABEL, "linked to")).from("a").next();

        cluster.close();
        return "OKI DOKI";
    }

    public static boolean isIdAlreadyTaken(int id, GraphTraversalSource g){
        GremlinPipeline pipe = new GremlinPipeline();
        pipe.start(g.V().has(T.label, "transaction").has(ID, id));
        return (pipe.toList().isEmpty());
    }

    public static boolean areBothIdsAlreadyTaken(int id1, int id2, GraphTraversalSource g){
        return (isIdAlreadyTaken(id1, g) || isIdAlreadyTaken(id2, g));
    }

    public static boolean areVertexesLinked(int id1, int id2, GraphTraversalSource g){
        GremlinPipeline pipe = new GremlinPipeline();
        Vertex v1 = getVertexAssociatedToId(id1, g);
        Vertex v2 = getVertexAssociatedToId(id2, g);

        GraphTraversal truc = g.V(v1).outE().where(inV().hasId(v2).id());

        return(!(truc == null));
    }

    public static Vertex getVertexAssociatedToId(int id, GraphTraversalSource g){
        Vertex truc = null;
        try {
            truc = g.V().has(T.label, "transaction").has(ID, id).next();
        } catch(Exception osef){}

            logger.info("The vertex with the label ID:" + Integer.toString(id) + " has the dabtabase id:" + truc.id().toString());
        return truc;
    }

}

