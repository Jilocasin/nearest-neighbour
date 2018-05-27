package de.jilocasin.nearestneighbour.kdtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.jilocasin.nearestneighbour.kdtree.exception.InvalidKdPointDimensionsException;
import de.jilocasin.nearestneighbour.kdtree.exception.InvalidKdPointCountException;
import de.jilocasin.nearestneighbour.kdtree.exception.KdTreeException;

public class KdTree<T extends Number & Comparable<T>> {
	/**
	 * Use 1% of all relevant points to choose a median approximation.
	 */
	private static final float MEDIAN_APPROXIMATION_POINTS_PERCENTAGE = 0.01f;

	/**
	 * Random instance used for the median approximation.
	 */
	private static final Random random = new Random(0);

	public final int dimensionCount;

	public final KdNode<T> rootNode;

	/**
	 * Creates a new KdTree instance based on the provided number of dimensions and
	 * data points.
	 * 
	 * @param dimensionCount
	 *            the number of dimensions of the new tree.
	 * @param points
	 *            the points to include in the tree data.
	 * @throws InvalidKdPointCountException
	 *             if the provided list of points was null or did not contain at
	 *             least one point.
	 * @throws InvalidKdPointDimensionsException
	 *             if the quick point axis value check fails. The check is only
	 *             performed for the first point in the provided list and tests
	 *             whether the number of axis values for this point matches the
	 *             provided dimension count of the tree.
	 */
	public KdTree(final int dimensionCount, final List<KdPoint<T>> points) throws KdTreeException {
		// Make sure at least one point was provided.

		if (points == null || points.isEmpty()) {
			throw new InvalidKdPointCountException();
		}

		// Perform a quick dimension count check on the very first point.
		// All other points are assumed to contain the same number of values for
		// performance reason.

		final KdPoint<T> testPoint = points.get(0);

		if (testPoint.getDimensions() != dimensionCount) {
			throw new InvalidKdPointDimensionsException();
		}

		this.dimensionCount = dimensionCount;

		this.rootNode = buildNode(null, points, 0);
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

			if (point.getAxisValue(axisIndex).compareTo(medianPoint.getAxisValue(axisIndex)) > 0) {
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
		final int numberOfElements = (int) Math.max(points.size() * MEDIAN_APPROXIMATION_POINTS_PERCENTAGE, 1);

		final List<KdPoint<T>> subset = pickRandomSubset(points, numberOfElements);

		sortByAxisIndex(subset, axisIndex);

		return subset.get(subset.size() / 2);
	}

	private final void sortByAxisIndex(final List<KdPoint<T>> points, final int axisIndex) {
		points.sort((point1, point2) -> {
			return point1.getAxisValue(axisIndex).compareTo(point2.getAxisValue(axisIndex));
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
