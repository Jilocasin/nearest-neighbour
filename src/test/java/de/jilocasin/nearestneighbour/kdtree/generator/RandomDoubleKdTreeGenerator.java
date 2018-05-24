package de.jilocasin.nearestneighbour.kdtree.generator;

import java.util.Random;

public class RandomDoubleKdTreeGenerator extends RandomKdTreeGenerator<Double> {
	/**
	 * The minimum for point values on any axis.
	 */
	private static final double AXIS_POSITION_MIN = 100_000;

	/**
	 * The maximum for random point values on any axis.
	 */
	private static final double AXIS_POSITION_MAX = 1_000_000;

	private final Random random = new Random(0);

	@Override
	public Double buildRandomValue() {
		return stretch(random.nextDouble(), AXIS_POSITION_MIN, AXIS_POSITION_MAX);
	}

	private double stretch(final double uniformDouble, final double min, final double max) {
		return min + (uniformDouble * (max - min));
	}
}
