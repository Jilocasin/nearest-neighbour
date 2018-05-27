package de.jilocasin.nearestneighbour.nnsolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.junit.Test;

import de.jilocasin.nearestneighbour.kdtree.KdPoint;
import de.jilocasin.nearestneighbour.kdtree.KdTree;
import de.jilocasin.nearestneighbour.kdtree.generator.RandomDoubleKdTreeGenerator;

public class NNSolverWorkerTest {
	private static final int POINT_COUNT = 100_000;

	@Test
	public void testGetResultPoints() throws InterruptedException {
		final int dimensionCount = 3;

		final RandomDoubleKdTreeGenerator treeGenerator = new RandomDoubleKdTreeGenerator();

		final List<KdPoint<Double>> inputPoints = treeGenerator.generatePoints(dimensionCount, POINT_COUNT);
		final KdTree<Double> tree = new KdTree<>(inputPoints);

		final NNSolverWorker<Double> worker = new NNSolverWorker<>(tree, inputPoints);

		// Kick off the worker and request the result.

		worker.start();

		final List<KdPoint<Double>> resultPoints = worker.getResultPoints();

		assertEquals(inputPoints.size(), resultPoints.size());

		for (int i = 0; i < resultPoints.size(); i++) {
			// Make sure each result point is different from its input point at that index.

			assertNotEquals(inputPoints.get(i), resultPoints.get(i));
		}
	}

}
