import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.*;

public class TravelMap {

    // Maps a single Id to a single Location.
    public Map<Integer, Location> locationMap = new HashMap<>();

    // List of locations, read in the given order
    public List<Location> locations = new ArrayList<>();

    // List of trails, read in the given order
    public List<Trail> trails = new ArrayList<>();

    // TODO: You are free to add more variables if necessary.

    public void initializeMap(String filename) {
        // Read the XML file and fill the instance variables locationMap, locations and trails.
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(filename);

            NodeList locationList = document.getElementsByTagName("Location");
            for (int i = 0; i < locationList.getLength(); i++) {
                Node locationNode = locationList.item(i);
                if (locationNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element locationElement = (Element) locationNode;
                    String name = locationElement.getElementsByTagName("Name").item(0).getTextContent();
                    int id = Integer.parseInt(locationElement.getElementsByTagName("Id").item(0).getTextContent());
                    Location location = new Location(name, id);
                    locations.add(location);
                    locationMap.put(id, location);
                }
            }

            NodeList trailList = document.getElementsByTagName("Trail");
            for (int i = 0; i < trailList.getLength(); i++) {
                Node trailNode = trailList.item(i);
                if (trailNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element trailElement = (Element) trailNode;
                    int sourceId = Integer.parseInt(trailElement.getElementsByTagName("SourceId").item(0).getTextContent());
                    int destinationId = Integer.parseInt(trailElement.getElementsByTagName("DestinationId").item(0).getTextContent());
                    int dangerLevel = Integer.parseInt(trailElement.getElementsByTagName("DangerLevel").item(0).getTextContent());
                    Location source = locationMap.get(sourceId);
                    Location destination = locationMap.get(destinationId);
                    Trail trail = new Trail(source, destination, dangerLevel);
                    trails.add(trail);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Trail> getSafestTrails() {
        List<Trail> safestTrails = new ArrayList<>();

        // Initializing the graph
        Set<Location> unvisited = new HashSet<>(locations);
        int[] distance = new int[locations.size()];
        int[] previous = new int[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            distance[i] = Integer.MAX_VALUE;
            previous[i] = -1;
        }
        distance[0] = 0;

        // Finding the shortest path between all pairs of nodes
        while (!unvisited.isEmpty()) {
            int current = getClosestNode(unvisited, distance);
            unvisited.remove(locations.get(current));
            for (Trail trail : getNeighbors(current)) {
                int neighbor = trail.destination.id;
                int weight = trail.danger;
                int tentativeDistance = distance[current] + weight;
                if (tentativeDistance < distance[neighbor]) {
                    distance[neighbor] = tentativeDistance;
                    previous[neighbor] = current;
                }
            }
        }

        // Constructing the safest map
        Set<Location> visited = new HashSet<>();
        visited.add(locations.get(0));
        while (visited.size() < locations.size()) {
            Trail minTrail = null;
            int minWeight = Integer.MAX_VALUE;
            for (Location source : visited) {
                for (Trail trail : trails) {
                    if (trail.source == source){
                        Location destination = trail.destination;
                    if (!visited.contains(destination) && trail.danger < minWeight) {
                        minTrail = trail;
                        minWeight = trail.danger;
                    }
                }
                }
            }
            if (minTrail != null) {
                safestTrails.add(minTrail);
                visited.add(minTrail.destination);
            } else {
                break; // No more trails to add
            }
        }

        return safestTrails;
    }
    private int getClosestNode(Set<Location> unvisited, int[] distance) {
        int minDistance = Integer.MAX_VALUE;
        int closestNode = -1;
        for (Location location : unvisited) {
            int index = location.id
                    ;
            if (distance[index] < minDistance) {
                minDistance = distance[index];
                closestNode = index;
            }
        }
        return closestNode;
    }

    private List<Trail> getNeighbors(int current) {
        List<Trail> neighbors = new ArrayList<>();
        for (Trail trail : trails) {
            if (trail.source.id == current) {
                neighbors.add(trail);
            }
        }
        return neighbors;
    }

    public void printSafestTrails(List<Trail> safestTrails) {
        // Print the given list of safest trails conforming to the given output format.
        // TODO: Your code here
    }
}
