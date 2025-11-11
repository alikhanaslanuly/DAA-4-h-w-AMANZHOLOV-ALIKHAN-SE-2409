package graph;
import graph.scc.SccTarjan;
import graph.topo.TopologicalSorter;
import graph.dagsp.DagShortestPaths;
import util.io.JsonGraph;
import util.metrics.metrics;
import java.nio.file.Paths;
import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
        String data_file = "data/tasks.json";
        if (args.length>0) data_file = args[0];
        JsonGraph g = JsonGraph.readFromFile(Paths.get(data_file));
        System.out.println("Loaded graph: n="+g.n+" edges="+g.edges.size()+" directed="+g.directed);
        metrics m = new util.metrics.simple_metrics();
        SccTarjan scc = new SccTarjan(g, m);
        List<List<Integer>> comps = scc.run();
        System.out.println("SCCs (count="+comps.size()+"):");
        for (int i=0;i<comps.size();i++) System.out.println("  comp"+i+": "+comps.get(i));
        var cond = scc.buildCondensation(comps);
        System.out.println("Condensation: n="+cond.n+" edges="+cond.edges.size());
        TopologicalSorter topo = new TopologicalSorter(cond, m);
        List<Integer> order = topo.kahn();
        System.out.println("Condensation topo-order: "+order);
        List<Integer> derived = scc.derivedOrderFromCompOrder(order, comps);
        System.out.println("Derived order of original tasks after SCC compression: "+derived);
        DagShortestPaths dsp = new DagShortestPaths(cond, m);
        int source_comp = 0;
        var shortest = dsp.shortest_paths(source_comp);
        System.out.println("Shortest dist from comp "+source_comp+": "+Arrays.toString(shortest));
        var longest = dsp.longest_path();
        System.out.println("Longest path in condensation DAG (critical path): length="+longest.length+", path="+longest.path);
        System.out.println("Metrics: " + m.summary());
    }
}
