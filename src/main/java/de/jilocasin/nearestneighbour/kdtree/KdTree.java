package de.jilocasin.nearestneighbour.kdtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KdTree<T extends Number & Comparable<T>> {
	/**
	 * Use 1 % of original points to choose a median approximation.
	 */
	private static final float APPROXIMATION_POINTS_PERCENTAGE = 0.01f;

	/**
	 * Random instance used for the median approximation.
	 */
	private static final Random random = new Random(0);

	public final int dimensionCount;

	public final KdNode<T> rootNode;

	public KdTree(final int dimensionCount, final List<KdPoint<T>> points) {
		this.dimensionCount = dimensionCount;

		rootNode = buildNode(null, points, 0);
	}

	/**
	 * Returns the axis index for the provided depth, based on the dimension count
	 * of this tree.
	 * 
	 * @param depth
	 *            the requested tree node depth.
	 */
	public int getAxisIndex(final int depth) {
		return depth % dimensionCount;
	}

	private KdNode<T> buildNode(final KdNode<T> parentNode, final List<KdPoint<T>> points, final int depth) {
		if (points.isEmpty()) {
			return null;
		}

		final int axisIndex = getAxisIndex(depth);

		final KdPoint<T> medianPoint = getFastApproximatedMedianPoint(points, axisIndex);

		// Create node and construct subtrees.

		final KdNode<T> newNode = new KdNode<>(medianPoint, depth, axisIndex);

		// Assume both sides have approximately half the number of points.

		final List<KdPoint<T>> leftOfMedian = new ArrayList<>(points.size() / 2);
		final List<KdPoint<T>> rightOfMedian = new ArrayList<>(points.size() / 2);

		for (final KdPoint<T> point : points) {
			if (point == medianPoint) {
				continue;
			}

			if (point.values.get(axisIndex).compareTo(medianPoint.values.get(axisIndex)) > 0) {
				rightOfMedian.add(point);
			} else {
				leftOfMedian.add(point);
			}
		}

		final KdNode<T> leftNode = buildNode(newNode, leftOfMedian, depth + 1);
		final KdNode<T> rightNode = buildNode(newNode, rightOfMedian, depth + 1);

		newNode.setLeftNode(leftNode);
		newNode.setRightNode(rightNode);
		newNode.setParentNode(parentNode);

		return newNode;
	}

	/**
	 * Uses a small subset of the points to choose a median point approximating the
	 * median of all provided points.
	 */
	private KdPoint<T> getFastApproximatedMedianPoint(final List<KdPoint<T>> points, final int axisIndex) {
		final int numberOfElements = (int) Math.max(points.size() * APPROXIMATION_POINTS_PERCENTAGE, 1);

		final List<KdPoint<T>> subset = pickRandomSubset(points, numberOfElements);

		sortByAxisIndex(subset, axisIndex);

		return subset.get(subset.size() / 2);
	}

	private final void sortByAxisIndex(final List<KdPoint<T>> points, final int axisIndex) {
		points.sort((point1, point2) -> {
			return point1.values.get(axisIndex).compareTo(point2.values.get(axisIndex));
		});
	};

	/**
	 * <p>
	 * Picks a random subset of points from the given list.
	 * </p>
	 * 
	 * <b>Important:</b>
	 * 
	 * <p>
	 * This method uses a simple select-one-of-N approach to choose each random
	 * point. This may result in points being included multiple times in the
	 * returned list.
	 * </p>
	 */
	private List<KdPoint<T>> pickRandomSubset(final List<KdPoint<T>> points, final int numberOfElements) {
		final List<KdPoint<T>> subset = new ArrayList<>(numberOfElements);

		for (int i = 0; i < numberOfElements; i++) {
			final int randomIndex = random.nextInt(numberOfElements);

			subset.add(points.get(randomIndex));
		}

		return subset;
	}
}
