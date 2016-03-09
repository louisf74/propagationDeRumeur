import org.graphstream.graph.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class NodeThread extends Thread {

    private Node node;
    private Gossip gossip;
    private NodeState nodeState;
    private ArrayList<NodeThread> neighboursNodeThreadsList;

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
            currentNextNodeThread.start();
        }
    }

    private void propagationIterations() {
        int lifeTime = 100;
        for (int i=lifeTime; i > 0; i--) {
            propagation();
        }
    }

    private void propagation() {
        //Loop over neighboors
        for (NodeThread currentNextNodeThread : this.neighboursNodeThreadsList ){
            tryToPropagate(currentNextNodeThread);
            sleep(1000);
        }
    }

    private void tryToPropagate(NodeThread currentNextNodeThread){
        if (this.getNodeState() == NodeState.PROPAGATE || this.getNodeState() == NodeState.SOURCE) {
            double probabilityAlterationByNodeSize = (this.getNode().getDegree() + Gossip.getPropagationChance()) / this.getNode().getDegree();
            TryToChangeNodeState(currentNextNodeThread, Gossip.getPropagationChance() * probabilityAlterationByNodeSize, NodeState.PROPAGATE, gossip.getGossipColor());
            TryToChangeNodeState(currentNextNodeThread, Gossip.getSafeChance(), NodeState.SAFE, Color.GREEN);
        }
    }

    private void TryToChangeNodeState(NodeThread currentNextNodeThread, double probability, NodeState nodeState, Color color) {
        if (currentNextNodeThread.getNodeState() != NodeState.PROPAGATE && currentNextNodeThread.getNodeState() != NodeState.SAFE) {
            int randInt;
            randInt = Math.abs(Gossip.getRandom().nextInt());
            System.out.println(randInt +" / " + probability*Integer.MAX_VALUE);
            //If safe chance is obtained
            if (randInt < Integer.MAX_VALUE * probability) {
                currentNextNodeThread.setNodeState(nodeState);
                currentNextNodeThread.getNode().setAttribute("ui.color", color);
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
}