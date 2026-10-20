package resqnet.network;

import java.util.ArrayList;
import java.util.List;

/** Tarjan's algorithm (iterative, so 100k+ nodes can't overflow the call stack). */
public final class BridgeFinder {
    public static final class Result {
        public final List<Edge> bridges;
        public final boolean[] articulation;
        Result(List<Edge> bridges, boolean[] articulation) {
            this.bridges = bridges; this.articulation = articulation;
        }
    }

    private BridgeFinder() {}

    public static Result run(Graph graph) {
        int n = graph.vertexCount();
        int[] disc = new int[n], low = new int[n], parentEdge = new int[n], it = new int[n];
        boolean[] articulation = new boolean[n];
        List<Edge> bridges = new ArrayList<>();
        int[] stack = new int[n];
        int timer = 0;

        for (int s = 0; s < n; s++) {
            if (disc[s] != 0) continue;
            int top = 0, rootChildren = 0;
            stack[top++] = s;
            disc[s] = low[s] = ++timer;
            parentEdge[s] = -1;

            while (top > 0) {
                int u = stack[top - 1];
                List<Edge> nb = graph.neighbors(u);
                if (it[u] < nb.size()) {
                    Edge e = nb.get(it[u]++);
                    if (e.blocked || e.id == parentEdge[u]) continue;   // skip only the exact parent edge
                    int v = e.other(u);
                    if (disc[v] == 0) {                                  // tree edge
                        parentEdge[v] = e.id;
                        disc[v] = low[v] = ++timer;
                        stack[top++] = v;
                        if (u == s) rootChildren++;
                    } else {                                             // back edge
                        low[u] = Math.min(low[u], disc[v]);
                    }
                } else {                                                 // u finished
                    top--;
                    if (top > 0) {
                        int p = stack[top - 1];
                        low[p] = Math.min(low[p], low[u]);
                        if (low[u] > disc[p]) bridges.add(graph.getEdge(parentEdge[u]));
                        if (p != s && low[u] >= disc[p]) articulation[p] = true;
                    }
                }
            }
            if (rootChildren > 1) articulation[s] = true;
        }
        return new Result(bridges, articulation);
    }
}