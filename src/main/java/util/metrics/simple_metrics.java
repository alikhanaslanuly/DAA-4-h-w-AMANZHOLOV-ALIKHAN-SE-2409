package util.metrics;
public class simple_metrics implements metrics {
    private long dfs_visits=0, dfs_edges=0, kahn_push=0, kahn_pop=0, relax=0;
    private long time_nanos=0;
    @Override public void add_dfs_visit(){ dfs_visits++; }
    @Override public void add_dfs_edge(){ dfs_edges++; }
    @Override public void add_kahn_push(){ kahn_push++; }
    @Override public void add_kahn_pop(){ kahn_pop++; }
    @Override public void add_relaxation(){ relax++; }
    @Override public void add_time(long nanos){ time_nanos += nanos; }
    @Override public String summary(){
        return String.format("dfs_visits=%d, dfs_edges=%d, kahn_push=%d, kahn_pop=%d, relax=%d, time_ms=%.3f", 
            dfs_visits, dfs_edges, kahn_push, kahn_pop, relax, time_nanos/1e6);
    }
}
