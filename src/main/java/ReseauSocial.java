import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.randomWalk.RandomWalk;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.MultiGraph;

public class ReseauSocial {

    private MultiGraph graph;
    private Generator gen;
    private RandomWalk rwalk;
    private static int nbNoeuds = 50;
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

    public ReseauSocial(MultiGraph graph, Generator gen, RandomWalk rwalk) {
        this.graph = graph;
        this.gen = gen;
        this.rwalk = rwalk;
    }

    public void init() {
        // On génère n noeuds
        gen.addSink(graph);
        gen.begin();
        for(int i=0; i<nbNoeuds; i++) {
            gen.nextEvents();
        }
        gen.end();
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.display();

        //random edges using rwalk object
        rwalk.setEntityCount(graph.getNodeCount()*2);
        rwalk.setEvaporation(0.97);
        rwalk.setEntityMemory(40);
        rwalk.init(graph);
        rwalk.compute();
        rwalk.terminate();

        // Update colors
        updateGraph(graph, rwalk);

        // We take a small screen-shot of the result.
        graph.addAttribute("ui.screenshot", "randomWalk.png");
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
}