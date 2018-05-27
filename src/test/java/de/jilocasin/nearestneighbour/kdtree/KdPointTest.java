package de.jilocasin.nearestneighbour.kdtree;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class KdPointTest<T> {
	private static final double EPSILON = 0.00001;

	@Test
	public void testKdPointFromArray() {
		// Create a 5-dimensional point from an array of values.

		final KdPoint<Integer> point = new KdPoint<>(1, 2, 3, 4, 5);

		assertEquals(5, point.getDimensions());
		assertEquals("[1, 2, 3, 4, 5]", point.toString());
	}

	@Test
	public void testKdPointFromList() {
		// Create a 5-dimensional point from a list of values.

		final List<Integer> values = new ArrayList<>();

		values.add(1);
		values.add(2);
		values.add(3);
		values.add(4);
		values.add(5);

		final KdPoint<Integer> point = new KdPoint<>(values);

		assertEquals(5, point.getDimensions());
	}

	@Test
	public void testToString() {
		final KdPoint<Integer> point = new KdPoint<>(1, 2, 3, 4, 5);

		assertEquals("[1, 2, 3, 4, 5]", point.toString());
	}

	@Test
	public void testGetDistanceSquared() {
		testDistanceSquaredOfPoints(new KdPoint<>(0.0, 0.0), new KdPoint<>(2.0, 2.0), 8.0);
		testDistanceSquaredOfPoints(new KdPoint<>(2.0, 2.0), new KdPoint<>(4.0, 4.0), 8.0);
		testDistanceSquaredOfPoints(new KdPoint<>(0.0, 0.0), new KdPoint<>(4.0, 4.0), 32.0);

		testDistanceSquaredOfPoints(new KdPoint<>(4.0, -4.0), new KdPoint<>(-4.0, 4.0), 128.0);

		testDistanceSquaredOfPoints(new KdPoint<>(0.0, 0.0), new KdPoint<>(-4.0, -4.0), 32.0);
	}

	private void testDistanceSquaredOfPoints(
	        final KdPoint<Double> originPoint,
	        final KdPoint<Double> targetPoint,
	        final double expectedDistance) {
		assertEquals(expectedDistance, originPoint.getDistanceSquared(targetPoint), EPSILON);
	}
}
