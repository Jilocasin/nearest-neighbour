package de.jilocasin.nearestneighbour.nnsolver;

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

	/**
	 * 
	 * @param inputPoints
	 * @return
	 * @throws NNSolverInterruptedException
	 *             if the calling thread was interrupted during the async operation.
	 */
	public List<KdPoint<T>> findNearestPoints(final List<KdPoint<T>> inputPoints) throws NNSolverInterruptedException {
		final List<NNSolverWorker<T>> workers = new ArrayList<>(workerThreadsCount);

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

		final List<KdPoint<T>> resultPoints = new ArrayList<>(inputPoints.size());

		for (final NNSolverWorker<T> worker : workers) {
			try {
				resultPoints.addAll(worker.getResultPoints());
			} catch (final InterruptedException e) {
				// Somebody decided to interrupt this orchestrator call.

				throw new NNSolverInterruptedException(e);
			}
		}

		return resultPoints;
	}
}
