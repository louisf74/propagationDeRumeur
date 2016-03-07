import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.randomWalk.RandomWalk;
import org.graphstream.graph.implementations.MultiGraph;

public class Main {

    public static void main(String args[]) {
        ReseauSocial reseauSocial = new ReseauSocial(new MultiGraph("random walk"), new DorogovtsevMendesGenerator(), new RandomWalk());
        reseauSocial.init();
    }


}

