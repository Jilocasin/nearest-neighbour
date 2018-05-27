package de.jilocasin.nearestneighbour.nnsolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.jilocasin.nearestneighbour.kdtree.KdPoint;
import de.jilocasin.nearestneighbour.kdtree.KdTree;
import de.jilocasin.nearestneighbour.kdtree.generator.RandomDoubleKdTreeGenerator;

public class NNSolverTest {
	private static final int POINT_COUNT = 100_000;

	private static final int MIN_DIMENSIONS = 3;
	private static final int MAX_DIMENSIONS = 3;

	private static final double EPSILON = 0.00001;

	private List<KdPoint<Double>> inputPoints;
	private KdTree<Double> tree;
	private NNSolver<Double> solver;

	@Test
	public void testFindNearestPointForAllDimensions() {
		for (int dimensionCount = MIN_DIMENSIONS; dimensionCount <= MAX_DIMENSIONS; dimensionCount++) {
			testFindNearestPointForDimensionCount(dimensionCount);
		}
	}

	@Test
	public void testFindNearestPointForExplicitData() {
		// Since the other test simply used random points to check some of the promises,
		// we need to use explicit data to make sure the returned nearest point is
		// actually correct.

		inputPoints = new ArrayList<>();

		inputPoints.add(new KdPoint<>(0.0, 0.0));
		inputPoints.add(new KdPoint<>(5.0, 5.0));
		inputPoints.add(new KdPoint<>(8.0, 5.0));
		inputPoints.add(new KdPoint<>(-30.0, -30.0));
		inputPoints.add(new KdPoint<>(-40.0, -40.0));
		inputPoints.add(new KdPoint<>(0.01, 0.01));

		tree = new KdTree<>(inputPoints);
		solver = new NNSolver<>(tree);

		final List<KdPoint<Double>> nearestToIndex = new ArrayList<>();

		// Find the nearest point for all input points and store them in an array.

		for (int i = 0; i < inputPoints.size(); i++) {
			nearestToIndex.add(solver.findNearestPoint(inputPoints.get(i)));
		}

		// Perform various neighbour checks on the array.
		// Keep in mind the notation is assertEquals(expected, actual).

		assertEquals(inputPoints.get(5), nearestToIndex.get(0));
		assertEquals(inputPoints.get(2), nearestToIndex.get(1));
		assertEquals(inputPoints.get(1), nearestToIndex.get(2));
		assertEquals(inputPoints.get(4), nearestToIndex.get(3));
		assertEquals(inputPoints.get(3), nearestToIndex.get(4));
		assertEquals(inputPoints.get(0), nearestToIndex.get(5));
	}

	public void testFindNearestPointForDimensionCount(final int dimensionCount) {
		final RandomDoubleKdTreeGenerator treeGenerator = new RandomDoubleKdTreeGenerator();

		inputPoints = treeGenerator.generatePoints(dimensionCount, POINT_COUNT);
		tree = new KdTree<>(inputPoints);
		solver = new NNSolver<>(tree);

		for (final KdPoint<Double> inputPoint : inputPoints) {
			// Build a list of axis values for the requested point to check.

			final List<Double> axisValues = new ArrayList<>(dimensionCount);

			for (int dimensionIndex = 0; dimensionIndex < dimensionCount; dimensionIndex++) {
				axisValues.add(inputPoint.getAxisValue(dimensionIndex));
			}

			// Assert the returned point from the NNSolver is located at our axis values.

			assertNearestPointIsPointAt(axisValues);

			// Assert that providing the point itself, it will NOT return the exact same
			// point instance.

			assertNearestPointIsNotSelf(inputPoint);
		}
	}

	private void assertNearestPointIsPointAt(final List<Double> axisValues) {
		// By creating a new KdPoint instance for the request, we should get the
		// input point instance at this exact location (assuming we added a point there
		// during tree setup of course).

		final KdPoint<Double> nearest = solver.findNearestPoint(new KdPoint<>(axisValues));

		// Check that the returned axis values of the nearest point equal to the input
		// axis values.

		for (int dimensionIndex = 0; dimensionIndex < axisValues.size(); dimensionIndex++) {
			assertEquals(nearest.getAxisValue(dimensionIndex), axisValues.get(dimensionIndex), EPSILON);
		}
	}

	private void assertNearestPointIsNotSelf(final KdPoint<Double> point) {
		final KdPoint<Double> nearest = solver.findNearestPoint(point);

		assertNotEquals(point, nearest);
	}
}
