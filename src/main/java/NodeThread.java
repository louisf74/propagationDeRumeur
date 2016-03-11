import org.graphstream.graph.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class NodeThread extends Thread {

    private Node node;
    private Gossip gossip;
    private NodeState nodeState;
    private ArrayList<NodeThread> neighboursNodeThreadsList;
    private static int lifeTime;
    private static int propagationSpeed;

    public NodeThread(Node node, Gossip gossip, NodeState nodeState) {
        this.node = node;
        this.gossip = gossip;
        this.nodeState = nodeState;
        this.neighboursNodeThreadsList = new ArrayList<NodeThread>();
    }

    //Propagate gossip to other linked nodes
    public void run(){
        nodeThreadsCreation();
        propagationIterations();
    }

    private void nodeThreadsCreation() {
        //Loop over linked nodes
        Iterator<Node> nextNodes = node.getNeighborNodeIterator();
        while (nextNodes.hasNext()) {
            Node currentNextNode = nextNodes.next();
            createNewNodeThreadIfNotAlreadyCreated(currentNextNode);
        }
    }

    private void createNewNodeThreadIfNotAlreadyCreated(Node currentNextNode){
        if (!gossip.getNodeAlreadyCreatedAsAThreadList().contains(currentNextNode)){
            gossip.getNodeAlreadyCreatedAsAThreadList().add(currentNextNode);
            NodeThread currentNextNodeThread = new NodeThread(currentNextNode, gossip, NodeState.NOTHING);
            this.neighboursNodeThreadsList.add(currentNextNodeThread);
        }
    }

    private void propagationIterations() {
        for (int i=lifeTime; i > 0; i--) {
            propagation();
        }
    }

    private void propagation() {
        //Loop over neighboors
        for (NodeThread currentNextNodeThread : this.neighboursNodeThreadsList ){
            tryToPropagate(currentNextNodeThread);
            sleep(propagationSpeed);
        }
    }

    private void tryToPropagate(NodeThread currentNextNodeThread){
        if (this.getNodeState() == NodeState.PROPAGATE || this.getNodeState() == NodeState.SOURCE) {
            double probabilityAlterationByNodeSize = (this.getNode().getDegree() + Gossip.getPropagationChance()) / this.getNode().getDegree();
            TryToChangeNodeState(currentNextNodeThread, Gossip.getPropagationChance() * probabilityAlterationByNodeSize, NodeState.PROPAGATE, gossip.getGossipColor());
            TryToChangeNodeState(currentNextNodeThread, Gossip.getSafeChance() / probabilityAlterationByNodeSize, NodeState.SAFE, new Color(124,255,0));
        }
    }

    private void TryToChangeNodeState(NodeThread currentNextNodeThread, double probability, NodeState nodeState, Color color) {
        if (currentNextNodeThread.getNodeState() != NodeState.PROPAGATE && currentNextNodeThread.getNodeState() != NodeState.SAFE) {
            double randInt = Math.random();
            //If safe chance is obtained
            if (randInt < probability) {
                if ( currentNextNodeThread.getNode().getAttribute("ui.color") != null ) {
                    int cpt = 0;
                    for (Color c : Gossip.getColors()) {
                        if (c.equals(currentNextNodeThread.getNode().getAttribute("ui.color"))){
                            Gossip.getNbsConverted().set(cpt, Gossip.getNbsConverted().get(cpt)-1);
                        }
                        cpt++;
                    }
                }
                currentNextNodeThread.setNodeState(nodeState);
                currentNextNodeThread.getNode().setAttribute("ui.color", color);
                Gossip.getNbsConverted().set(gossip.getGossipId(), Gossip.getNbsConverted().get(gossip.getGossipId())+1.0);
                currentNextNodeThread.start();
            }
        }
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Node getNode() {
        return node;
    }

    public void setNodeState(NodeState nodeState) {
        this.nodeState = nodeState;
    }

    public NodeState getNodeState() {
        return nodeState;
    }

    public static void setLifeTime(int lifeTime) {
        NodeThread.lifeTime = lifeTime;
    }

    public static void setPropagationSpeed(int propagationSpeed) {
        NodeThread.propagationSpeed = propagationSpeed;
    }
}