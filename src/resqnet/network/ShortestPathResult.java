package resqnet.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ShortestPathResult {
    public final int source;
    public final double[] dist;
    private final int[] prev;
    public final int expanded;   // nodes settled: used to benchmark Dijkstra vs A*

    public ShortestPathResult(int source, double[] dist, int[] prev, int expanded) {
        this.source = source; this.dist = dist; this.prev = prev; this.expanded = expanded;
    }

    public boolean reachable(int t) { return dist[t] < Double.POSITIVE_INFINITY; }

    /** Empty list if unreachable. For A*, only valid for the target it was run with. */
    public List<Integer> pathTo(int t) {
        if (!reachable(t)) return List.of();
        List<Integer> path = new ArrayList<>();
        for (int v = t; v != -1; v = prev[v]) path.add(v);
        Collections.reverse(path);
        return path;
    }
}