package util.metrics;
public interface metrics {
    void add_dfs_visit();
    void add_dfs_edge();
    void add_kahn_push();
    void add_kahn_pop();
    void add_relaxation();
    void add_time(long nanos);
    String summary();
}
