import org.graphstream.graph.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class NodeThread extends Thread {

    private Node node;
    private Gossip gossip;
    private NodeState nodeState;

    public NodeThread(Node node, Gossip gossip, NodeState nodeState) {
        this.node = node;
        this.gossip = gossip;
        this.nodeState = nodeState;
    }

    //Propagate gossip to other linked nodes
    public void run(){
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
            tryToPropagate(currentNextNodeThread);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentNextNodeThread.start();
        }
    }

    private void tryToPropagate(NodeThread currentNextNodeThread){
        if (currentNextNodeThread.getNodeState() != NodeState.PROPAGATE && currentNextNodeThread.getNodeState() != NodeState.SAFE) {
            TryToChangeNodeState(currentNextNodeThread, Gossip.getPropagationChance(), NodeState.PROPAGATE, gossip.getGossipColor());
            TryToChangeNodeState(currentNextNodeThread, Gossip.getSafeChance(), NodeState.SAFE, Color.GRAY);
        }
    }

    private void TryToChangeNodeState(NodeThread currentNextNodeThread, double probability, NodeState nodeState, Color color) {
        int randInt;
        randInt = Math.abs(Gossip.getRandom().nextInt());
        //If safe chance is obtained
        if ( randInt < Integer.MAX_VALUE*probability ){
            currentNextNodeThread.setNodeState(nodeState);
            currentNextNodeThread.getNode().setAttribute("ui.color", color);
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