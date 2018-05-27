package de.jilocasin.nearestneighbour.kdtree.generator;

import java.util.ArrayList;
import java.util.List;

import de.jilocasin.nearestneighbour.kdtree.KdPoint;
import de.jilocasin.nearestneighbour.kdtree.KdTree;

public abstract class RandomKdTreeGenerator<T extends Number & Comparable<T>> {

	public KdTree<T> generate(final int dimensionCount, final int pointCountt) {
		return new KdTree<>(dimensionCount, generatePoints(dimensionCount, pointCountt));
	}

	public List<KdPoint<T>> generatePoints(final int dimensionCount, final int pointCount) {
		final List<KdPoint<T>> points = new ArrayList<>(pointCount);

		for (int i = 0; i < pointCount; i++) {
			final List<T> position = new ArrayList<>(dimensionCount);

			for (int axisIndex = 0; axisIndex < dimensionCount; axisIndex++) {
				position.add(buildRandomValue());
			}

			points.add(new KdPoint<>(position));
		}

		return points;
	}

	public abstract T buildRandomValue();
}
