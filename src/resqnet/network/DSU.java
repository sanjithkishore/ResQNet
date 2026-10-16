package resqnet.network;

/** Disjoint Set Union with path compression and union by rank. */
public class DSU {
    private final int[] parent, rank;
    private int components;

    public DSU(int n) {
        parent = new int[n];
        rank = new int[n];
        components = n;
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    public int find(int x) {
        int root = x;
        while (parent[root] != root) root = parent[root];
        while (parent[x] != root) {          // path compression
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    /** @return true if a and b were in different sets (a merge happened). */
    public boolean union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;
        if (rank[ra] < rank[rb]) { int t = ra; ra = rb; rb = t; }
        parent[rb] = ra;
        if (rank[ra] == rank[rb]) rank[ra]++;
        components--;
        return true;
    }

    public int components() { return components; }
}