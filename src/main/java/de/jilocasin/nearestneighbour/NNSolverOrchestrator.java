package de.jilocasin.nearestneighbour;

import java.util.ArrayList;
import java.util.List;

import de.jilocasin.nearestneighbour.kdtree.KdPoint;
import de.jilocasin.nearestneighbour.kdtree.KdTree;

public class NNSolverOrchestrator<T extends Number & Comparable<T>> {
	private final KdTree<T> tree;
	private final int workerThreadsCount;

	public NNSolverOrchestrator(final KdTree<T> tree, final int workerThreadsCount) {
		this.tree = tree;
		this.workerThreadsCount = workerThreadsCount;
	}

	public List<KdPoint<T>> findNearestPoints(final List<KdPoint<T>> inputPoints) {
		final List<NNSolverWorker<T>> workers = new ArrayList<>();

		final int batchSize = inputPoints.size() / workerThreadsCount;

		int startIndex = 0;

		for (int i = 0; i < workerThreadsCount; i++) {
			final int endIndex = Math.min(startIndex + batchSize, inputPoints.size());

			final List<KdPoint<T>> subset = inputPoints.subList(startIndex, endIndex);

			startIndex += batchSize;

			final NNSolverWorker<T> worker = new NNSolverWorker<>(tree, subset);

			workers.add(worker);

			worker.start();
		}

		// Join the individual workers and append their data.

		final List<KdPoint<T>> resultPoints = new ArrayList<>(inputPoints);

		for (final NNSolverWorker<T> worker : workers) {
			resultPoints.addAll(worker.getResultPoints());
		}

		return resultPoints;
	}
}
