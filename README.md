# nearest-neighbour [![Build Status](https://travis-ci.org/Jilocasin/nearest-neighbour.svg?branch=master)](https://travis-ci.org/Jilocasin/nearest-neighbour) [![Coverage Status](https://coveralls.io/repos/github/Jilocasin/nearest-neighbour/badge.svg)](https://coveralls.io/github/Jilocasin/nearest-neighbour)
Small  **Java** library to find the [nearest neighbour](https://en.wikipedia.org/wiki/Nearest_neighbor_search) of a k-dimensional set of points using an efficient [k-d tree](https://en.wikipedia.org/wiki/K-d_tree).

## Installation
Just download the repository and include the sources in your project. I will try to set up a maven repository soon™.

## Usage
To search for any nearest neighbours points, you first need to set up a `KdTree` based on a list of `KdPoint` objects. Both classes allow arbitrary value types like `Integer`, `Double` or any other class derived from `java.lang.Number` and implementing `Comparable`.

### Show me some code
Alright.

Start by setting up a list of k-dimensional points:
```java
List<KdPoint<Integer>> points = new ArrayList<>();

points.add(new KdPoint<>(5, 8));
points.add(new KdPoint<>(5, 5));
points.add(new KdPoint<>(9, 1));
```

Now set up a `KdTree` based on those points. For performance reason, only the first provided point is used to determine the dimension count of the tree. All subsequent points are expected to have the same number of axis values.

Note that once the tree instance has been set up, no points can be added or removed at a later time. This may be subject for future improvements.

```java
KdTree<Integer> tree = new KdTree<>(points);
```

To calculate the nearest neighbour of any arbitrary point, use a ```NNSolver``` for the used axis type.
```java
NNSolver<Integer> solver = new NNSolver<>(tree);
		
KdPoint<Integer> searchPoint = new KdPoint<>(5, 10);
KdPoint<Integer> nearestPoint = solver.findNearestPoint(searchPoint);

// Returns the point at (5, 8)
```

This library allows nearest neighbour search for arbitrary point instances not used during tree setup, as well as existing tree points. When providing a point instance included in the tree data, the closest point except the search point itself will be returned.

```java
KdPoint<Integer> searchPoint = points.get(0);
KdPoint<Integer> nearestOtherPoint = solver.findNearestPoint(searchPoint);

// Returns the nearest point at (5, 5) to this instance,
// instead of the point instance at (5, 8) we used to search.
//
// If we had provided a new point instance at (5, 8) instead,
// the returned nearest point would be the original point at (5, 8).
```

When dealing with larger sets of data, you should always use a `NNSolverOrchestrator` to get the best performance. It will distribute the workload to a given number of threads. 

Using an orchestrator is just as easy:

```java
List<KdPoint<Integer>> searchPoints = new ArrayList<>();
searchPoints.add(new KdPoint<>(1, 1));
searchPoints.add(new KdPoint<>(7, 7));
    
NNSolverOrchestrator<Integer> solverOrchestrator = new NNSolverOrchestrator<>(tree);

List<KdPoint<Integer>> nearestPoints = solverOrchestrator.findNearestPoints(searchPoints);
```

This will use an orchestrator to distribute the calculation workload to a number of worker threads equal to the number of processor cores available. You can also specify the number of worker threads yourself when creating the NNSolverOrchestrator instance as a second parameter: `new NNSolverOrchestrator<>(tree, numberOfThreads)`.

Note that the call to `findNearestPoints` in this case returns a list of points. The index of each result point corresponds to the index of the requested search point. The nearest point to the first search point will be returned at `nearestPoints.get(0)` etc.

## Note
This is my first open source repo and there may be issues, there may be bugs, things will catch fire - so bare with me.

My aim is to use this project as a step-by-step example for my future open source work.

## To-Do
* Use a `KdDistanceCalculator` interface to provide different methods, e.g. [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry)
* Allow editing an existing tree by adding or removing points
* Make this library available on Maven Central
* Grab a coffee ☕
