package graph.topo;
import graph.scc.SccTarjan;
import util.metrics.metrics;
import java.util.*;

public class TopologicalSorter {
    private SccTarjan.JsonGraphCondensation g;
    private metrics m;
    public TopologicalSorter(SccTarjan.JsonGraphCondensation g, metrics m){
        this.g = g; this.m = m;
    }

    public List<Integer> kahn(){
        int n = g.n;
        int[] indeg = new int[n];
        List<List<Integer>> adj = new ArrayList<>();
        for (int i=0;i<n;i++) adj.add(new ArrayList<>());
        for (var e : g.edges){
            adj.get(e.u).add(e.v);
            indeg[e.v]++;
        }
        Deque<Integer> q = new ArrayDeque<>();
        for (int i=0;i<n;i++) if (indeg[i]==0){ q.add(i); m.add_kahn_push(); }
        List<Integer> order = new ArrayList<>();
        while(!q.isEmpty()){
            int u = q.removeFirst(); m.add_kahn_pop();
            order.add(u);
            for (int v : adj.get(u)){
                indeg[v]--;
                if (indeg[v]==0){ q.add(v); m.add_kahn_push(); }
            }
        }
        return order;
    }
}
