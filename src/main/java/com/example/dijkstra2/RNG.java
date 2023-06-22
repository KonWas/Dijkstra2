package com.example.dijkstra2;
import java.util.*;

//public class RelativeNeighborhoodGraph {
//    private List<Edge>[] graph;
//    private double[][] coordinates;
//
//    public RelativeNeighborhoodGraph(int numVertices) {
//        this.coordinates = generateCoordinates(numVertices);
//        this.graph = generateGraph(numVertices);
//    }
//
//    public void findAndDisplayCycles() {
//        List<List<Integer>> cycles = findCycles();
//        cycles.sort(Comparator.comparingInt(this::calculateCycleWeight).reversed());
//
//        for (int i = 0; i < cycles.size(); i++) {
//            List<Integer> cycle = cycles.get(i);
//            if (cycle.size() > 2) {
//                System.out.println("Cycle " + (i + 1) + ": " + cycle.toString());
//                System.out.println("Cycle weight: " + calculateCycleWeight(cycle));
//                System.out.println();
//            }
//        }
//    }
//
//    public int getNumVertices() {
//        return graph.length - 1;
//    }
//
//    public List<Edge> getEdges() {
//        List<Edge> edges = new ArrayList<>();
//
//        for (List<Edge> edgeList : graph) {
//            if (edgeList != null) {
//                edges.addAll(edgeList);
//            }
//        }
//
//        return edges;
//    }
//
//    public double[] getCoordinates(int vertex) {
//        if (vertex >= 1 && vertex < coordinates.length) {
//            return coordinates[vertex];
//        } else {
//            throw new IllegalArgumentException("Invalid vertex");
//        }
//    }
//
//    private List<List<Integer>> findCycles() {
//        List<List<Integer>> cycles = new ArrayList<>();
//        Set<Integer> visited = new HashSet<>();
//
//        for (int i = 1; i < graph.length; i++) {
//            dfs(i, i, new ArrayList<>(), visited, cycles);
//        }
//
//        return cycles;
//    }
//
//    private void dfs(int start, int current, List<Integer> path, Set<Integer> visited, List<List<Integer>> cycles) {
//        visited.add(current);
//        path.add(current);
//
//        for (Edge edge : graph[current]) {
//            int neighbor = edge.getEnd();
//
//            if (neighbor == start) {
//                // Found a cycle
//                cycles.add(new ArrayList<>(path));
//            }
//
//            if (!visited.contains(neighbor)) {
//                dfs(start, neighbor, path, visited, cycles);
//            }
//        }
//
//        path.remove(path.size() - 1);
//        visited.remove(current);
//    }
//
//    private int calculateCycleWeight(List<Integer> cycle) {
//        int weight = 0;
//        for (int i = 0; i < cycle.size(); i++) {
//            int start = cycle.get(i);
//            int end = cycle.get((i + 1) % cycle.size());
//            weight += getEdgeWeight(start, end);
//        }
//        return weight;
//    }
//
//    private int getEdgeWeight(int start, int end) {
//        for (Edge edge : graph[start]) {
//            if (edge.getEnd() == end) {
//                return edge.getWeight();
//            }
//        }
//        return 0;
//    }
//
//    private List<Edge>[] generateGraph(int numVertices) {
//        if (coordinates == null) {
//            throw new IllegalStateException("coordinates");
//        }
//
//        List<Edge>[] graph = new ArrayList[numVertices + 1];
//
//        for (int i = 1; i <= numVertices; i++) {
//            graph[i] = new ArrayList<>();
//        }
//
//        for (int i = 1; i <= numVertices; i++) {
//            for (int j = i + 1; j <= numVertices; j++) {
//                boolean canConnect = false;
//                for (int k = 1; k <= numVertices; k++) {
//                    if (k != i && k != j) {
//                        double distance1 = calculateDistance(coordinates[i][0], coordinates[i][1], coordinates[k][0], coordinates[k][1]);
//                        double distance2 = calculateDistance(coordinates[j][0], coordinates[j][1], coordinates[k][0], coordinates[k][1]);
//                        double distance3 = calculateDistance(coordinates[i][0], coordinates[i][1], coordinates[j][0], coordinates[j][1]);
//
//                        if (distance1 < distance3 && distance2 < distance3) {
//                            canConnect = true;
//                            break;
//                        }
//                    }
//                }
//
//                if (!canConnect) {
//                    double distance = calculateDistance(coordinates[i][0], coordinates[i][1], coordinates[j][0], coordinates[j][1]);
//                    int weight = (int) Math.ceil(distance + 0.5);
//                    graph[i].add(new Edge(i, j, weight));
//                    graph[j].add(new Edge(j, i, weight));
//                }
//            }
//        }
//
//        return graph;
//    }
//
//    private double[][] generateCoordinates(int numVertices) {
//        double[][] coordinates = new double[numVertices + 1][2];
//
//        for (int i = 1; i <= numVertices; i++) {
//            coordinates[i][0] = Math.random() * 300; // Random x-coordinate in the range (-10, 10)
//            coordinates[i][1] = Math.random() * 300; // Random y-coordinate in the range (-10, 10)
//        }
//
//        return coordinates;
//    }
//
//    private double calculateDistance(double x1, double y1, double x2, double y2) {
//        double dx = x2 - x1;
//        double dy = y2 - y1;
//        return Math.sqrt(dx * dx + dy * dy);
//    }
//}




import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RNG {
    private List<Edge>[] graph;
    private double[][] coordinates;

    public RNG(int numVertices) {
        this.coordinates = generateCoordinates(numVertices);
        this.graph = generateGraph(numVertices);
    }

    public void findAndDisplayCycles() {
        List<List<Integer>> cycles = findCycles();

        Set<List<Integer>> uniqueCycles = removeDuplicateCycles(cycles);

        List<List<Integer>> result = new ArrayList<>(uniqueCycles);
        result.sort(Comparator.comparingInt(this::calculateCycleWeight).reversed());

        for (int i = 0; i < result.size(); i++) {
            List<Integer> cycle = result.get(i);
            displayCycle(i+1, cycle);
        }
    }

    private void displayCycle(int index, List<Integer> cycle) {
        if (cycle.size() > 2) {
            System.out.println("Cycle " + index + ": " + cycle.toString());
            System.out.println("Cycle weight: " + calculateCycleWeight(cycle));
            System.out.println();
        }
    }

    private Set<List<Integer>> removeDuplicateCycles(List<List<Integer>> cycles) {
        Set<List<Integer>> uniqueCycles = new LinkedHashSet<>();
        Set<List<Integer>> sortedCycles = new HashSet<>();

        for (List<Integer> cycle : cycles) {
            List<Integer> sortedCycle = new ArrayList<>(cycle);
            sortedCycle.sort(Comparator.naturalOrder());

            if (sortedCycles.contains(sortedCycle)) {
                continue;
            }

            sortedCycles.add(sortedCycle);
            uniqueCycles.add(cycle);
        }

        return uniqueCycles;
    }

    public int getNumVertices() {
        return graph.length - 1;
    }

    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();

        for (List<Edge> edgeList : graph) {
            if (edgeList != null) {
                edges.addAll(edgeList);
            }
        }

        return edges;
    }

    public double[] getCoordinates(int vertex) {
        if (vertex >= 1 && vertex < coordinates.length) {
            return coordinates[vertex];
        } else {
            throw new IllegalArgumentException("Invalid vertex");
        }
    }

    private List<List<Integer>> findCycles() {
        List<List<Integer>> cycles = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        for (int i = 1; i < graph.length; i++) {
            if (!visited.contains(i)) {
                dfs(i, visited, new ArrayList<>(), cycles);
            }
        }

        return cycles;
    }

    private void dfs(int current, Set<Integer> visited, List<Integer> path, List<List<Integer>> cycles) {
        visited.add(current);
        path.add(current);

        for (Edge edge : graph[current]) {
            int neighbor = edge.getEnd();

            if (path.size() > 2 && neighbor == path.get(0)) {
                // Found a cycle
                List<Integer> cycle = new ArrayList<>(path);
                cycles.add(cycle);
            }

            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited, path, cycles);
            }
        }

        visited.remove(current);
        path.remove(path.size() - 1);
    }

    private int calculateCycleWeight(List<Integer> cycle) {
        int weight = 0;
        for (int i = 0; i < cycle.size(); i++) {
            int start = cycle.get(i);
            int end = cycle.get((i + 1) % cycle.size());
            weight += getEdgeWeight(start, end);
        }
        return weight;
    }

    private int getEdgeWeight(int start, int end) {
        for (Edge edge : graph[start]) {
            if (edge.getEnd() == end) {
                return edge.getWeight();
            }
        }
        return 0;
    }

    public List<Edge>[] getGraph() {
        return graph;
    }

    public List<Edge>[] generateGraph(int numVertices) {
        if (coordinates == null) {
            throw new IllegalStateException("coordinates");
        }

        List<Edge>[] graph = new ArrayList[numVertices + 1];

        for (int i = 1; i <= numVertices; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 1; i <= numVertices; i++) {
            for (int j = i + 1; j <= numVertices; j++) {
                boolean canConnect = false;
                for (int k = 1; k <= numVertices; k++) {
                    if (k != i && k != j) {
                        double distance1 = calculateDistance(coordinates[i][0], coordinates[i][1], coordinates[k][0], coordinates[k][1]);
                        double distance2 = calculateDistance(coordinates[j][0], coordinates[j][1], coordinates[k][0], coordinates[k][1]);
                        double distance3 = calculateDistance(coordinates[i][0], coordinates[i][1], coordinates[j][0], coordinates[j][1]);

                        if (distance1 < distance3 && distance2 < distance3) {
                            canConnect = true;
                            break;
                        }
                    }
                }

                if (!canConnect) {
                    double distance = calculateDistance(coordinates[i][0], coordinates[i][1], coordinates[j][0], coordinates[j][1]);
                    int weight = (int) Math.ceil(distance + 0.5);
                    graph[i].add(new Edge(i, j, weight));
                    graph[j].add(new Edge(j, i, weight));
                }
            }
        }

        return graph;
    }

    private double[][] generateCoordinates(int numVertices) {
        double[][] coordinates = new double[numVertices + 1][2];

        for (int i = 1; i <= numVertices; i++) {
            coordinates[i][0] = Math.random() * 300; // Random x-coordinate in the range (-10, 10)
            coordinates[i][1] = Math.random() * 300; // Random y-coordinate in the range (-10, 10)
        }

        return coordinates;
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private List<List<Integer>> removeDuplicates(List<List<Integer>> cycles) {
        Set<List<Integer>> uniqueCycles = new HashSet<>();

        for (List<Integer> cycle : cycles) {
            List<Integer> sortedCycle = new ArrayList<>(cycle);
            sortedCycle.sort(Comparator.naturalOrder());
            uniqueCycles.add(sortedCycle);
        }

        return new ArrayList<>(uniqueCycles);
    }
}
