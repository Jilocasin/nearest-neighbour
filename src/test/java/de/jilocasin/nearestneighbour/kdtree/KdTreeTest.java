package de.jilocasin.nearestneighbour.kdtree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import de.jilocasin.nearestneighbour.kdtree.exception.InvalidKdPointCountException;
import de.jilocasin.nearestneighbour.kdtree.generator.RandomDoubleKdTreeGenerator;

public class KdTreeTest {
	private static final int POINT_COUNT = 100_000;

	private static final int MIN_DIMENSIONS = 1;
	private static final int MAX_DIMENSIONS = 10;

	@Test
	public void testKdTree() {
		for (int dimensionCount = MIN_DIMENSIONS; dimensionCount <= MAX_DIMENSIONS; dimensionCount++) {
			testKdTreeWithDimensionCount(dimensionCount);
		}
	}

	@Test(expected = InvalidKdPointCountException.class)
	public void testKdTreeWithNoPoints() {
		// Expected to throw.

		new KdTree<>(new ArrayList<>());
	}

	public void testKdTreeWithDimensionCount(final int dimensionCount) {
		final RandomDoubleKdTreeGenerator treeGenerator = new RandomDoubleKdTreeGenerator();

		final KdTree<Double> tree = treeGenerator.generate(dimensionCount, POINT_COUNT);

		assertEquals(dimensionCount, tree.dimensionCount);

		assertFalse(tree.rootNode.hasParentNode());

		// To test the tree, we have to traverse each node, performing various checks.

		checkNode(tree, tree.rootNode);
	}

	private void checkNode(final KdTree<Double> tree, final KdNode<Double> node) {
		final int axisIndex = node.axisIndex;

		final KdNode<Double> leftNode = node.getLeftNode();

		if (leftNode != null) {
			checkNode(tree, leftNode);

			assertTrue(node.hasLeftNode());

			// Assert that the children depth is greater that the current depth.

			assertEquals(leftNode.depth, node.depth + 1);

			assertEquals(node.axisIndex, tree.getAxisIndex(node.depth));
			assertEquals(leftNode.axisIndex, tree.getAxisIndex(leftNode.depth));

			// Assert that the value of the left node on the relevant axis index is always
			// smaller than or equal to the parent we're checking.

			assertTrue(leftNode.point.getAxisValue(axisIndex) <= node.point.getAxisValue(axisIndex));

			// Assert that the children has a parent and the parent is the current node.

			assertTrue(leftNode.hasParentNode());
			assertEquals(node, leftNode.getParentNode());
		} else {
			assertFalse(node.hasLeftNode());
		}

		final KdNode<Double> rightNode = node.getRightNode();

		if (rightNode != null) {
			checkNode(tree, rightNode);

			assertTrue(node.hasRightNode());

			// Assert that the children depth is greater that the current depth.

			assertEquals(rightNode.depth, node.depth + 1);

			assertEquals(node.axisIndex, tree.getAxisIndex(node.depth));
			assertEquals(rightNode.axisIndex, tree.getAxisIndex(rightNode.depth));

			// Assert that the value of the right node on the relevant axis index is always
			// larger than the parent we're checking.

			assertTrue(rightNode.point.getAxisValue(axisIndex) > node.point.getAxisValue(axisIndex));

			// Assert that the children has a parent and the parent is the current node.

			assertTrue(rightNode.hasParentNode());
			assertEquals(node, rightNode.getParentNode());
		} else {
			assertFalse(node.hasRightNode());
		}

		// The children count of this node must correspond to the actual nodes instances
		// we requested earlier.

		if (leftNode != null && rightNode != null) {
			assertTrue(node.hasChildren());
			assertEquals(node.numberOfChildren(), 2);
		} else if (leftNode != null || rightNode != null) {
			assertTrue(node.hasChildren());
			assertEquals(node.numberOfChildren(), 1);
		} else {
			assertFalse(node.hasChildren());
			assertEquals(node.numberOfChildren(), 0);
		}
	}
}
