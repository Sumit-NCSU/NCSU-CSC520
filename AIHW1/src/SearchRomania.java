import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * @author Sumit (UnityID:-ssrivas8)
 *
 */
public class SearchRomania {

	private static String[][] edges = { { "oradea", "zerind" }, { "zerind", "arad" }, { "arad", "timisoara" },
			{ "timisoara", "lugoj" }, { "lugoj", "mehadia" }, { "dobreta", "mehadia" }, { "oradea", "sibiu" },
			{ "arad", "sibiu" }, { "dobreta", "craiova" }, { "sibiu", "rimnicu_vilcea" }, { "sibiu", "fagaras" },
			{ "rimnicu_vilcea", "craiova" }, { "pitesti", "craiova" }, { "rimnicu_vilcea", "pitesti" },
			{ "bucharest", "pitesti" }, { "bucharest", "fagaras" }, { "bucharest", "giurgiu" },
			{ "bucharest", "urziceni" }, { "vaslui", "urziceni" }, { "hirsova", "urziceni" }, { "hirsova", "eforie" },
			{ "vaslui", "iasi" }, { "neamt", "iasi" } };
	private static String searchtype, srccityname, destcityname;
	private static List<String> visitedCities = new ArrayList<String>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args != null && args.length != 3) {
			System.out.println("Incorrect Input.");
		} else {
			searchtype = args[0].toLowerCase();
			srccityname = args[1].toLowerCase();
			destcityname = args[2].toLowerCase();
			if ("dfs".equalsIgnoreCase(searchtype)) {
				if (dfs(srccityname, destcityname)) {
					for (String city : visitedCities) {
						System.out.print(city + " - ");
					}
					System.out.print(destcityname + "\n");
					System.out.println("Path length: " + visitedCities.size());
				} else {
					System.out.println("Path not found");
				}
			} else if ("bfs".equalsIgnoreCase(searchtype)) {
				if (bfs(srccityname, destcityname)) {
					for (String city : visitedCities) {
						System.out.print(city + " - ");
					}
					System.out.print(destcityname + "\n");
					System.out.println("Path length: " + visitedCities.size());
				} else {
					System.out.println("Path not found");
				}
			} else {
				System.out.println("Incorrect Input.");
			}
		}
	}

	private static boolean bfs(String startCity, String endCity) {
		Queue<String> bfsQueue = new LinkedList<String>();
		bfsQueue.add(startCity); // add city to queue
		visitedCities.add(startCity); // visited the city
		while (!bfsQueue.isEmpty()) {
			String city = bfsQueue.remove();
			String childCity = null;
			while ((childCity = getChildNode(city)) != null) {
				if (childCity.equalsIgnoreCase(endCity)) {
					return true;
				}
				visitedCities.add(childCity); // visited the city
				bfsQueue.add(childCity); // add city to queue
			}
		}
		return false;
	}

	private static boolean dfs(String startCity, String endCity) {
		Stack<String> dfsStack = new Stack<String>();
		dfsStack.push(startCity); // add city to stack
		visitedCities.add(startCity); // visited the city
		while (!dfsStack.isEmpty()) {
			String stackTopNode = dfsStack.peek();
			String childCity = getChildNode(stackTopNode);
			if (childCity == null) {
				dfsStack.pop(); // remove city from stack
			} else {
				if (childCity.equalsIgnoreCase(endCity)) {
					return true;
				}
				visitedCities.add(childCity); // marked city as visited
				dfsStack.push(childCity); // add city to stack
			}
		}
		return false;
	}

	private static String getChildNode(String currentCity) {
		List<String[]> currentCityEdges = new ArrayList<String[]>();
		for (String[] edge : edges) {
			if (currentCity.equalsIgnoreCase(edge[0])) {
				currentCityEdges.add(edge);
			} else if (currentCity.equalsIgnoreCase(edge[1])) {
				currentCityEdges.add(new String[] { edge[1], edge[0] });
			}
		}
		for (String[] edge : currentCityEdges) {
			if (!visitedCities.contains(edge[1])) {
				return edge[1];
			}
		}
		return null;
	}
}