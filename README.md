[![Build Status](https://travis-ci.org/Jilocasin/nearest-neighbour.svg?branch=master)](https://travis-ci.org/Jilocasin/nearest-neighbour) [![Coverage Status](https://coveralls.io/repos/github/Jilocasin/nearest-neighbour/badge.svg?branch=master)](https://coveralls.io/github/Jilocasin/nearest-neighbour?branch=master)

# nearest-neighbour
Finding the [nearest neighbour](https://en.wikipedia.org/wiki/Nearest_neighbor_search) of a k-dimensional set of points using an efficient [k-d tree](https://en.wikipedia.org/wiki/K-d_tree) data structure.

## Installation
Just download the repository and include the sources in your project.

## Usage
To search for any nearest neighbours points, you first need to set up a `KdTree` based on a list of `KdPoints`. Both classes use generics to allow any point data payload. Use `Integer`, `Double` or any other class extending `java.lang.Number` and implementing the `Comparable` interface.

Start by setting up a list of k-dimensional points:
```java
List<KdPoint<Integer>> points = new ArrayList<>();

points.add(new KdPoint<>(5, 8));
points.add(new KdPoint<>(5, 5));
points.add(new KdPoint<>(9, 1));
```

Now set up a `KdTree` based on those points. Pay attention to the correct dimension value, matching the amount of individual axis values used when creating the points. For performance reason the number of axis values per point is not checked during tree creation.

```java
int dimensions = 2;
KdTree<Integer> tree = new KdTree<>(dimensions, points);
```

To calculate the nearest neighbour of an arbitrary point, use a generic ```NNSolver``` for the used axis type.
```java
NNSolver<Integer> solver = new NNSolver<>(tree);
		
KdPoint<Integer> searchPoint = new KdPoint<>(5, 10);
KdPoint<Integer> nearestOtherPoint = solver.findNearestPoint(searchPoint);

// Returns the point at (5, 8)
```

This library allows nearest neighbour search for arbitrary point instances not used during tree setup, as well as existing tree points. When providing a point instance included in the tree data, the closest point except the search point itself will be returned.

```java
KdPoint<Integer> searchPoint = points.get(0);
KdPoint<Integer> nearestOtherPoint = solver.findNearestPoint(searchPoint);

// Returns the nearest point at (5, 5) to this instance,
// instead of the point instance at (5, 8) we used to search.
// When provided a new point instance at (5, 8) instead,
// the closest point would be the original point at (5, 8).
```

You should always use a `NNSolverOrchestrator` to get the best performance. It will distribute the workload to a given number of threads. 

Using an orchestrator is just as easy:

```java
int workerThreadsCount = Runtime.getRuntime().availableProcessors();

List<KdPoint<Integer>> searchPoints = new ArrayList<>();
searchPoints.add(new KdPoint<>(1, 1));
searchPoints.add(new KdPoint<>(7, 7));
    
NNSolverOrchestrator<Integer> solverOrchestrator = new NNSolverOrchestrator<>(tree, workerThreadsCount);

List<KdPoint<Integer>> nearestPoints = solverOrchestrator.findNearestPoints(searchPoints);
```

Note that the orchestrator simply returns a new list of points. The index of each result point corresponds to the index of the provided search points. So, the nearest point for the first search point will be returned at `nearestPoints.get(0)` etc.

## Note
This is my first open source repo and there may be issues, there may be bugs, things will catch fire - so bare with me. I will try to use this as a step-by-step example project for future open source work.

## To-Do
* Availability on Maven
* Improve unit test coverage and test cases
* Perform automated builds/tests to embed the funny green status image (I have to get into this topic first)
* Grab a coffee ☕
