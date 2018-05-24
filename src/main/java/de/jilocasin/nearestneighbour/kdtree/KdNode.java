package de.jilocasin.nearestneighbour.kdtree;

public class KdNode<T extends Number & Comparable<T>> {
	public final KdPoint<T> point;

	public final int depth;
	public final int axisIndex;

	private KdNode<T> parentNode;
	private KdNode<T> leftNode;
	private KdNode<T> rightNode;

	public KdNode(final KdPoint<T> point, final int depth, final int axisIndex) {
		this.point = point;
		this.depth = depth;
		this.axisIndex = axisIndex;
	}

	public KdNode<T> getParentNode() {
		return parentNode;
	}

	public void setParentNode(final KdNode<T> parentNode) {
		this.parentNode = parentNode;
	}

	public KdNode<T> getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(final KdNode<T> leftNode) {
		this.leftNode = leftNode;
	}

	public KdNode<T> getRightNode() {
		return rightNode;
	}

	public void setRightNode(final KdNode<T> rightNode) {
		this.rightNode = rightNode;
	}

	public boolean hasParentNode() {
		return parentNode != null;
	}

	public boolean hasLeftNode() {
		return leftNode != null;
	}

	public boolean hasRightNode() {
		return rightNode != null;
	}

	public boolean hasChildren() {
		return leftNode != null && rightNode != null;
	}

	public int numberOfChildren() {
		return (leftNode != null ? 1 : 0) + (rightNode != null ? 1 : 0);
	}
}
