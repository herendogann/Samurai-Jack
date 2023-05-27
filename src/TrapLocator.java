import java.util.*;

public class TrapLocator {
    public List<Colony> colonies;

    public TrapLocator(List<Colony> colonies) {
        this.colonies = colonies;
    }

    public List<List<Integer>> revealTraps() {
        List<List<Integer>> traps = new ArrayList<>();

        for (Colony colony : colonies) {
            List<Integer> trapCities = new ArrayList<>();
            int maxCityId = colony.cities.stream().max(Integer::compareTo).orElse(0);

            for (int city : colony.cities) {
                if (colony.roadNetwork.get(city) == null) {
                    continue;
                }

                boolean[] visited = new boolean[maxCityId + 1];
                boolean[] recStack = new boolean[maxCityId + 1];
                if (isCyclicUtil(city, visited, recStack, colony.roadNetwork, trapCities)) {
                    break;
                }
            }
            Collections.sort(trapCities);
            traps.add(trapCities);
        }

        return traps;
    }
    private boolean isCyclicUtil(int city, boolean[] visited, boolean[] recStack, HashMap<Integer, List<Integer>> roadNetwork, List<Integer> trapCities) {
        if (recStack[city]) {
            trapCities.add(city);
            return true;
        }
        if (visited[city]) {
            return false;
        }

        visited[city] = true;
        recStack[city] = true;
        List<Integer> connectedCities = roadNetwork.get(city);

        boolean foundCycle = false;

        if (connectedCities != null) {
            for (int connectedCity : connectedCities) {
                if (isCyclicUtil(connectedCity, visited, recStack, roadNetwork, trapCities)) {
                    foundCycle = true;
                    if (!trapCities.contains(connectedCity)) {
                        trapCities.add(connectedCity);
                    }
                }
            }
        }

        recStack[city] = false;
        return foundCycle;
    }

    public void printTraps(List<List<Integer>> traps) {
        System.out.println("Danger exploration conclusions:");

        for (int i = 0; i < traps.size(); i++) {
            System.out.print("Colony " + (i + 1) + ": ");

            List<Integer> trapCities = traps.get(i);
            if (trapCities.isEmpty()) {
                System.out.println("Safe");
            } else {
                System.out.print("Dangerous. Cities on the dangerous path: ");
                System.out.print("[");

                // Print the cities on the dangerous path, sorted in ascending order
                for (int j = 0; j < trapCities.size(); j++) {
                    if (j != 0) {
                        System.out.print(", ");
                    }
                    System.out.print(trapCities.get(j));
                }
                System.out.println("]");
            }
        }
    }

}
