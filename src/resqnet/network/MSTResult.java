package resqnet.network;

import java.util.List;

public final class MSTResult {
    public final List<Edge> edges;
    public final double totalCost;
    public final int components;   // 1 = fully connected, >1 = spanning forest

    public MSTResult(List<Edge> edges, double totalCost, int components) {
        this.edges = edges; this.totalCost = totalCost; this.components = components;
    }

    public boolean isConnected() { return components <= 1; }
}