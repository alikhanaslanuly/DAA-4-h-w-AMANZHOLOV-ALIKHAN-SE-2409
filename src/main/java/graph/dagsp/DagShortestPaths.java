package graph.dagsp;
import graph.scc.SccTarjan;
import util.metrics.metrics;
import java.util.*;
public class DagShortestPaths {
    private SccTarjan.JsonGraphCondensation g;
    private metrics m;
    public DagShortestPaths(SccTarjan.JsonGraphCondensation g, metrics m){
        this.g = g; this.m = m;
    }
    
    private List<Integer> topoOrder(){
        int n = g.n;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i=0;i<n;i++) adj.add(new ArrayList<>());
        for (var e : g.edges) adj.get(e.u).add(e.v);
        boolean[] vis = new boolean[n];
        List<Integer> order = new ArrayList<>();
        for (int i=0;i<n;i++) if (!vis[i]) dfs(i, vis, adj, order);
        Collections.reverse(order);
        return order;
    }
    private void dfs(int u, boolean[] vis, List<List<Integer>> adj, List<Integer> order){
        vis[u]=true;
        for (int v: adj.get(u)) if (!vis[v]) dfs(v, vis, adj, order);
        order.add(u);
    }

    public long[] shortest_paths(int source){
        long start = System.nanoTime();
        int n = g.n;
        List<List<Edge>> adj = new ArrayList<>();
        for (int i=0;i<n;i++) adj.add(new ArrayList<>());
        for (var e : g.edges) adj.get(e.u).add(new Edge(e.v, e.w));
        long INF = Long.MAX_VALUE/4;
        long[] dist = new long[n]; Arrays.fill(dist, INF); dist[source]=0;
        int relaxations=0;
        List<Integer> topo = topoOrder();
        for (int u : topo){
            if (dist[u]==INF) continue;
            for (var ed : adj.get(u)){
                if (dist[ed.to] > dist[u] + ed.w){
                    dist[ed.to] = dist[u] + ed.w;
                    relaxations++; m.add_relaxation();
                }
            }
        }
        m.add_time(System.nanoTime()-start);
        return dist;
    }

    public static class PathResult { public long length; public List<Integer> path; public PathResult(long l, List<Integer> p){ length=l; path=p; } }

    public PathResult longest_path(){
        long start = System.nanoTime();
        int n = g.n;
        List<List<Edge>> adj = new ArrayList<>();
        for (int i=0;i<n;i++) adj.add(new ArrayList<>());
        for (var e : g.edges) adj.get(e.u).add(new Edge(e.v, e.w));
        List<Integer> topo = topoOrder();
        long NEG = Long.MIN_VALUE/4;
        long[] dp = new long[n]; Arrays.fill(dp, NEG);
        int[] parent = new int[n]; Arrays.fill(parent, -1);
        for (int s : topo){
            if (dp[s]==NEG) dp[s]=0;
            for (int uidx = topo.indexOf(s); uidx<topo.size(); uidx++){
                int u = topo.get(uidx);
                if (dp[u]==NEG) continue;
                for (var e : adj.get(u)){
                    if (dp[e.to] < dp[u] + e.w){
                        dp[e.to] = dp[u] + e.w;
                        parent[e.to] = u;
                    }
                }
            }
        }
        long best = NEG; int bi=-1;
        for (int i=0;i<n;i++) if (dp[i]>best){ best=dp[i]; bi=i; }
        List<Integer> path = new ArrayList<>();
        if (bi!=-1){
            int cur = bi;
            while(cur!=-1){ path.add(cur); cur = parent[cur]; }
            Collections.reverse(path);
        }
        m.add_time(System.nanoTime()-start);
        return new PathResult(best, path);
    }

    private static class Edge{ int to; long w; Edge(int to,long w){this.to=to;this.w=w;} }
}
