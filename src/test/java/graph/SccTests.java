package graph;
import graph.scc.SccTarjan;
import util.io.JsonGraph;
import util.metrics.simple_metrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;
import java.util.List;

public class SccTests {
    @Test public void testSimpleCycle() throws Exception {
        JsonGraph g = JsonGraph.readFromFile(Paths.get("data/test_cycle.json"));
        var m = new simple_metrics();
        SccTarjan s = new SccTarjan(g, m);
        List<List<Integer>> comps = s.run();
        assertEquals(1, comps.size());
        assertEquals(3, comps.get(0).size());
    }
}
