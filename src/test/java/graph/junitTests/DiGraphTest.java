package graph.junitTests;

import graph.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.List;

import static org.junit.Assert.*;

public final class DiGraphTest {

    @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    public final int TEST_SIZE = 10;

    public DiGraph<String, String> graph0 = new DiGraph<>();
    public DiGraph<String, String> graph1 = new DiGraph<>();
    public DiGraph<String, String> graph2 = new DiGraph<>();

    public void populateGraph1() {
        graph1.addNode("1");
    }

    public void populateGraph2() {
        for (int i = 0; i < TEST_SIZE; i++) {
            graph2.addNode(Integer.toString(i));
        }
    }

    public void addEdge(DiGraph<String, String> graph) {
        List<String> nodes = graph.listNodes();
        for (int i = 0; i < nodes.size()-1; i++) {
           graph.addEdge(nodes.get(i), Integer.toString(i+1), Integer.toString(i));
        }
    }

    @Before
    public void setUp() {
        populateGraph1();
        populateGraph2();
        addEdge(graph1);
        addEdge(graph2);
    }

    @Test
    public void testSize() {
        // test size of an empty graph
        assertEquals(0, graph0.size());

        // test size of graph of 1
        assertEquals(1, graph1.size());

        // test size of graph of 10
        assertEquals(TEST_SIZE, graph2.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(graph0.isEmpty());
        assertFalse(graph1.isEmpty());
        assertFalse(graph2.isEmpty());
    }

    @Test
    public void testContainsNode() {
        //DiGraph.Node n = new DiGraph.Node("1", 1);
        String n = "1";
        assertFalse(graph0.containsNode(n));
        assertTrue(graph1.containsNode(n));
        assertTrue(graph2.containsNode(n));
    }

    @Test
    public void testContainsEdge() {
        List<String> nodes = graph2.listNodes();
        for (int i = 0; i < nodes.size(); i++) {
            List<DiGraph.LabeledEdge<String, String>> edges = graph2.listChildren(nodes.get(i));
            for (DiGraph.LabeledEdge e : edges) {
                assertTrue(graph2.containsEdge(e));
            }
        }
    }

    @Test
    public void testLabeledEdgeConstructor() {
        for (int i=0; i<TEST_SIZE-1; i++) {
            String n1 = Integer.toString(i);
            String n2 = Integer.toString(i+1);
            new DiGraph.LabeledEdge<>(n1, n2, n1);
        }
    }

    @Test
    public void testLabeledEdgeEquals() {
        for (int i=0; i<TEST_SIZE-1; i++) {
            String n1 = Integer.toString(i);
            String n2 = Integer.toString(i+1);
            String n3 = Integer.toString(i+1);
            DiGraph.LabeledEdge<String, String> e1 = new DiGraph.LabeledEdge<>(n1, n2, Integer.toString(i));
            DiGraph.LabeledEdge<String, String> e2 = new DiGraph.LabeledEdge<>(n1, n3, Integer.toString(i));
            assertEquals(e1, e2);
        }
    }
}
