import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.randomWalk.RandomWalk;
import org.graphstream.graph.implementations.MultiGraph;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.tabbedui.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String args[]) throws InterruptedException {
        int nbGossip = 0;
        Color[] colors = {Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN};
        Gossip.setColors(colors);
        String[] colorsStrings = {"Blue", "Yellow", "magenta", "cyan"};
        try {
            System.out.println("Veuillez saisir le nombre de rumeurs à lancer (max 4)");
            do {
                nbGossip = Integer.parseInt(bufferedReader.readLine());
            }while(nbGossip > 4);
            System.out.println("Veuillez saisir la chance de propagation de la rumeur (ex: 0.5) :");
            Gossip.setPropagationChance(Double.parseDouble(bufferedReader.readLine()));
            System.out.println("Veuillez saisir la chance de devenir insensible à la rumeur (ex: 0.1) :");
            Gossip.setSafeChance(Double.parseDouble(bufferedReader.readLine()));
            System.out.println("Veuillez saisir la durée de vie d'un noeud (lorsqu'un noeud propage une rumeur, combien de temps la propage-t-il?)");
            Gossip.setLifeTime(Integer.parseInt(bufferedReader.readLine()));
            System.out.println("Veuillez saisir le temps de propagation de la rumeur (temps d'attente en ms de chaque itération)");
            Gossip.setPropagationSpeed(Integer.parseInt(bufferedReader.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Results");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new VerticalLayout());
        ArrayList<Label> labels = new ArrayList<Label>();
        ArrayList<Double> nbs = new ArrayList<Double>();
        for (Color c : colors){
            labels.add(new Label());
            nbs.add(0.0);
        }
        int i=0;
        for (Label l : labels){
            frame.add(new Label("Gossip " + colorsStrings[i] + ": "));
            frame.add(l);
            i++;
        }
        Gossip.setNbsConverted(nbs);

        frame.pack();
        frame.setSize(500,200);
        frame.setVisible(true);

        ReseauSocial reseauSocial = new ReseauSocial(new MultiGraph("random walk"), new DorogovtsevMendesGenerator(), new RandomWalk());
        reseauSocial.init();
        for (int cpt=0; cpt < nbGossip; cpt++){
            Gossip g = new Gossip(reseauSocial, colors[cpt%colors.length], cpt);
            g.init();
            g.start();
        }

        while (true){
            int j = 0;
            for (Label l : labels){
                l.setText((Gossip.getNbsConverted().get(j)/(double)reseauSocial.getGraph().getNodeCount())*100.0+"%");
                j++;
            }
        }
    }

}


