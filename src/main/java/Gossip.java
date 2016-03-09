import org.graphstream.graph.Node;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Gossip extends Thread{

    private ReseauSocial reseauSocial;
    private Color gossipColor;
    private Node gossipNode;
    private static SecureRandom random;
    private static double propagationChance;
    private static double safeChance;
    private ArrayList<Node> nodeAlreadyCreatedAsAThreadList;

    public Gossip(ReseauSocial reseauSocial, Color gossipColor) {
        this.reseauSocial = reseauSocial;
        this.gossipColor = gossipColor;
        random = new SecureRandom();
        propagationChance = 0.5;
        safeChance = 0.2;
    }

    public void init(){
        byte seed[] = random.generateSeed(20);
        random.nextBytes(seed);
        //Get node randomly that will init gossip
        gossipNode = reseauSocial.getGraph().getNode(Math.abs(random.nextInt()%ReseauSocial.getNbNoeuds()));
        gossipNode.setAttribute("ui.color", Color.RED);
        gossipNode.setAttribute("ui.label", "source");
    }

    //New thread starting propagation
    public void run(){
        propagation();
    }

    //Propagation over nodes
    private void propagation(){
        //Init list of already existing nodeThreads
        nodeAlreadyCreatedAsAThreadList = new ArrayList<Node>();
        nodeAlreadyCreatedAsAThreadList.add(gossipNode);
        //Launch new Thread for each node
        NodeThread nodeThread = new NodeThread(gossipNode, this, NodeState.SOURCE);
        nodeThread.start();
    }

    public static SecureRandom getRandom() {
        return random;
    }

    public static double getPropagationChance() {
        return propagationChance;
    }

    public static double getSafeChance() {
        return safeChance;
    }

    public Color getGossipColor() {
        return gossipColor;
    }

    public ArrayList<Node> getNodeAlreadyCreatedAsAThreadList() {
        return nodeAlreadyCreatedAsAThreadList;
    }

    public ReseauSocial getReseauSocial() {
        return reseauSocial;
    }
}
