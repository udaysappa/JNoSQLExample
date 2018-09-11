package spring.janus.jnosql.integration;


import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.attribute.Geo;
import org.janusgraph.core.attribute.Geoshape;
import org.janusgraph.example.GraphOfTheGodsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;

public class JavaExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaExample.class);

    public static void main(String[] args)  throws Exception{
        JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-berkeleyje-lucene.properties");
        GraphTraversalSource g = graph.traversal();
        if (g.V().count().next() == 0) {
            // load the schema and graph data
            GraphOfTheGodsFactory.load(graph);
        }
        
        final Vertex marko = graph.addVertex("name", "marko", "age", 29);
        
        final Vertex vadas = graph.addVertex("name", "vadas", "age", 27);
        final Vertex lop = graph.addVertex("name", "lop", "lang", "java");
        final Vertex josh = graph.addVertex("name", "josh", "age", 32);
        final Vertex ripple = graph.addVertex("name", "ripple", "lang", "java");
        final Vertex peter = graph.addVertex("name", "peter", "age", 35);
        
        marko.addEdge("knows", vadas, "weight", 0.5f);
        marko.addEdge("knows", josh, "weight", 1.0f);
        marko.addEdge("created", lop, "weight", 0.4f);
        josh.addEdge("created", ripple, "weight", 1.0f);
        josh.addEdge("created", lop, "weight", 0.4f);
        peter.addEdge("created", lop, "weight", 0.2f);
        
        Map<String, ?> saturnProps = g.V().has("name", "saturn").valueMap(true).next();
        LOGGER.info("saturnProps :: "+saturnProps.toString());
        List<Edge> places = g.E().has("place", Geo.geoWithin(Geoshape.circle(37.97, 23.72, 50))).toList();
        
        Vertex fromNode = g.V().has("name", "marko").next();
        Vertex toNode = g.V().has("name", "peter").next();
        ArrayList list = new ArrayList();
        g.V(fromNode).repeat(both().simplePath()).until(is(toNode)).limit(1).path().fill(list);
        LOGGER.info("testing ::: "+list.toString());
        
        LOGGER.info(g.V().toString());
        LOGGER.info(places.toString());
        
        for (Edge e : places) {
        	
        	LOGGER.info(e.inVertex().value("name"));
        	LOGGER.info(e.outVertex().value("name"));
        	
        }
        
        System.exit(0);
    }
}
