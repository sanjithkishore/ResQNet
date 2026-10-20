package resqnet.network;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public final class Prim {
    private record Candidate(double cost, Edge edge, int to) {}

    private Prim() {}

    /** Runs Prim from every unvisited node, so a disconnected graph gives a spanning forest. */
    public static MSTResult run(Graph graph) {
        int n = graph.vertexCount();
        boolean[] visited = new boolean[n];
        List<Edge> mst = new ArrayList<>();
        double total = 0;
        int components = 0;

        for (int s = 0; s < n; s++) {
            if (visited[s]) continue;
            components++;
            PriorityQueue<Candidate> pq = new PriorityQueue<>(Comparator.comparingDouble(Candidate::cost));
            visited[s] = true;
            push(graph, s, visited, pq);
            while (!pq.isEmpty()) {
                Candidate c = pq.poll();
                if (visited[c.to()]) continue;   // stale entry
                visited[c.to()] = true;
                mst.add(c.edge());
                total += c.cost();
                push(graph, c.to(), visited, pq);
            }
        }
        return new MSTResult(mst, total, components);
    }

    private static void push(Graph graph, int u, boolean[] visited, PriorityQueue<Candidate> pq) {
        for (Edge e : graph.neighbors(u)) {
            if (e.blocked) continue;
            int v = e.other(u);
            if (!visited[v]) pq.add(new Candidate(e.cost, e, v));
        }
    }
}