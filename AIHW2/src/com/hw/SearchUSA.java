package com.hw;

import java.io.BufferedReader;
import java.io.File;
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

public class SearchUSA {

	private static String searchtype, srccityname, destcityname;
	private static Map<String, City> cities = new HashMap<String, City>();
	private static List<Road> roads = new ArrayList<Road>();
	protected static List<String> expandedNodes = new ArrayList<String>();

	public static void main(String[] args) {
		try {
			init();
			boolean flag = true;
			Path p = null;
			if (args != null && args.length != 3) {
				System.out.println("Incorrect Input.");
			} else {
				searchtype = args[0].toLowerCase();
				srccityname = args[1].toLowerCase();
				destcityname = args[2].toLowerCase();
				if (srccityname == null || destcityname == null || srccityname.length() == 0
						|| destcityname.length() == 0) {
					System.out.println("Incorrect Input.");
				} else if (srccityname.equalsIgnoreCase(destcityname)) {
					System.out.println(srccityname);
				} else if ("astar".equalsIgnoreCase(searchtype)) {
					AStar as = new AStar();
					expandedNodes.clear();
					p = as.search(srccityname, destcityname);
				} else if ("greedy".equalsIgnoreCase(searchtype)) {
					Greedy g = new Greedy();
					expandedNodes.clear();
					p = g.search(srccityname, destcityname);
				} else if ("uniform".equalsIgnoreCase(searchtype)) {
					Uniform u = new Uniform();
					expandedNodes.clear();
					p = u.search(srccityname, destcityname);
				} else {
					System.out.println("Incorrect Input.");
					flag = false;
				}
				if (flag) {
					if (p == null) {
						System.out.println("Path not found");
					} else {
						// A comma separated list of expanded nodes;
						String op1 = "";
						for (String node : expandedNodes) {
							op1 = op1.concat(", " + node);
						}
						System.out.println(op1.replaceFirst(", ", "").trim());
						// The number of nodes expanded;
						System.out.println("number of nodes expanded: " + expandedNodes.size());
						// A comma-separated list of nodes in the solution path;
						System.out.println(p.toString());
						// The number of nodes in the path;
						System.out.println("Number of nodes in path: " + p.pathLength());
						// The total distance from A to B in the solution path.
						System.out.println("Total cost from " + srccityname + " to " + destcityname + " = "
								+ getTotalPathDistance(p));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int getTotalPathDistance(Path p) {
		int totalCost = 0;
		for (int i = 0; i < p.citiesInPath.size() - 1; i++) {
			for (Road r : roads) {
				if (r.city1.equalsIgnoreCase(p.citiesInPath.get(i))
						&& r.city2.equalsIgnoreCase(p.citiesInPath.get(i + 1))) {
					totalCost += r.cost;
				}
			}
		}
		return totalCost;
	}

	protected static boolean checkGoal(Path path, String destination) {
		return destination.equalsIgnoreCase(path.getLastCityInPath());
	}

	protected static Path[] extendNode(Path p) {
		Path[] paths = new Path[50];
		int i = 0;
		String cityToExtend = p.getLastCityInPath();
		for (Road r : roads) {
			if (cityToExtend.equalsIgnoreCase(r.city1)) {
				Path p2 = new Path(p);
				p2.addCity(r.city2, r.cost);
				paths[i] = p2;
				i++;
			}
		}
		return paths;
	}

	protected double heuristicEstimate(String c1, String c2) {
		City city1 = cities.get(c1);
		City city2 = cities.get(c2);
		return city1.getHDistance(city2);
	}

	public static void init() throws IOException {
		FileInputStream fis = null;
		BufferedReader br = null;
		try {
			String basePath = new File("").getAbsolutePath();
			fis = new FileInputStream(basePath.concat("\\src\\com\\hw\\usroads.pl"));
			br = new BufferedReader(new InputStreamReader(fis));
			String line;
			while ((line = br.readLine()) != null) {
				line = line.replaceFirst("\\(", " ").replaceFirst("\\)", " ").trim();
				if (line.startsWith("road")) {
					line = line.replaceFirst("road", "").trim();
					String tokens[] = line.split(",");
					roads.add(new Road(tokens[0].trim(), tokens[1].trim(), Double.valueOf(tokens[2])));
					roads.add(new Road(tokens[1].trim(), tokens[0].trim(), Double.valueOf(tokens[2])));
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

class AStar extends SearchUSA {

	Comparator<Path> comparator = new PathCostComparator();
	Queue<Path> pQueue = new PriorityQueue<Path>(500, comparator);

	public Path search(String src, String dest) {
		// Initialize a priority queue of paths with the one-node path consisting of the initial state
		pQueue.add(new Path(src, 0));
		// While (queue not empty)
		while (!pQueue.isEmpty()) {
			// Remove path at root (which will be of min cost)
			Path path = pQueue.remove();
			if (expandedNodes.contains(path.getLastCityInPath())) {
				continue;// if it is already visited, then don't visit it again.
			} else {
				expandedNodes.add(path.getLastCityInPath());
				if (checkGoal(path, dest)) {
					// If last node on path matches goal, return path
					return path;
				} else {
					// Else extend the path by one node in all possible ways, by generating successors of the last node on the path
					Path[] paths = extendNode(path);
					// Foreach successor path succ
					for (Path p : paths) {//path already ontains the updated cost(g).
						if (p != null) {
							// Heuristically estimate remaining distance (h-hat) to goal from last node on succ
							double h = heuristicEstimate(p.citiesInPath.get(p.pathLength() - 1),
									p.citiesInPath.get(p.pathLength() - 2));
							// Insert succ on queue and re-heapify using (g) and
							// (h-hat) as the priority
							p.cost += h;							
							boolean addPath = true;
							for (Path queuePath : pQueue) {
								//discarding sub-optimal solutions
								if (queuePath.getLastCityInPath().equalsIgnoreCase(p.getLastCityInPath())) {
									//there is already a path to that city. check its cost.
									addPath = false;
									if (queuePath.cost > p.cost) {
										//the new path needs to be added only if its cost is better than the old path.
										pQueue.remove(queuePath);
										addPath = true;
										break;
									}
								}
							}
							if (addPath) {
								pQueue.add(p);
							}							
						}
					}
					
				}
			}
		}
		return null;
	}

}

class Greedy extends SearchUSA {
	Comparator<Path> comparator = new PathCostComparator();
	Queue<Path> pQueue = new PriorityQueue<Path>(500, comparator);

	public Path search(String src, String dest) {
		// Initialize a priority queue of paths with the one-node path consisting of the initial state
		pQueue.add(new Path(src, 0));
		// While (queue not empty)
		while (!pQueue.isEmpty()) {
			// Remove path at root (which will be of min cost)
			Path path = pQueue.remove();
			if (expandedNodes.contains(path.getLastCityInPath())) {
				continue;// if it is already visited, then dont visit it again.
			} else {
				expandedNodes.add(path.getLastCityInPath());
				if (checkGoal(path, dest)) {
					// If last node on path matches goal, return path
					return path;
				} else {
					// Else extend the path by one node in all possible ways, by generating successors of the last node on the path
					Path[] paths = extendNode(path);
					// Foreach successor path succ
					for (Path p : paths) {
						if (p != null) {
							// Heuristically estimate remaining distance (h-hat) to goal from last node on succ
							double estimate = heuristicEstimate(p.citiesInPath.get(p.pathLength() - 1),
									p.citiesInPath.get(p.pathLength() - 2));
							// Insert succ on queue and re-heapify using (h-hat) as the priority
							p.cost = estimate;
							pQueue.add(p);
						}
					}
				}
			}
		}
		// Return FAIL
		return null;
	}
}

class Uniform extends SearchUSA {
	Comparator<Path> comparator = new PathCostComparator();
	Queue<Path> pQueue = new PriorityQueue<Path>(500, comparator);

	public Path search(String src, String dest) {
		// Initialize a priority queue of paths with the one-node path consisting of the initial state
		pQueue.add(new Path(src, 0));
		// While (queue not empty)
		while (!pQueue.isEmpty()) {
			// Remove path at root (which will be of min cost)
			Path path = pQueue.remove();
			if (expandedNodes.contains(path.getLastCityInPath())) {
				continue;// if it is already visited, then dont visit it again.
			} else {
				expandedNodes.add(path.getLastCityInPath());
				if (checkGoal(path, dest)) {
					// If last node on path matches goal, return path
					return path;
				} else {
					// Else extend the path by one node in all possible ways, by generating successors of the last node on the path
					Path[] paths = extendNode(path);
					// Foreach successor path succ
					for (Path p : paths) { //path already contains the updated cost to succ.
						if (p != null) {
							// Insert succ on queue and re-heapify using PATH COST FROM START NODE as the priority
							boolean addPath = true;
							for (Path queuePath : pQueue) {
								//discarding sub-optimal solutions
								if (queuePath.getLastCityInPath().equalsIgnoreCase(p.getLastCityInPath())) {
									//there is already a path to that city. check its cost.
									addPath = false;
									if (queuePath.cost > p.cost) {
										//the new path needs to be added only if its cost is better than the old path.
										pQueue.remove(queuePath);
										addPath = true;
										break;
									}
								}
							}
							if (addPath) {
								pQueue.add(p);
							}
						}
					}					
				}
			}
		}
		// Return FAIL
		return null;
	}

}

class City {
	String cityName;
	double lat;
	double lon;

	public City(String cityName, double lat, double lon) {
		this.cityName = cityName;
		this.lat = lat;
		this.lon = lon;
	}

	public double getHDistance(City city2) {
		return Math.sqrt(Math.pow(69.5 * (this.lat - city2.lat), 2)
				+ Math.pow(69.5 * Math.cos((this.lat + city2.lat) / 360 * Math.PI) * (this.lon - city2.lon), 2));
	}

	public void printCity() {
		System.out.println(this.cityName + " ");
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

	public void printRoad() {
		System.out.println(this.city1 + "--(" + this.cost + ")--" + this.city2);
	}
}

class Path {
	List<String> citiesInPath;
	double cost;

	public Path(String cityName, double cost) {
		this.citiesInPath = new ArrayList<String>();
		this.citiesInPath.add(cityName);
		this.cost = cost;
	}

	public Path(Path p) {
		this.citiesInPath = new ArrayList<String>();
		for (String s : p.citiesInPath) {
			this.citiesInPath.add(s);
		}
		this.cost = p.cost;
	}

	public String toString() {
		String returnVal = "";
		if (this.citiesInPath != null) {
			for (String city : citiesInPath) {
				returnVal = returnVal.concat(", " + city);
			}
		}
		return returnVal.replaceFirst(", ", "").trim();
	}

	public void printPath() {
		if (this.citiesInPath != null) {
			for (String city : this.citiesInPath) {
				System.out.print(city + " ");
			}
		}
	}

	public int pathLength() {
		if (this.citiesInPath != null) {
			return this.citiesInPath.size();
		}
		return 0;
	}

	public void addCity(String city, double cost) {
		if (this.citiesInPath != null) {
			this.citiesInPath.add(city);
			this.cost += cost;
		}
	}

	public String getLastCityInPath() {
		return this.citiesInPath.get(this.citiesInPath.size() - 1);
	}

}