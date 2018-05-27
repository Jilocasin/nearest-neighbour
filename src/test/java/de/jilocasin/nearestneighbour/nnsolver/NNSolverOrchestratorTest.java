package de.jilocasin.nearestneighbour.nnsolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.jilocasin.nearestneighbour.kdtree.KdPoint;
import de.jilocasin.nearestneighbour.kdtree.KdTree;
import de.jilocasin.nearestneighbour.kdtree.generator.RandomDoubleKdTreeGenerator;

public class NNSolverOrchestratorTest {
	private static final int POINT_COUNT = 100_000;

	@Test(expected = NNSolverInterruptedException.class)
	public void testInterruptThingy() {
		final int dimensionCount = 3;
		final int workerThreadsCount = 10;

		final RandomDoubleKdTreeGenerator treeGenerator = new RandomDoubleKdTreeGenerator();

		final List<KdPoint<Double>> inputPoints = treeGenerator.generatePoints(dimensionCount, POINT_COUNT);
		final KdTree<Double> tree = new KdTree<>(dimensionCount, inputPoints);

		final NNSolverOrchestrator<Double> orchestrator = new NNSolverOrchestrator<>(tree, workerThreadsCount);

		Thread.currentThread().interrupt();

		orchestrator.findNearestPoints(inputPoints);
	}

	@Test
	public void testFindNearestPointsFixedWorkerThreadCount() {
		final int dimensionCount = 3;
		final int workerThreadsCount = 10;

		final RandomDoubleKdTreeGenerator treeGenerator = new RandomDoubleKdTreeGenerator();

		final List<KdPoint<Double>> inputPoints = treeGenerator.generatePoints(dimensionCount, POINT_COUNT);
		final KdTree<Double> tree = new KdTree<>(dimensionCount, inputPoints);

		final NNSolverOrchestrator<Double> orchestrator = new NNSolverOrchestrator<>(tree, workerThreadsCount);

		performOrchestratorTest(orchestrator, inputPoints);
	}

	@Test
	public void testFindNearestPointsDynamicWorkerThreadCount() {
		final int dimensionCount = 3;
		final int workerThreadsCount = 10;

		final RandomDoubleKdTreeGenerator treeGenerator = new RandomDoubleKdTreeGenerator();

		final List<KdPoint<Double>> inputPoints = treeGenerator.generatePoints(dimensionCount, POINT_COUNT);
		final KdTree<Double> tree = new KdTree<>(dimensionCount, inputPoints);

		final NNSolverOrchestrator<Double> orchestrator = new NNSolverOrchestrator<>(tree);

		performOrchestratorTest(orchestrator, inputPoints);
	}

	private void performOrchestratorTest(final NNSolverOrchestrator<Double> orchestrator,
	        final List<KdPoint<Double>> inputPoints) {
		// Request the result from the orchestrator.

		final List<KdPoint<Double>> resultPoints = orchestrator.findNearestPoints(inputPoints);

		assertEquals(inputPoints.size(), resultPoints.size());

		for (int i = 0; i < resultPoints.size(); i++) {
			// Make sure each result point is different from its input point at that index.

			assertNotEquals(inputPoints.get(i), resultPoints.get(i));
		}

		// Build a new list of request points that are based on the original tree
		// points. Then we expect the orchestrator to return the original point
		// instances for each new point.

		final List<KdPoint<Double>> identicalRequestPoints = new ArrayList<>();

		for (final KdPoint<Double> inputPoint : inputPoints) {
			final KdPoint<Double> deepCopy = new KdPoint<>(inputPoint);

			identicalRequestPoints.add(deepCopy);
		}
	}
}
