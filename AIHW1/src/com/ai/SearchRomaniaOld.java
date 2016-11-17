package com.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * @author Sumit (UnityID:-ssrivas8)
 *
 */
public class SearchRomaniaOld {

	private static String[][] edges = { { "oradea", "zerind" }, { "zerind", "arad" }, { "arad", "timisoara" },
			{ "timisoara", "lugoj" }, { "lugoj", "mehadia" }, { "dobreta", "mehadia" }, { "oradea", "sibiu" },
			{ "arad", "sibiu" }, { "dobreta", "craiova" }, { "sibiu", "rimnicu_vilcea" }, { "sibiu", "fagaras" },
			{ "rimnicu_vilcea", "craiova" }, { "pitesti", "craiova" }, { "rimnicu_vilcea", "pitesti" },
			{ "bucharest", "pitesti" }, { "bucharest", "fagaras" }, { "bucharest", "giurgiu" },
			{ "bucharest", "urziceni" }, { "vaslui", "urziceni" }, { "hirsova", "urziceni" }, { "hirsova", "eforie" },
			{ "vaslui", "iasi" }, { "neamt", "iasi" } };
	private static String searchtype, srccityname, destcityname;
	private static List<String> cities = Arrays.asList("arad", "bucharest", "craiova", "dobreta", "eforie", "fagaras",
			"giurgiu", "hirsova", "iasi", "lugoj", "mehadia", "neamt", "oradea", "pitesti", "rimnicu_vilcea", "sibiu",
			"timisoara", "urziceni", "vaslui", "zerind");
	private static int[] visited = new int[20];
	private static List<Node> nodes;
	private static boolean[][] adjacencyMatrix = new boolean[20][20];
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
			initializeGraph();
			switch (searchtype) {
			case "dfs":
				dfs(srccityname, destcityname);
				break;
			case "bfs":
				bfs(srccityname, destcityname);
				break;
			default:
				System.out.println("Incorrect Input.");
				break;
			}
		}
	}

	private static void initializeGraph() {
		for (String city : cities) {
			nodes.add(new Node(city));
		}
		for (String[] edge : edges) {

		}
		// adjacencyMatrix[0][15] = adjacencyMatrix[15][0] = true;
		// adjacencyMatrix[0][16] = adjacencyMatrix[16][0] = true;
		// adjacencyMatrix[0][19] = adjacencyMatrix[19][0] = true;
		// adjacencyMatrix[1][5] = adjacencyMatrix[5][1] = true;
		// adjacencyMatrix[1][6] = adjacencyMatrix[6][1] = true;
		// adjacencyMatrix[1][13] = adjacencyMatrix[13][1] = true;
		// adjacencyMatrix[1][17] = adjacencyMatrix[17][1] = true;
		// adjacencyMatrix[2][3] = adjacencyMatrix[3][2] = true;
		// adjacencyMatrix[2][13] = adjacencyMatrix[13][2] = true;
		// adjacencyMatrix[2][14] = adjacencyMatrix[14][2] = true;
		// adjacencyMatrix[3][10] = adjacencyMatrix[10][3] = true;
		// adjacencyMatrix[4][7] = adjacencyMatrix[7][4] = true;
		// adjacencyMatrix[5][15] = adjacencyMatrix[15][5] = true;
		// adjacencyMatrix[7][17] = adjacencyMatrix[17][7] = true;
		// adjacencyMatrix[8][11] = adjacencyMatrix[11][8] = true;
		// adjacencyMatrix[8][18] = adjacencyMatrix[18][8] = true;
		// adjacencyMatrix[9][10] = adjacencyMatrix[10][9] = true;
		// adjacencyMatrix[9][16] = adjacencyMatrix[16][9] = true;
		// adjacencyMatrix[12][15] = adjacencyMatrix[15][12] = true;
		// adjacencyMatrix[12][19] = adjacencyMatrix[19][12] = true;
		// adjacencyMatrix[13][14] = adjacencyMatrix[14][13] = true;
		// adjacencyMatrix[14][15] = adjacencyMatrix[15][14] = true;
		// adjacencyMatrix[17][18] = adjacencyMatrix[18][17] = true;
	}

	private static void bfs(String startCity, String endCity) {
		Queue<String> bfsQueue = new LinkedList<String>();
		bfsQueue.add(startCity); // add city to queue
		visitedCities.add(startCity); // visited the city
		visited[cities.indexOf(startCity)] = 1; // marked city as visited
		while (!bfsQueue.isEmpty()) {
			String city = bfsQueue.remove();
			String childCity = null;
			while ((childCity = getChildNode(city)) != null) {
				visitedCities.add(childCity); // visited the city
				visited[cities.indexOf(childCity)] = 1; // marked city as
														// visited
				bfsQueue.add(childCity); // add city to queue
			}
		}
	}

	private static void dfs(String startCity, String endCity) {
		Stack<String> dfsStack = new Stack<String>();
		dfsStack.push(startCity); // add city to stack
		visitedCities.add(startCity); // visited the city
		visited[cities.indexOf(startCity)] = 1; // marked city as visited
		while (!dfsStack.isEmpty()) {
			String stackTopNode = dfsStack.peek();
			String childCity = getChildNode(stackTopNode);
			if (childCity == null) {
				dfsStack.pop(); // remove city from stack
			} else {
				visited[cities.indexOf(startCity)] = 1; // visited the city
				visitedCities.add(childCity); // marked city as visited
				dfsStack.push(childCity); // add city to stack
			}
		}
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

	static class Graph {
		private Node rootNode;

		public Node getRootNode() {
			return rootNode;
		}

		public void setRootNode(Node rootNode) {
			this.rootNode = rootNode;
		}

		public void addEdge(Node s, Node d) {
			adjacencyMatrix[nodes.indexOf(s)][nodes.indexOf(d)] = true;
			adjacencyMatrix[nodes.indexOf(d)][nodes.indexOf(s)] = true;
		}

		public Graph() {

		}
	}

	static class Node {
		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		boolean visited;

		public Node(String value) {
			this.value = value;
			this.visited = false;
		}
	}
}