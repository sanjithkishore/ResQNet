package resqnet.network;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * A* with a straight-line-distance heuristic.
 * Correct (optimal) only if every edge cost >= the Euclidean distance between its endpoints,
 * which holds for real roads (a road is never shorter than the straight line).
 */
public final class AStar {
    private record State(int node, double g, double f) {}

    private AStar() {}

    public static ShortestPathResult run(Graph graph, int source, int target) {
        int n = graph.vertexCount();
        graph.checkVertex(source);
        graph.checkVertex(target);

        double[] dist = new double[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingDouble(State::f));
        dist[source] = 0;
        pq.add(new State(source, 0, graph.euclid(source, target)));
        int expanded = 0;

        while (!pq.isEmpty()) {
            State s = pq.poll();
            int u = s.node();
            if (s.g() > dist[u]) continue;
            expanded++;
            if (u == target) break;
            for (Edge e : graph.neighbors(u)) {
                if (e.blocked) continue;
                int v = e.other(u);
                double ng = dist[u] + e.cost;
                if (ng < dist[v]) {
                    dist[v] = ng;
                    prev[v] = u;
                    pq.add(new State(v, ng, ng + graph.euclid(v, target)));
                }
            }
        }
        return new ShortestPathResult(source, dist, prev, expanded);
    }
}