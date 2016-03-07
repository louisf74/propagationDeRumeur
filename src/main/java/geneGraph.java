import com.sun.corba.se.impl.orbutil.graph.Graph;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.randomWalk.RandomWalk;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.MultiGraph;

public class GeneGraph{
    public static void testRandomWalk() {
        MultiGraph graph = new MultiGraph("random walk");
        Generator gen   = new DorogovtsevMendesGenerator();
        RandomWalk rwalk = new RandomWalk();

        // On génère 400 noeuds
        gen.addSink(graph);
        gen.begin();
        for(int i=0; i<50; i++) {
            gen.nextEvents();
        }
        gen.end();
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.display();

        // We configure the random walk to use twice as
        // much entities as nodes in the graph. To use
        // a small evaporation on the number of passes
        // per element and a last visited edge list of
        // 40 elements.
        rwalk.setEntityCount(graph.getNodeCount()*2);
        rwalk.setEvaporation(0.97);
        rwalk.setEntityMemory(40);
        rwalk.init(graph);

        // Compute the walks for 3000 steps only as an
        // example, but the test could run forever with
        // a dynamic graph if needed.
        for(int i=0; i<3000; i++) {
            rwalk.compute();
        }
        rwalk.terminate();

        // Only when finished we change the edges colors
        // according to the number of passes. This call could
        // be made inside the loop above to show the evolution
        // of the entities passes.
        updateGraph(graph, rwalk);

        // We take a small screen-shot of the result.
        graph.addAttribute("ui.screenshot", "randomWalk.png");
        //graph.display(); //test
    }


    /**
     * Update the edges with colors corresponding to entities passes.
     */
    public static void updateGraph(MultiGraph graph, RandomWalk rwalk) {
        double mine = Double.MAX_VALUE;
        double maxe = Double.MIN_VALUE;

        // Obtain the maximum and minimum passes values.
        for(Edge edge: graph.getEachEdge()) {
            double passes = rwalk.getPasses(edge);
            if(passes>maxe) maxe = passes;
            if(passes<mine) mine = passes;
        }

        // Set the colors.
        for(Edge edge:graph.getEachEdge()) {
            double passes = rwalk.getPasses(edge);
            double color  = ((passes-mine)/(maxe-mine));
            edge.setAttribute("ui.color", color);
        }
    }

    protected static String styleSheet =
            "edge {"+
                    "	size: 2px;"+
                    "	fill-color: red, yellow, green, #444;"+
                    "	fill-mode: dyn-plain;"+
                    "}"+
                    "node {"+
                    "	size: 6px;"+
                    "	fill-color: #444;"+
                    "}";
}