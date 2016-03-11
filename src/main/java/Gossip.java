import org.graphstream.graph.Node;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Gossip extends Thread{

    private int id;
    private ReseauSocial reseauSocial;
    private Color gossipColor;
    private Node gossipNode;
    private ArrayList<Node> nodeAlreadyCreatedAsAThreadList;
    private int nbNodesConverted;
    private static SecureRandom random;
    private static double propagationChance;
    private static double safeChance;
    private static int lifeTime;
    private static int propagationSpeed;
    private static ArrayList<Double> nbsConverted;
    private static Color[] colors;

    public Gossip(ReseauSocial reseauSocial, Color gossipColor, int id) {
        this.id = id;
        this.reseauSocial = reseauSocial;
        this.gossipColor = gossipColor;
        random = new SecureRandom();
        NodeThread.setLifeTime(lifeTime);
        NodeThread.setPropagationSpeed(propagationSpeed);
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

    public int getNbNodesConverted() {
        return nbNodesConverted;
    }

    public void incrementNbNodeConverted() {
        this.nbNodesConverted++;
    }

    public static void setLifeTime(int lifeTime) {
        Gossip.lifeTime = lifeTime;
    }

    public static void setPropagationSpeed(int propagationSpeed) {
        Gossip.propagationSpeed = propagationSpeed;
    }

    public static void setPropagationChance(double propagationChance) {
        Gossip.propagationChance = propagationChance;
    }

    public static void setSafeChance(double safeChance) {
        Gossip.safeChance = safeChance;
    }

    public int getGossipId() {
        return id;
    }

    public static ArrayList<Double> getNbsConverted() {
        return nbsConverted;
    }

    public static void setNbsConverted(ArrayList<Double> nbsConverted) {
        Gossip.nbsConverted = nbsConverted;
    }

    public static Color[] getColors() {
        return colors;
    }

    public static void setColors(Color[] colors) {
        Gossip.colors = colors;
    }
}
