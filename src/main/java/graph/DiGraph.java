package graph;

import java.util.*;

/**
 * This class represents a mutable Directed Labeled Graph
 *
 * Specification fields:
 * @spec.specfield Graph        : HashMap &lt;Node, Set&lt;LabeledEdge&lt;&lt;
 *                              // node and a set of its outgoing edges
 *
 * Abstract Invariant:
 *  no duplicate of nodes, every node must have at least 1 outgoing edges
 */

public class DiGraph<Node, T>{
    // Representation Invariant
    // RI(this) = this != null &&
    //            this.node != null && this.neighbors != null &&
    //            && each node must have at least 1 labeled edge
    //
    // Abstraction Function
    // AF(this) = Directed Labeled Graph where
    //            this.node : {} , this.neighbors = {}    if graph is empty
    //            this.node : {n1, n2, ... nk} ,
    //            this.neighbors = {n1 = {...}, n2 = {...}, ... , nk = {...}} otherwise
    //            n1, n2, ... nk represents nodes in this.graph
    //            n1 = {...} represents the set of neighbors of n1

    private HashMap<Node, Set<LabeledEdge<Node, T>>> graph;

    private final static boolean CHECK_REP = false;

    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        if (CHECK_REP) {
            assert (graph != null) : "graph cannot be null";
            Set<Node> nodes = graph.keySet();
            assert (nodes != null) : "set of nodes cannot be null";
            for (Node n : nodes) {
                assert (n != null) : "node cannot be null";
                Set<LabeledEdge<Node, T>> neighbors = graph.get(n);
                assert (neighbors != null) : "set of neighbors cannot be null";
                if (!neighbors.isEmpty()) {
                    assert(neighbors.size() >= 1) : "each node must have at least 1 labeled edge";
                }
                for (LabeledEdge edge : neighbors) {
                    assert(edge != null) : "neighbor cannot be null";
                }
            }
        }
    }

    /**
     * Creates an empty Directed Labeled Graph
     *
     * @spec.effects constructs an empty directed labeled graph
     */
    public DiGraph() {
        graph = new HashMap<>();
        checkRep();
    }

    /**
     * Adds node to the graph if it's not already in the graph
     *
     * @param n node to be added
     * @throws IllegalArgumentException if n is null
     *
     * @spec.requires n != null and !this.graph.contains(n)
     * @spec.modifies this.graphs
     * @spec.effects adds a new node to this.graph
     */
    public void addNode(Node n) {
        assert (!graph.containsKey(n)) : "node already exists";
        if (n.equals(null)) throw new IllegalArgumentException("node cannot be null");
        checkRep();
        graph.put(n, new HashSet<>());
        checkRep();
    }

    /**
     * Adds an edge to the graph
     *
     * @param from origin of the edge
     * @param to the destination of the edge
     * @param label of the edge
     * @throws IllegalArgumentException if from or to is null
     *                                  if label is null
     *
     * @spec.requires from != null and to != null and label != null
     *              and this.graph.contains(from)
     * @spec.modifies this.graph
     * @spec.effects adds an edge with label between 2 nodes to this.graph
     *               if the destination node does not exist in graph, it is added
     */
    public void addEdge(Node from, Node to, T label) {
        assert(graph.containsKey(from)) : "parent node must exist in the graph";
        if (from.equals(null) || to.equals(null))
            throw new IllegalArgumentException("parent or child node cannot be null");
        if (label.equals(null))
            throw new IllegalArgumentException("edge label cannot be null");
        checkRep();
        LabeledEdge<Node, T> e = new LabeledEdge<>(from, to, label);
        HashSet<LabeledEdge<Node, T>> eSet = new HashSet<>();
        eSet.add(e);
        graph.merge(from, eSet, (prev, val) -> {
            if (prev.equals(val)) {
                return prev;
            }
            prev.add(e);
            return prev;
        });
        if (!graph.containsKey(to)) {
            addNode(to);
        }
        checkRep();
    }

    /**
     * Returns the number of nodes in the graph
     *
     * @return the number of nodes in the graph
     */
    public int size() {
        checkRep();
        return graph.size();
    }

    /**
     * Returns whether graph is empty
     *
     * @return true if there is no nodes in this.nodes
     */
    public boolean isEmpty() {
        checkRep();
        return graph.isEmpty();
    }

    /**
     * Returns whether Node n is in the graph
     *
     * @param n node
     * @return true if node is in this.nodes
     * @throws IllegalArgumentException if n is null
     *
     * @spec.requires n != null
     */
    public boolean containsNode(Node n) {
        if (n.equals(null)) throw new IllegalArgumentException("node cannot be null");
        checkRep();
        return graph.containsKey(n);
    }

    /**
     * Returns whether LabeledEdge e is in the graph
     *
     * @param e labeled edge
     * @return true if edge is in this.graph
     * @throws IllegalArgumentException if e is null
     *
     * @spec.requires e != null
     */
    public boolean containsEdge(LabeledEdge e) {
        if (e.equals(null)) throw new IllegalArgumentException("labeled edge cannot be null");
        Set<LabeledEdge<Node, T>> neighbors = graph.get(e.getFrom());
        checkRep();
        return neighbors.contains(e);
    }

    /**
     * Returns a list of all nodes in the graph
     *
     * @return a list of all nodes in this.graph
     */
    public List<Node> listNodes() {
        checkRep();
        List<Node> ret = new ArrayList<>();
        ret.addAll(graph.keySet());
        checkRep();
        return ret;
    }

    /**
     * returns a list of labeled edges from a node
     *
     * @param n node
     * @return a list of all labeled edges from node n
     * @throws IllegalArgumentException if n is not in this.node
     *                                  if n is null
     *
     * @spec.requires n != null and this.nodes.contains(node)
     */
    public List<LabeledEdge<Node, T>> listChildren(Node n) {
        if (n.equals(null)) throw new IllegalArgumentException("node cannot be null");
        if (!this.containsNode(n)) throw new IllegalArgumentException("node is not in the graph");
        checkRep();
        List<LabeledEdge<Node, T>> ret = new ArrayList<>(graph.get(n));
        checkRep();
        return ret;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ////  Labeled Edge
    ///////////////////////////////////////////////////////////////////////////////////////

    /** Inner Class that represent an immutable Labeled edge
     *
     * @spec.specfield from : Node    // origin node
     * @spec.specfield to   : Node    // destination node
     * @spec.specfield label: T       // label of the edge
     */
    public static class LabeledEdge<Node, T> {
        // Representation Invariant
        // RI(this) = from != null && to != null && label != null
        //
        // Abstraction Function
        // AF(this) = LabeledEdge is a directed edge between 2 nodes where
        //            this.from is the origin node
        //            this.to is the destination node
        //            this.label is the label of the edge

        private Node from;
        private Node to;
        private T label;

        private void checkRep() {
            assert(!this.from.equals(null));
            assert(!this.to.equals(null));
            assert(!this.label.equals(null));
        }

        /**
         * Constructs a new labeled edge
         * @param from origin of the edge
         * @param to the destination of the edge
         * @param label of the edge
         * @throws IllegalArgumentException if from or to is null
         *
         * @spec.requires from != null and to != null
         * @spec.effects constructs a new labeled edge with String from, to and label
         */
        public LabeledEdge(Node from, Node to, T label) {
            if (from.equals(null) || to.equals(null))
                throw new IllegalArgumentException("from and to nodes cannot be null");
            this.from = from;
            this.to = to;
            this.label = label;
            checkRep();
        }

        /**
         * Accessor method to return origin node
         *
         * @return this.from
         */
        public Node getFrom() {
            checkRep();
            return from;
        }

        /**
         * Accessor method to return destination node
         *
         * @return this.to
         */
        public Node getTo() {
            checkRep();
            return to;
        }

        /**
         * Accessor method to return the edge's label
         *
         * @return this.label
         */
        public T getLabel() {
            checkRep();
            return label;
        }

        /**
         * overrides the default equal function
         *
         * @param o to be compared for equality
         * @return true iff o is an instance of Labeled Edge and o and this
         *         represents same Labeled Edge
         */
        @Override
        public boolean equals(Object o) {
            checkRep();
            if (o instanceof LabeledEdge) {
                LabeledEdge e = (LabeledEdge) o;
                checkRep();
                return (this.getFrom().equals(e.getFrom()) &&
                        this.getTo().equals(e.getTo()) &&
                        this.getLabel().equals(e.getLabel()));
            } else {
                checkRep();
                return false;
            }
        }

        /**
         * overrides the default hashcode function
         *
         * @return int that all labeled edges equal to this will return
         */
        @Override
        public int hashCode() {
            checkRep();
            return (from.hashCode() + to.hashCode() + label.hashCode());
        }
    }
}
