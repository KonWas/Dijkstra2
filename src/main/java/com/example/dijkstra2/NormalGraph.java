package com.example.dijkstra2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NormalGraph {

    private List<Edge>[] graph;
    private int n;
    private int weightRange;

    public NormalGraph(int n, int weightRange) {
        graph = generateRandomGraph(n, weightRange);
        this.n = n;
        this.weightRange = weightRange;
    }

    public List<Edge>[] generateRandomGraph(int n, int weightRange) {
        List<Edge>[] graph = new List[n + 1];

        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 1; i <= n; i++) {
            int numEdges = getRandomNumberInRange(1, n - 1);
            Set<Integer> usedVertices = new HashSet<>();
            usedVertices.add(i);

            while (graph[i].size() < numEdges) {
                int end = getRandomNumberInRange(1, n);
                int weight = getRandomNumberInRange(1, weightRange);

                if (end != i && !usedVertices.contains(end)) {
                    boolean edgeExists = false;
                    for (Edge edge : graph[i]) {
                        if (edge.end == end) {
                            edgeExists = true;
                            break;
                        }
                    }

                    if (!edgeExists) {
                        graph[i].add(new Edge(i, end, weight));
                        graph[end].add(new Edge(end, i, weight));
                        usedVertices.add(end);
                    }
                }
            }
        }

        return graph;
    }

    public List<Edge>[] getGraph() {
        return graph;
    }

    public int getRandomNumberInRange(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
