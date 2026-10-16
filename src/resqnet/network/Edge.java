package resqnet.network;

/** An undirected road/communication link. Shared by both endpoints' adjacency lists. */
public final class Edge {
    public final int id;
    public final int u, v;
    public final double cost;
    public boolean blocked;   // set true when a disaster destroys this link

    public Edge(int id, int u, int v, double cost) {
        this.id = id; this.u = u; this.v = v; this.cost = cost;
    }

    public int other(int x) { return x == u ? v : u; }

    @Override
    public String toString() { return "(" + u + "-" + v + ", cost=" + cost + ")"; }
}