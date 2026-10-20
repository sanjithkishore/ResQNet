package resqnet;

import resqnet.network.*;

public class Module1Demo {
    public static void main(String[] args) {
        Graph g = new Graph(8);
        double[][] pos = {{0,0},{4,0},{8,0},{0,4},{4,4},{8,4},{20,20},{30,30}};
        for (int i = 0; i < pos.length; i++) g.setCoordinates(i, pos[i][0], pos[i][1]);

        g.addEdge(0, 1, 4.0);   // id 0
        g.addEdge(1, 2, 4.5);   // id 1
        g.addEdge(0, 3, 4.0);   // id 2
        g.addEdge(1, 4, 4.2);   // id 3
        g.addEdge(2, 5, 4.0);   // id 4
        g.addEdge(3, 4, 4.4);   // id 5
        g.addEdge(4, 5, 4.0);   // id 6
        g.addEdge(5, 6, 21.0);  // id 7   (node 7 is deliberately isolated)

        MSTResult k = Kruskal.run(g), p = Prim.run(g);
        System.out.printf("Kruskal: cost=%.1f edges=%d components=%d%n", k.totalCost, k.edges.size(), k.components);
        System.out.printf("Prim   : cost=%.1f edges=%d components=%d%n", p.totalCost, p.edges.size(), p.components);

        ShortestPathResult d = Dijkstra.run(g, 0, 6);
        ShortestPathResult a = AStar.run(g, 0, 6);
        System.out.println("Dijkstra 0->6: " + d.pathTo(6) + " dist=" + d.dist[6] + " expanded=" + d.expanded);
        System.out.println("A*       0->6: " + a.pathTo(6) + " dist=" + a.dist[6] + " expanded=" + a.expanded);
        System.out.println("0->7 reachable? " + Dijkstra.run(g, 0, -1).reachable(7));

        BridgeFinder.Result r = BridgeFinder.run(g);
        System.out.println("Bridges: " + r.bridges);
        for (int i = 0; i < g.vertexCount(); i++) if (r.articulation[i]) System.out.println("Articulation point: " + i);

        System.out.println("--- road 1-4 destroyed ---");
        System.out.println("Before: " + Dijkstra.run(g, 0, 5).pathTo(5));
        g.blockEdge(3);
        System.out.println("After : " + Dijkstra.run(g, 0, 5).pathTo(5));
        MSTResult k2 = Kruskal.run(g);
        System.out.printf("Backbone recomputed: cost=%.1f components=%d%n", k2.totalCost, k2.components);
    }
}