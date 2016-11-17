package com.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFS {

	private static String input = "(oradea,zerind).(zerind,arad).(arad,timisoara).(timisoara,lugoj).(lugoj,mehadia).(dobreta,mehadia).(oradea,sibiu).(arad,sibiu).(dobreta,craiova).(sibiu,rimnicu_vilcea).(sibiu,fagaras).(rimnicu_vilcea,craiova).(pitesti,craiova).(rimnicu_vilcea,pitesti).(bucharest,pitesti).(bucharest,fagaras).(bucharest,giurgiu).(bucharest,urziceni).(vaslui,urziceni).(hirsova,urziceni).(hirsova,eforie).(vaslui,iasi).(neamt,iasi).";

	// public void bfs() {
	// Queue<TreeNode> q = new LinkedList<TreeNode>();
	// q.add(rootNode);
	// visited[rootNode] = true;
	// printNode(rootNode);
	// while (!q.isEmpty()) {
	// int n, child;
	// n = (q.peek()).intValue();
	// child = getUnvisitedChildNode(n); // Returns -1 if no unvisited node left
	// if (child != -1) { // Found an unvisited node
	// visited[child] = true; // Mark as visited
	// printNode(child);
	// q.add(child); // Add to queue
	// } else {
	// q.remove(); // Process next node
	// }
	// }
	// }

	public static void main(String[] args) {
		String paths[] = input.split("\\."); // (oradea,zerind) (zerind,arad)
		for (String path : paths) {
			String cities[] = path.replaceFirst("\\(","").replaceFirst("\\)", "").split(",");
			for (String city : cities){
				System.out.println(city);
			}
		}
	}

	private void parseInput() {

	}

	private void initializeTree() {
		TreeNode romaniaTree = new TreeNode("oradea");
		String paths[] = input.split("\\."); // (oradea,zerind) (zerind,arad)
		for (String path : paths) {
			String cities[] = path.replaceFirst("\\(","").replaceFirst("\\)", "").split(",");
			for (String city : cities){
				System.out.println(city);
			}
		}
		romaniaTree.children.add(new TreeNode("zerind"));
	}

	private class TreeNode {
		String value;
		List<TreeNode> children;

		public TreeNode(String value) {
			this.value = value;
			this.children = new ArrayList<TreeNode>();
		}
	}
}
