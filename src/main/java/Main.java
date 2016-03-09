import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.randomWalk.RandomWalk;
import org.graphstream.graph.implementations.MultiGraph;

import java.awt.*;

public class Main {

    public static void main(String args[]) throws InterruptedException {
        ReseauSocial reseauSocial = new ReseauSocial(new MultiGraph("random walk"), new DorogovtsevMendesGenerator(), new RandomWalk());
        reseauSocial.init();
        Gossip gossip = new Gossip(reseauSocial, Color.BLUE);
        gossip.init();
        gossip.start();
        Gossip gossip2 = new Gossip(reseauSocial, Color.MAGENTA);
        gossip2.init();
        gossip2.start();
        Gossip gossip3 = new Gossip(reseauSocial, Color.YELLOW);
        gossip3.init();
        gossip3.start();

    }

}


