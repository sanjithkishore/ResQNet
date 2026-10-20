package resqnet;

import java.util.Random;
import resqnet.network.*;

public class PrimCheck {
    static int failures = 0;

    static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS  " : "FAIL  ") + name);
        if (!ok) failures++;
    }

    public static void main(String[] args) {
        // Same demo graph as KruskalCheck (node 7 isolated)
        Graph g = new Graph(8);
        g.addEdge(0, 1, 4.0);
        g.addEdge(1, 2, 4.5);
        g.addEdge(0, 3, 4.0);
        g.addEdge(1, 4, 4.2);
        g.addEdge(2, 5, 4.0);
        g.addEdge(3, 4, 4.4);
        g.addEdge(4, 5, 4.0);
        g.addEdge(5, 6, 21.0);

        MSTResult p = Prim.run(g);
        System.out.printf("Prim: cost=%.1f edges=%d components=%d%n", p.totalCost, p.edges.size(), p.components);
        check("Prim cost is 41.2", Math.abs(p.totalCost - 41.2) < 1e-9);
        check("Prim has 6 edges", p.edges.size() == 6);
        check("Prim finds 2 components", p.components == 2);

        g.blockEdge(4);
        check("after blocking 2-5, Prim cost is 41.7", Math.abs(Prim.run(g).totalCost - 41.7) < 1e-9);

        check("empty graph", Prim.run(new Graph(0)).edges.isEmpty());
        check("single node is connected", Prim.run(new Graph(1)).isConnected());

        // Cross-check: Prim and Kruskal must agree on 200 random graphs
        Random rnd = new Random(42);
        boolean allMatch = true;
        for (int t = 0; t < 200; t++) {
            int n = 2 + rnd.nextInt(30);
            Graph rg = new Graph(n);
            int m = rnd.nextInt(n * 3);
            for (int i = 0; i < m; i++) rg.addEdge(rnd.nextInt(n), rnd.nextInt(n), 1 + rnd.nextInt(50));
            MSTResult a = Kruskal.run(rg), b = Prim.run(rg);
            if (Math.abs(a.totalCost - b.totalCost) > 1e-9 || a.components != b.components) allMatch = false;
        }
        check("Prim == Kruskal on 200 random graphs (cost and components)", allMatch);

        System.out.println(failures == 0 ? "\nALL CHECKS PASSED" : "\n" + failures + " CHECK(S) FAILED");
    }
}