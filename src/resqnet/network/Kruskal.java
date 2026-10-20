package resqnet.network;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Kruskal {
    private Kruskal() {}

    public static MSTResult run(Graph graph) {
        int n = graph.vertexCount();
        List<Edge> usable = new ArrayList<>();
        for (Edge e : graph.edges()) if (!e.blocked) usable.add(e);
        usable.sort(Comparator.comparingDouble(e -> e.cost));

        DSU dsu = new DSU(n);
        List<Edge> mst = new ArrayList<>();
        double total = 0;
        for (Edge e : usable) {
            if (dsu.union(e.u, e.v)) {
                mst.add(e);
                total += e.cost;
                if (mst.size() == n - 1) break;   // tree complete
            }
        }
        return new MSTResult(mst, total, dsu.components());
    }
}