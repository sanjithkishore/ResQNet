package resqnet.network;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public final class Dijkstra {
    private record State(int node, double dist) {}

    private Dijkstra() {}

    /** @param target pass -1 to compute distances to every node. */
    public static ShortestPathResult run(Graph graph, int source, int target) {
        int n = graph.vertexCount();
        graph.checkVertex(source);
        if (target != -1) graph.checkVertex(target);

        double[] dist = new double[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingDouble(State::dist));
        dist[source] = 0;
        pq.add(new State(source, 0));
        int expanded = 0;

        while (!pq.isEmpty()) {
            State s = pq.poll();
            int u = s.node();
            if (s.dist() > dist[u]) continue;   // stale entry (lazy deletion)
            expanded++;
            if (u == target) break;
            for (Edge e : graph.neighbors(u)) {
                if (e.blocked) continue;
                int v = e.other(u);
                double nd = dist[u] + e.cost;
                if (nd < dist[v]) {
                    dist[v] = nd;
                    prev[v] = u;
                    pq.add(new State(v, nd));
                }
            }
        }
        return new ShortestPathResult(source, dist, prev, expanded);
    }
}