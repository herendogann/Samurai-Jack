import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Kingdom {
    private int[][] kingdomMap;
    private int rows;
    private int cols;

    public void initializeKingdom(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        rows = lines.size();
        cols = lines.get(0).split(" ").length;
        kingdomMap = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            String[] row = lines.get(i).split(" ");
            for (int j = 0; j < cols; j++) {
                kingdomMap[i][j] = Integer.parseInt(row[j]);
            }
        }
    }

    public List<Colony> getColonies() {
        List<Colony> colonies = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (kingdomMap[i][j] == 1 && !visited.contains(i * cols + j)) {
                    Colony colony = new Colony();
                    dfs(i, j, visited, colony);
                    colonies.add(colony);
                }
            }
        }
        return colonies;
    }
    private void dfs(int i, int j, Set<Integer> visited, Colony colony) {
        visited.add(i * cols + j);
        colony.cities.add(i * cols + j);
        colony.roadNetwork.put(i * cols + j, new ArrayList<>());
        if (i > 0 && kingdomMap[i - 1][j] == 1 && !visited.contains((i - 1) * cols + j)) {
            colony.roadNetwork.get(i * cols + j).add((i - 1) * cols + j);
            dfs(i - 1, j, visited, colony);
        }
        if (i < rows - 1 && kingdomMap[i + 1][j] == 1 && !visited.contains((i + 1) * cols + j)) {
            colony.roadNetwork.get(i * cols + j).add((i + 1) * cols + j);
            dfs(i + 1, j, visited, colony);
        }
        if (j > 0 && kingdomMap[i][j - 1] == 1 && !visited.contains(i * cols + j - 1)) {
            colony.roadNetwork.get(i * cols + j).add(i * cols + j - 1);
            dfs(i, j - 1, visited, colony);
        }
        if (j < cols - 1 && kingdomMap[i][j + 1] == 1 && !visited.contains(i * cols + j + 1)) {
            colony.roadNetwork.get(i * cols + j).add(i * cols + j + 1);
            dfs(i, j + 1, visited, colony);
        }
    }

    public void printColonies(List<Colony> discoveredColonies) {
        System.out.println("Number of colonies: " + discoveredColonies.size());
        for (int i = 0; i < discoveredColonies.size(); i++) {
            Colony colony = discoveredColonies.get(i);
            System.out.println("Colony " + (i + 1) + ":");
            System.out.println("Cities: " + colony.cities);
            System.out.println("Road Network:");
            for (int j = 0; j < colony.cities.size(); j++) {
                int city = colony.cities.get(j);
                System.out.println(city + ": " + colony.roadNetwork.get(city));
            }
        }
    }
}
