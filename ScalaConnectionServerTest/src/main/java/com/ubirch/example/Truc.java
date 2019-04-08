package com.ubirch.example;

import com.tinkerpop.gremlin.java.GremlinPipeline;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.process.traversal.Bindings;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inV;

public class Truc {

    private static final String LABEL = "label";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String OUT_V = "outV";
    private static final String IN_V = "inV";
    private static final String TEST = "test";
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    private static GraphTraversalSource g;
    protected static Cluster cluster;
    protected static Client client;
    protected JanusGraph janusgraph;
    protected Configuration conf;
    static int ID1 = 6485621;
    static int ID2 = 684684;
    static int IDFAUX = 11;
    private static Bindings b;


    public static void main(String[] args) throws Exception {


        String str = addTwoVertexes("name1", 11, "name2", 111);
        logger.info(str);


    }

    public static String addTwoVertexes(String nameOfVertex1, int id1, String nameOfVertex2, int id2) throws Exception {

        Graph graph = EmptyGraph.instance();
        g = getTraversal(graph);
        b = Bindings.instance();
        logger.info("areVertexesLinked(id1, id2, g) = " + areVertexesLinked(id1, id2, g));

        int howMany = howManyExist(id1, id2);
        switch (howMany){
            case 0 : noneExit(id1, id2, nameOfVertex1, nameOfVertex2);
            case 1 :

        }

        if(!areBothIdsAlreadyTaken(id1, id2, g)){
            logger.info("both IDs are already taken, those vertexes exist and are linked");
            closeConnection();
            return null;
        }


        Vertex v1 = g.addV(b.of(LABEL, "transaction")).property(NAME, b.of(NAME, nameOfVertex1)).property(ID, b.of(ID, id1)).next();

        Vertex v2 = g.addV(b.of(LABEL, "transaction")).property(NAME, nameOfVertex2).property(ID, id2).next();
        g.V(b.of(OUT_V, v1)).as("a").V(b.of(IN_V, v2)).addE(b.of(LABEL, "linked to")).from("a").iterate();
        logger.info("coucou");

        closeConnection();

        return "OKI DOKI";
    }

    public static boolean isIdAlreadyTaken(int id){
        GremlinPipeline pipe = new GremlinPipeline();
        pipe.start(g.V().has(T.label, "transaction").has(ID, id));
        return (pipe.toList().isEmpty());
    }

    public static boolean areBothIdsAlreadyTaken(int id1, int id2, GraphTraversalSource g){
        return (isIdAlreadyTaken(id1) || isIdAlreadyTaken(id2));
    }

    public static boolean areVertexesLinked(int id1, int id2, GraphTraversalSource g){
        Vertex v1 = getVertexAssociatedToId(id1);
        Vertex v2 = getVertexAssociatedToId(id2);

        if((v1 != null && v2 != null)){

            GraphTraversal oneWay =  g.V(v1).outE().as("e").inV().has(ID, id2).select("e");
            GraphTraversal otherWay =  g.V(v2).outE().as("e").inV().has(ID, id1).select("e");
            return !oneWay.toList().isEmpty() || !otherWay.toList().isEmpty();
        }
        return false;

    }

    public static Vertex getVertexAssociatedToId(int id){
        Vertex truc = null;
        try {
            truc = g.V().has(T.label, "transaction").has(ID, id).next();
        } catch(Exception osef){
            logger.info("vertex does not exist");
            return null;
        }

        logger.info("The vertex with the label ID:" + Integer.toString(id) + " has the dabtabase id:" + truc.id().toString());
        return truc;
    }


    public static GraphTraversalSource getTraversal(Graph graph){
        GraphTraversalSource g = null;
        try {
            g = graph.traversal().withRemote("configuration/remote-graph.properties");
        } catch (Exception e) {
            logger.error("", e);
        }
        return g;
    }

    public static void closeConnection(){
        try {
            g.close();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static int howManyExist(int id1, int id2){
        Vertex v1 = getVertexAssociatedToId(id1);
        Vertex v2 = getVertexAssociatedToId(id2);
        int i = 0;
        if (v1 != null) {i++;}
        if (v2 != null) {i++;}
        return i;
    }

    public static void noneExit(int id1, int id2, String nameOfVertex1, String nameOfVertex2){

        Vertex v1 = g.addV(b.of(LABEL, "transaction")).property(NAME, b.of(NAME, nameOfVertex1)).property(ID, b.of(ID, id1)).next();

        Vertex v2 = g.addV(b.of(LABEL, "transaction")).property(NAME, nameOfVertex2).property(ID, id2).next();
        g.V(b.of(OUT_V, v1)).as("a").V(b.of(IN_V, v2)).addE(b.of(LABEL, "linked to")).from("a").iterate();
    }

    public static void oneExist(int id1, int id2, String nameOfVertex1, String nameOfVertex2)
}
