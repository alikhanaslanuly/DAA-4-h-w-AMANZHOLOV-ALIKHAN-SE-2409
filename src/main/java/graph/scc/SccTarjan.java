package graph.scc;
import util.io.JsonGraph;
import util.metrics.metrics;
import java.util.*;

public class SccTarjan {
    private JsonGraph g;
    private metrics m;
    private int n;
    private List<List<Integer>> adj;
    private int time = 0;
    private int[] disc, low;
    private boolean[] in_stack;
    private Deque<Integer> stack = new ArrayDeque<>();

    public SccTarjan(JsonGraph g, metrics m){
        this.g = g; this.m = m; this.n = g.n;
        adj = new ArrayList<>();
        for (int i=0;i<n;i++) adj.add(new ArrayList<>());
        for (var e : g.edges){
            adj.get(e.u).add(e.v);
        }
    }

    public List<List<Integer>> run(){
        disc = new int[n]; Arrays.fill(disc, -1);
        low = new int[n]; in_stack = new boolean[n];
        List<List<Integer>> comps = new ArrayList<>();
        for (int i=0;i<n;i++) if (disc[i]==-1) dfs(i, comps);
        return comps;
    }

    private void dfs(int u, List<List<Integer>> comps){
        disc[u]=low[u]=time++; m.add_dfs_visit();
        stack.push(u); in_stack[u]=true;
        for (int v : adj.get(u)){
            m.add_dfs_edge();
            if (disc[v]==-1){
                dfs(v, comps);
                low[u] = Math.min(low[u], low[v]);
            } else if (in_stack[v]){
                low[u] = Math.min(low[u], disc[v]);
            }
        }
        if (low[u]==disc[u]){
            List<Integer> comp = new ArrayList<>();
            while (true){
                int w = stack.pop(); in_stack[w]=false;
                comp.add(w);
                if (w==u) break;
            }
            comps.add(comp);
        }
    }

    public JsonGraphCondensation buildCondensation(List<List<Integer>> comps){
        int cn = comps.size();
        Map<Integer,Integer> comp_id = new HashMap<>();
        for (int i=0;i<cn;i++) for (int v : comps.get(i)) comp_id.put(v,i);
        JsonGraphCondensation cg = new JsonGraphCondensation(cn);
        for (int u=0;u<n;u++){
            for (int v : adj.get(u)){
                int cu = comp_id.get(u), cv = comp_id.get(v);
                if (cu!=cv) cg.addEdge(cu, cv);
            }
        }
        return cg;
    }

    public List<Integer> derivedOrderFromCompOrder(List<Integer> comp_order, List<List<Integer>> comps){
        List<Integer> order = new ArrayList<>();
        for (int cid : comp_order){
            var block = comps.get(cid);
            for (int v : block) order.add(v);
        }
        return order;
    }

    public static class JsonGraphCondensation {
        public int n;
        public List<Edge> edges = new ArrayList<>();
        private Set<String> used = new HashSet<>();
        public JsonGraphCondensation(int n){ this.n=n; }
        public static class Edge{ public int u,v; public long w=1; public Edge(int u,int v){this.u=u;this.v=v;} }
        public void addEdge(int u,int v){ String key=u+"->"+v; if (!used.contains(key)){ used.add(key); edges.add(new Edge(u,v)); } }
    }
}
