package graph;

import graph.scc.SccTarjan;
import graph.dagsp.DagShortestPaths;
import org.junit.jupiter.api.Test;
import util.io.JsonGraph;
import util.metrics.simple_metrics;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class DagSpTests {
    @Test public void testDagShortest() throws Exception {
        JsonGraph g = JsonGraph.readFromFile(Paths.get("data/test_dag.json"));
        var m = new simple_metrics();
        SccTarjan s = new SccTarjan(g, m);
        var comps = s.run();
        var cond = s.buildCondensation(comps);
        DagShortestPaths dsp = new DagShortestPaths(cond, m);
        long[] dist = dsp.shortest_paths(0);
        assertEquals(0, dist[0]);
        assertTrue(dist[2] > 0);
    }
}
