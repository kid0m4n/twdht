package com.thoughtworks.dht.contentaddressable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* Understands the key value pairs in its region */
public class Node {
    private final Point bottomLeft;
    private final Point topRight;

    private final List<Node> neighbours;

    public Node(Point bottomLeft, Point topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;

        neighbours = new ArrayList<Node>();
    }

    public boolean isNeighboursWith(Node node) {
        return neighbours.contains(node);
    }

    private boolean sharesHorizontalEdgeWith(Node other) {
        return topRight.verticallyAlignedTo(other.bottomLeft) &&
               topRight.aheadOfHorizontally(other.bottomLeft) &&
               other.topRight.aheadOfHorizontally(bottomLeft);
    }

    private boolean sharesVerticalEdgeWith(Node other) {
        return topRight.horizontallyAlignedTo(other.bottomLeft) &&
               topRight.aheadOfVertically(other.bottomLeft) &&
               other.topRight.aheadOfVertically(bottomLeft);
    }

    protected boolean isAdjacentTo(Node other) {
        return sharesHorizontalEdgeWith(other) || other.sharesHorizontalEdgeWith(this) ||
               sharesVerticalEdgeWith(other) || other.sharesVerticalEdgeWith(this);
    }

    public List<Node> splitHorizontal() {
        Node node1 = new Node(bottomLeft, new Point(topRight.x(), (topRight.y() + bottomLeft.y()) / 2));
        Node node2 = new Node(new Point(bottomLeft.x(), (topRight.y() + bottomLeft.y()) / 2), topRight);

        performUpdates(node1, node2);

        return Arrays.asList(node1, node2);
    }

    public List<Node> splitVertical() {
        Node node1 = new Node(bottomLeft, new Point((topRight.x() + bottomLeft.x()) / 2, topRight.y()));
        Node node2 = new Node(new Point((topRight.x() + bottomLeft.x()) / 2, bottomLeft.y()), topRight);

        performUpdates(node1, node2);

        return Arrays.asList(node1, node2);
    }

    private void performUpdates(Node node1, Node node2) {
        node1.inheritNeighboursFrom(this);
        node2.inheritNeighboursFrom(this);
        node1.neighbours.add(node2);
        node2.neighbours.add(node1);
        updateNeighbours(Arrays.asList(node1, node2));
    }

    private void updateWithNewBorns(Node oldNode, List<Node> newBornNodes) {
        neighbours.remove(oldNode);
        for (Node node : newBornNodes)
            if (isAdjacentTo(node))
                neighbours.add(node);
    }

    private void updateNeighbours(List<Node> newBornNodes) {
        for (Node node : neighbours)
            node.updateWithNewBorns(this, newBornNodes);
    }

    private void inheritNeighboursFrom(Node parentNode) {
        for (Node neighbour : parentNode.neighbours)
            if (isAdjacentTo(neighbour))
                neighbours.add(neighbour);
    }
}
