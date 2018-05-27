package de.jilocasin.nearestneighbour.kdtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a single point in a k dimensional space.
 */
public class KdPoint<T extends Number & Comparable<T>> {
	private final List<T> axisValues;

	@SafeVarargs
	public KdPoint(final T... axisValues) {
		this.axisValues = new ArrayList<>();

		for (final T value : axisValues) {
			this.axisValues.add(value);
		}
	}

	public KdPoint(final List<T> axisValues) {
		this.axisValues = axisValues;
	}

	/**
	 * Creates a deep copy of the provided point. The new point will have the exact
	 * same axis values but be a new point instance.
	 */
	public KdPoint(final KdPoint<T> sourcePoint) {
		this.axisValues = new ArrayList<>();

		this.axisValues.addAll(sourcePoint.axisValues);
	}

	/**
	 * Returns the value on the axis with the provided index.
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (index < 0 || index >= size())
	 */
	public T getAxisValue(final int axisIndex) {
		return axisValues.get(axisIndex);
	}

	public int getDimensions() {
		return axisValues.size();
	}

	@Override
	public String toString() {
		return axisValues.toString();
	}

	public double getDistanceSquared(final KdPoint<T> other) {
		// Calculate the squared distance to the other point via euclidean distance.

		final int dimensions = axisValues.size();
		double distance = 0;

		for (int i = 0; i < dimensions; i++) {
			final double delta = axisValues.get(i).doubleValue() - other.axisValues.get(i).doubleValue();

			distance += (delta * delta);
		}

		return distance;

	}
}
