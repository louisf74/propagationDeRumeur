/**
 * Created by flori on 04/03/2016.
 */
import org.graphstream.algorithm.TarjanStronglyConnectedComponents;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;


public class Graph {

    public static void main(String args[]) {
        MultiGraph g = new MultiGraph("g");

        String nodes = "abcdefgh";
        String edges = "abbccddccgdhhdhggffgbfefbeea";

        for (int i = 0; i < 8; i++)
            g.addNode(nodes.substring(i, i + 1));

        for (int i = 0; i < 14; i++) {
            g.addEdge(edges.substring(2 * i, 2 * i + 2),
                    edges.substring(2 * i, 2 * i + 1),
                    edges.substring(2 * i + 1, 2 * i + 2), true);
        }

        g.display();

        TarjanStronglyConnectedComponents tscc = new TarjanStronglyConnectedComponents();
        tscc.init(g);
        tscc.compute();

        for (Node n : g.getEachNode())
            n.addAttribute("label", n.getAttribute(tscc.getSCCIndexAttribute()));
    }

}


