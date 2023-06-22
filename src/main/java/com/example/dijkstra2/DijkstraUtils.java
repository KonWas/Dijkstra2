package com.example.dijkstra2;

import java.util.List;

public class DijkstraUtils {
    public static boolean shortestPathContainsEdge(List<Integer> shortestPath, int start, int end) {
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            int currentVertex = shortestPath.get(i);
            int nextVertex = shortestPath.get(i + 1);
            if ((currentVertex == start && nextVertex == end) || (currentVertex == end && nextVertex == start)) {
                return true;
            }
        }
        return false;
    }
}
