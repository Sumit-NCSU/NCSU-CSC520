package com.astar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Astr {
	
	private static Map<String, City> cities = new HashMap<String, City>();
	private static Map<String, List<Road>> roads = new HashMap<String, List<Road>>();
	private static List<String> expandedNodes = new ArrayList<String>();
	private static Comparator<Path> comp = new PathCostComparator();
	private static Queue<Path> priorityQueue = new PriorityQueue<Path>(500, comp);

	public static void main(String[] args) {
//		try {
//			init();
//			for (String s : roads.keySet()) {
//				System.out.println(s + ":");
//				for (Road r : roads.get(s)) {
//					System.out.println("\t" + r.toString());
//				}
//			}
//			System.out.println("CITIES:");
//			for (String c : cities.keySet()) {
//				System.out.print(c + ", ");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public Path search(String s, String d) {
//		Initialize a priority queue of paths with the one-node path consisting of the initial state
		Path source = new Path(s,getHeuristicEstimate(s,d));
		priorityQueue.add(source);
//		While (queue not empty)
		while (!priorityQueue.isEmpty()) {
//		    Remove path at root (which will be of min cost)
			Path path = priorityQueue.remove();
//			If last node on path matches goal, return path
			if (d.equalsIgnoreCase(path.getEndNode())) {
				return path;
			} else if (expandedNodes.contains(path.getEndNode())) {//city is already visited.
				continue;
			} else {
				expandedNodes.add(path.getEndNode());
//				Else extend the path by one node in all possible ways,
//		        by generating successors of the last node on the path
				Path[] paths = extendPath(path);
//				Foreach successor path succ
				for (Path p : paths) { // path p includes updated cost to successor.
					if (p != null) {
//						Heuristically estimate remaining distance (h-hat) to goal from last node on succ
						double heuristicDistance = getHeuristicEstimate(path.getEndNode(), p.getEndNode());
						p.cost += heuristicDistance;
						priorityQueue.add(p);
					}
				}
			}
//		        Insert succ on queue and re-heapify
//		            using SUM OF PATH COST FROM ROOT (g) AND
//		                ESTIMATED REMAINING DISTANCE TO GOAL (h-hat) as the priority
//		    If two or more paths reach the same node, delete all paths except
//		        the one of min cost
		}
//		Return FAIL
		return null;
	}
	
	public Path[] extendPath(Path path) {
		Path[] paths = new Path[50];
		int i = 0;
		for (Road r : roads.get(path.getEndNode())) {
			Path p = new Path(path);
			p.addCity(r.city2, r.cost);
			paths[i] = p;
			i++;
		}
		return paths;
	}
	
	private double getHeuristicEstimate(String city1, String city2) {
		City c1 = cities.get(city1);
		City c2 = cities.get(city2);
		return Math.sqrt(Math.pow(69.5 * (c1.lat - c2.lat), 2)
				+ Math.pow(69.5 * Math.cos((c1.lat + c2.lat) / 360 * Math.PI) * (c1.lon - c2.lon), 2));
	}
	
	private static void init() throws IOException {
		FileInputStream fis = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream("F:\\Work\\Java\\Workspace\\AIHW2\\src\\com\\hw\\usroads.pl");
			br = new BufferedReader(new InputStreamReader(fis));
			String line;
			while ((line = br.readLine()) != null) {
				line = line.replaceFirst("\\(", " ").replaceFirst("\\)", " ").trim();
				if (line.startsWith("road")) {
					line = line.replaceFirst("road", "").trim();
					String tokens[] = line.split(",");//city1, city2, cost
					String city1 = tokens[0].trim();
					String city2 = tokens[1].trim();
					double cost = Double.valueOf(tokens[2]);
					if (roads.containsKey(city1)) {
						roads.get(city1).add(new Road(city1, city2, cost));
					} else {
						List<Road> outgoingRoads = new ArrayList<Road>();
						outgoingRoads.add(new Road(city1, city2, cost));
						roads.put(city1, outgoingRoads);
					}
				} else if (line.startsWith("city")) {
					line = line.replaceFirst("city", "").trim();
					String tokens[] = line.split(",");
					cities.put(tokens[0].trim(),
							new City(tokens[0].trim(), Double.valueOf(tokens[1]), Double.valueOf(tokens[2])));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}
	
}

class PathCostComparator implements Comparator<Path> {
	@Override
	public int compare(Path n1, Path n2) {
		return (int) (n1.cost - n2.cost);
	}
}

class Path {
	String cities[] = new String[500];
	int pathLength = 0;
	double cost;

	public Path(String city, double cost) {
		this.cities[0] = city;
		this.pathLength++;
		this.cost = cost;
	}
	
	public Path(Path p) {
		int i = 0;
		for (String s : p.cities) {
			this.cities[i] = s;
			i++;
		}
		this.pathLength = p.pathLength;
		this.cost = p.cost;
	}

	public void addCity(String city, double cost) {
		this.cities[this.pathLength - 1] = city;
		this.pathLength++;
		this.cost += cost;
	}
	
	public String getEndNode() {
		return this.cities[this.pathLength-1];
	}
	
	public String toString() {
		String out = "";
		for (String s : cities) {
			out = out.concat(", " + s);
		}
		return out.replaceFirst(", ", "").trim();
	}
}

class Road {
	String city1;
	String city2;
	double cost;
	
	public Road(String city1, String city2, double cost) {
		this.city1 = city1;
		this.city2 = city2;
		this.cost = cost;
	}
	
	public String toString() {
		return city1.concat("---(").concat(String.valueOf(cost)).concat(")---").concat(city2);
	}
}

class City {
	String name;
	double lat;
	double lon;
	
	public City(String name, double lat, double lon) {
		this.name = name;
		this.lat = lat;
		this.lon = lon;
	}
}