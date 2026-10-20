package resqnet;

import resqnet.network.*;

public class KruskalCheck {
    static int failures = 0;

    static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS  " : "FAIL  ") + name);
        if (!ok) failures++;
    }

    public static void main(String[] args) {
        // ---- DSU ----
        DSU d = new DSU(5);
        check("DSU starts with 5 components", d.components() == 5);
        check("union(0,1) merges", d.union(0, 1));
        check("union(0,1) again does nothing", !d.union(0, 1));
        d.union(1, 2);
        check("0 and 2 are connected", d.find(0) == d.find(2));
        check("0 and 4 are not connected", d.find(0) != d.find(4));
        check("components now 3", d.components() == 3);

        // ---- Kruskal on the 8-node demo graph (node 7 is isolated) ----
        Graph g = new Graph(8);
        g.addEdge(0, 1, 4.0);   // id 0
        g.addEdge(1, 2, 4.5);   // id 1
        g.addEdge(0, 3, 4.0);   // id 2
        g.addEdge(1, 4, 4.2);   // id 3
        g.addEdge(2, 5, 4.0);   // id 4
        g.addEdge(3, 4, 4.4);   // id 5
        g.addEdge(4, 5, 4.0);   // id 6
        g.addEdge(5, 6, 21.0);  // id 7

        MSTResult r = Kruskal.run(g);
        System.out.printf("Kruskal: cost=%.1f edges=%d components=%d%n", r.totalCost, r.edges.size(), r.components);
        check("MST cost is 41.2", Math.abs(r.totalCost - 41.2) < 1e-9);
        check("MST has 6 edges", r.edges.size() == 6);
        check("2 components (node 7 isolated)", r.components == 2);
        check("graph reported as not connected", !r.isConnected());

        // ---- Road destroyed: block edge 2-5 (id 4) ----
        g.blockEdge(4);
        MSTResult r2 = Kruskal.run(g);
        System.out.printf("After blocking 2-5: cost=%.1f components=%d%n", r2.totalCost, r2.components);
        check("backbone reroutes through 1-2, cost 41.7", Math.abs(r2.totalCost - 41.7) < 1e-9);
        g.unblockEdge(4);
        check("unblocking restores cost 41.2", Math.abs(Kruskal.run(g).totalCost - 41.2) < 1e-9);

        // ---- Edge cases ----
        MSTResult empty = Kruskal.run(new Graph(0));
        check("empty graph: no edges, cost 0", empty.edges.isEmpty() && empty.totalCost == 0);
        MSTResult single = Kruskal.run(new Graph(1));
        check("single node: connected, no edges", single.isConnected() && single.edges.isEmpty());

        boolean threw = false;
        try { g.addEdge(0, 99, 1.0); } catch (IllegalArgumentException e) { threw = true; }
        check("invalid vertex throws exception", threw);

        System.out.println(failures == 0 ? "\nALL CHECKS PASSED" : "\n" + failures + " CHECK(S) FAILED");
    }
}