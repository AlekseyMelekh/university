import javafx.util.Pair;

import java.util.ArrayList;

public class TopSort {

    private ArrayList<Pair<Pair<Integer, Integer>, String>>[][] graph;
    private boolean[][] used;
    private int[][] cycleUsed;
    private Pair[][] parent;
    private boolean cycle;
    private int rows;
    private int columns;

    public TopSort (ArrayList<Pair<Pair<Integer, Integer>, String>>[][] graph, int rows, int columns) {
        this.graph = graph;
        this.rows = rows;
        this.columns = columns;
        used = new boolean[rows][columns];
        cycleUsed = new int[rows][columns];
        parent = new Pair[rows][columns];
        cycle = false;
    }

    private void dfs(int i, int j, ArrayList<Pair<Integer, Integer>> ans) {
        used[i][j] = true;
        for (int ind = 0; ind < graph[i][j].size(); ++ind) {
            int to_i = graph[i][j].get(ind).getKey().getKey();
            int to_j = graph[i][j].get(ind).getKey().getValue();
            if (!used[to_i][to_j]) {
                dfs(to_i, to_j, ans);
            }
        }
        ans.add(new Pair<>(i, j));
    }

    public ArrayList<Pair<Integer, Integer>> work() {
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                used[i][j] = false;
            }
        }
        ArrayList<Pair<Integer, Integer>> ans = new ArrayList<>();
        for (int i = 1; i < this.rows; ++i) {
            for (int j = 1; j < this.columns; ++j) {
                if (!used[i][j]) {
                    dfs(i, j, ans);
                }
            }
        }
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                used[i][j] = false;
                parent[i][j] = new Pair<>(-1, -1);
            }
        }
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                if (!used[i][j]) {
                    cycleDfs(i, j);
                }
            }
        }
        return ans;
    }

    private void cycleDfs(int i, int j) {
        cycleUsed[i][j] = 1;
        for (int ind = 0; ind < graph[i][j].size(); ++ind) {
            int to_i = graph[i][j].get(ind).getKey().getKey();
            int to_j = graph[i][j].get(ind).getKey().getValue();
            if (cycleUsed[to_i][to_j] == 0) {
                parent[to_i][to_j] = new Pair(i, j);
                cycleDfs(to_i, to_j);
            } else {
                if (cycleUsed[to_i][to_j] == 1) {
                    cycle = true;
                }
            }
        }
        cycleUsed[i][j] = 2;
    }

    public boolean isCycle() {
        return cycle;
    }
}
