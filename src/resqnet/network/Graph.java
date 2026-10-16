package resqnet.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Undirected weighted graph stored as an adjacency list. */
public class Graph {
    private final int n;
    private final List<List<Edge>> adj;
    private final List<Edge> edges = new ArrayList<>();
    private final double[] xs, ys;   // coordinates, used by A* and the map

    public Graph(int n) {
        if (n < 0) throw new IllegalArgumentException("vertex count must be >= 0");
        this.n = n;
        adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        xs = new double[n];
        ys = new double[n];
    }

    public int vertexCount() { return n; }
    public int edgeCount() { return edges.size(); }

    public void checkVertex(int v) {
        if (v < 0 || v >= n) throw new IllegalArgumentException("invalid vertex: " + v);
    }

    public int addEdge(int u, int v, double cost) {
        checkVertex(u);
        checkVertex(v);
        if (cost < 0) throw new IllegalArgumentException("negative cost not allowed");
        Edge e = new Edge(edges.size(), u, v, cost);
        edges.add(e);
        adj.get(u).add(e);
        if (u != v) adj.get(v).add(e);   // a self-loop is stored once
        return e.id;
    }

    public List<Edge> neighbors(int u) { checkVertex(u); return adj.get(u); }
    public List<Edge> edges() { return Collections.unmodifiableList(edges); }
    public Edge getEdge(int id) { return edges.get(id); }

    public void blockEdge(int id) { edges.get(id).blocked = true; }
    public void unblockEdge(int id) { edges.get(id).blocked = false; }

    public void setCoordinates(int v, double x, double y) { checkVertex(v); xs[v] = x; ys[v] = y; }
    public double x(int v) { return xs[v]; }
    public double y(int v) { return ys[v]; }

    public double euclid(int a, int b) { return Math.hypot(xs[a] - xs[b], ys[a] - ys[b]); }
}