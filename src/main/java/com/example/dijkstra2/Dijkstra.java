package com.example.dijkstra2;

import java.util.*;

public class Dijkstra {
    private List<Edge>[] graph;
    private int startVertex;
    private int endVertex;
    private int[] distances;

    public Dijkstra(List<Edge>[] graph, int startVertex, int endVertex) {
        this.graph = graph;
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        distances = runDijkstraAlgorithm(graph, startVertex);
    }

    private final int INFINITY = Integer.MAX_VALUE;

    public int[] runDijkstraAlgorithm(List<Edge>[] graph, int startVertex) {
        int n = graph.length - 1;
        int[] distances = new int[n + 1];
        Arrays.fill(distances, INFINITY);
        distances[startVertex] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(startVertex, 0));

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            int vertex = node.vertex;
            int distance = node.distance;

            if (distance > distances[vertex]) {
                continue;
            }

            for (Edge edge : graph[vertex]) {
                int neighbor = edge.end;
                int newDistance = distance + edge.weight;

                if (newDistance < distances[neighbor]) {
                    distances[neighbor] = newDistance;
                    pq.offer(new Node(neighbor, newDistance));
                }
            }
        }

        return distances;
    }

    public List<Integer> getShortestPath(int[] distances, int startVertex, int endVertex, List<Edge>[] graph) {
        if (distances[endVertex] == INFINITY) {
            return Collections.emptyList();
        }

        List<Integer> path = new ArrayList<>();
        path.add(endVertex);

        while (endVertex != startVertex) {
            for (Edge edge : graph[endVertex]) {
                int neighbor = edge.getEnd();
                int weight = edge.getWeight();

                if (distances[neighbor] + weight == distances[endVertex]) {
                    path.add(0, neighbor);
                    endVertex = neighbor;
                    break;
                }
            }
        }

        return path;
    }

    public int[] getDistances() {
        return distances;
    }

    public int getNumVertices() {
        return graph.length - 1;
    }

    public List<Edge>[] getGraph() {
        return graph;
    }
    public void setGraph(List<Edge>[] graph) {
        this.graph = graph;
    }

    public int getEndVertex() {
        return endVertex;
    }

    public int getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(int startVertex) {
        this.startVertex = startVertex;
    }

    public void setEndVertex(int endVertex) {
        this.endVertex = endVertex;
    }
}

