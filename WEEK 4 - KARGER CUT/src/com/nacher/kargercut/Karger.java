package com.nacher.kargercut;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Karger {

	// This method performs the Karger Minimum Cut algorithm on the given graph
	private static int processKargerMinimumCutAlgorithm(HashMap<Integer, ArrayList<Integer>> graph) {
		// Iterate until there are only two nodes
		while (graph.size() > 2) {
			processKargerAlgorithmStep(graph);
		}

		// Return the Minimum Cut (the number of edges of both nodes is the
		// same)
		return graph.get((Integer) graph.keySet().toArray()[0]).size();
	}

	// This method performs a single step in the algorithm
	private static void processKargerAlgorithmStep(HashMap<Integer, ArrayList<Integer>> graph) {
		// Choose random items
		List<Integer> randomItems = chooseRandomItems(graph);

		int firstItem = randomItems.get(0);
		int secondItem = randomItems.get(1);

		ArrayList<Integer> firstItemList = graph.get(firstItem);
		ArrayList<Integer> secondItemList = graph.get(secondItem);

		// Add second list items to first list
		firstItemList.addAll(secondItemList);

		// Remove second list items, the edges
		graph.remove(randomItems.get(1));

		// Replace second item appearances by first item
		Iterator<Integer> it = graph.keySet().iterator();

		while (it.hasNext()) {
			int currentKey = (Integer) it.next();

			ArrayList<Integer> processItemList = graph.get(currentKey);

			for (int i : processItemList) {
				if (i == secondItem) {
					processItemList.set(processItemList.indexOf(i), firstItem);
				}
			}
		}

		// Remove all repeat elements to prevent loops
		ArrayList<Integer> itemsToRemove = new ArrayList<Integer>();

		for (int i : firstItemList) {
			if (i == firstItem) {
				itemsToRemove.add(i);
			}
		}

		firstItemList.removeAll(itemsToRemove);
	}

	// This method chooses a node and one of the edges of that node at random
	private static List<Integer> chooseRandomItems(HashMap<Integer, ArrayList<Integer>> graph) {
		ArrayList<Integer> randomItems = new ArrayList<Integer>();

		int nodeIndex = (int) (Math.random() * graph.keySet().size());
		Integer randomNode = (Integer) (graph.keySet().toArray()[nodeIndex]);

		int edgeIndex = (int) (Math.random() * graph.get(randomNode).size());
		Integer randomEdge = graph.get(randomNode).get(edgeIndex);

		randomItems.add(randomNode);
		randomItems.add(randomEdge);

		return randomItems;
	}

	// This method returns a copy of the graph that will be operated with
	private static HashMap<Integer, ArrayList<Integer>> copyGraph(HashMap<Integer, ArrayList<Integer>> graph) {
		HashMap<Integer, ArrayList<Integer>> graphCopy = new HashMap<Integer, ArrayList<Integer>>();

		Iterator<Integer> it = graph.keySet().iterator();

		while (it.hasNext()) {
			Integer currentKey = (Integer) it.next();
			ArrayList<Integer> currentItemList = graph.get(currentKey);

			graphCopy.put(currentKey, new ArrayList<Integer>(currentItemList));
		}

		return graphCopy;
	}

	// Reads the graph and translates it into a hashmap
	private static HashMap<Integer, ArrayList<Integer>> readGraphFromFile() {
		HashMap<Integer, ArrayList<Integer>> graph = new HashMap<Integer, ArrayList<Integer>>();

		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream("C:\\Users\\Sergio Nacher\\Documents\\Java\\WEEK 4 - KARGER CUT\\src\\com\\nacher\\kargercut\\kargerMinCut.txt");

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line;
			while ((line = br.readLine()) != null) {
				// process the line
				StringTokenizer tokens = new StringTokenizer(line);
				ArrayList<Integer> edges = new ArrayList<Integer>();

				// first item is the token
				Integer node = new Integer(tokens.nextToken());

				while (tokens.hasMoreTokens()) {
					edges.add(new Integer(tokens.nextToken()));
				}

				graph.put(node, edges);
			}

			br.close();
		} catch (FileNotFoundException ex) {
			System.out.println("File no found!");
		} catch (IOException ex) {
			Logger.getLogger(Karger.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				fstream.close();
			} catch (IOException ex) {
				Logger.getLogger(Karger.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return graph;
	}

	public static void main(String[] args) {
		// Read the graph from the text file
		HashMap<Integer, ArrayList<Integer>> originalGraph = readGraphFromFile();

		int minCut = 0;

		for (int i = 0; i < 100; i++) {
			// Copy the original graph in each iteration
			HashMap<Integer, ArrayList<Integer>> copyGraph = copyGraph(originalGraph);

			int result = processKargerMinimumCutAlgorithm(copyGraph);

			if (minCut == 0) {
				minCut = result;
			} else {
				if (result < minCut) {
					minCut = result;
				}
			}
			// I will display the result of each individual iteration to prove
			// it works as intended i.e. randomly
			// while still giving an adequate final result
			// System.out.println("Partial Result: " + result);
		}

		System.out.println("The Minimum Cut is " + minCut);
	}

}
