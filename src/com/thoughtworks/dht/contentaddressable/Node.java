package com.thoughtworks.dht.contentaddressable;

/* Understands the key value pairs in its region */
public class Node {
    private final Point bottomLeft;
    private final Point topRight;

    public Node(Point bottomLeft, Point topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
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

    public boolean isAdjacentTo(Node other) {
        return sharesHorizontalEdgeWith(other) || other.sharesHorizontalEdgeWith(this) ||
                sharesVerticalEdgeWith(other) || other.sharesVerticalEdgeWith(this);
    }

    public Node[] splitHorizontal() {
        Node node1 = new Node(bottomLeft, new Point(topRight.x(), (topRight.y() + bottomLeft.y()) / 2));
        Node node2 = new Node(new Point(bottomLeft.x(), (topRight.y() + bottomLeft.y()) / 2), topRight);

        return new Node[]{node1, node2};
    }

    public Node[] splitVertical() {
        Node node1 = new Node(bottomLeft, new Point((topRight.x() + bottomLeft.x()) / 2, topRight.y()));
        Node node2 = new Node(new Point((topRight.x() + bottomLeft.x()) / 2, bottomLeft.y()), topRight);

        return new Node[]{node1, node2};
    }
}