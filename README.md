[![Build Status](https://travis-ci.org/Jilocasin/nearest-neighbour.svg?branch=master)](https://travis-ci.org/Jilocasin/nearest-neighbour) [![Coverage Status](https://coveralls.io/repos/github/Jilocasin/nearest-neighbour/badge.svg)](https://coveralls.io/github/Jilocasin/nearest-neighbour)

# nearest-neighbour
Finding the [nearest neighbour](https://en.wikipedia.org/wiki/Nearest_neighbor_search) of a k-dimensional set of points using an efficient [k-d tree](https://en.wikipedia.org/wiki/K-d_tree) data structure.

## Installation
Just download the repository and include the sources in your project. I will try to set up a maven repository soon™.

## Usage
To search for any nearest neighbours points, you first need to set up a `KdTree` based on a list of `KdPoints`. Both classes use generics to allow any point data payload. Use `Integer`, `Double` or any other class extending `java.lang.Number` and implementing the `Comparable` interface.

Start by setting up a list of k-dimensional points:
```java
List<KdPoint<Integer>> points = new ArrayList<>();

points.add(new KdPoint<>(5, 8));
points.add(new KdPoint<>(5, 5));
points.add(new KdPoint<>(9, 1));
```

Now set up a `KdTree` based on those points. For performance reason, only the first provided point is used to determine the dimension count of the tree. All subsequent points are expected to have the same number of axis values.

```java
KdTree<Integer> tree = new KdTree<>(points);
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
List<KdPoint<Integer>> searchPoints = new ArrayList<>();
searchPoints.add(new KdPoint<>(1, 1));
searchPoints.add(new KdPoint<>(7, 7));
    
NNSolverOrchestrator<Integer> solverOrchestrator = new NNSolverOrchestrator<>(tree);

List<KdPoint<Integer>> nearestPoints = solverOrchestrator.findNearestPoints(searchPoints);
```

This will use an orchestrator instance to distribute the calculation workload to a number of worker threads equal to the number of processor cores available. You can also specify the number of worker threads yourself when creating the NNSolverOrchestrator instance as a second parameter.

Note that the orchestrator simply returns a new list of points. The index of each result point corresponds to the index of the provided search points. So, the nearest point for the first search point will be returned at `nearestPoints.get(0)` etc.

## Note
This is my first open source repo and there may be issues, there may be bugs, things will catch fire - so bare with me. I will try to use this as a step-by-step example project for future open source work.

## To-Do
* Make this lib available on Maven
* Grab a coffee ☕
