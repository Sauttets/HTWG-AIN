package shortestPath;

import directedGraph.*;
import java.io.FileNotFoundException;
import sim.SYSimulation;
import java.awt.Color;
import java.io.IOException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * Kürzeste Wege im Scotland-Yard Spielplan mit A* und Dijkstra.
 * @author Oliver Bittel
 * @since 27.02.2019
 */
public class ScotlandYard {

	/**
	 * Fabrikmethode zur Erzeugung eines gerichteten Graphens für den Scotland-Yard-Spielplan.
	 * <p>
	 * Liest die Verbindungsdaten von der Datei ScotlandYard_Kanten.txt.
	 * Für die Verbindungen werden folgende Gewichte angenommen:
	 * U-Bahn = 5, Taxi = 2 und Bus = 3.
	 * Falls Knotenverbindungen unterschiedliche Beförderungsmittel gestatten,
	 * wird das billigste Beförderungsmittel gewählt. 
	 * Bei einer Vebindung von u nach v wird in den gerichteten Graph sowohl 
	 * eine Kante von u nach v als auch von v nach u eingetragen.
	 * @return Gerichteter und Gewichteter Graph für Scotland-Yard.
	 * @throws FileNotFoundException
	 */
	public static DirectedGraph<Integer> getGraph() throws FileNotFoundException {

		DirectedGraph<Integer> sy_graph = new AdjacencyListDirectedGraph<>();
		Scanner in = new Scanner(new File(""));

		while(in.hasNext()) {
			String tmp = in.nextLine();
			//System.out.println(tmp);
			String[] tmp2 = tmp.split(" ");
			switch (tmp2[2]){
				case "Taxi":
					tmp2[2] = "2";
					break;
				case "Bus":
					tmp2[2] = "3";
					break;
				case "UBahn":
					tmp2[2] = "5";
					break;
				default:

					break;
			}
			if(sy_graph.containsEdge(Integer.parseInt(tmp2[0]),Integer.parseInt(tmp2[1]))){	//testen ob Kante schon vorhanden ist
				if(sy_graph.getWeight(Integer.parseInt(tmp2[0]),Integer.parseInt(tmp2[1])) < Integer.parseInt(tmp2[2]) && sy_graph.getWeight(Integer.parseInt(tmp2[0]),Integer.parseInt(tmp2[1])) != 0.0){	//testen ob der Alte Wert kleiner dem neuen ist

				} else {	//Alter Wert > Neuer Wert also aktualisieren
					sy_graph.addEdge(Integer.parseInt(tmp2[0]),Integer.parseInt(tmp2[1]),Integer.parseInt(tmp2[2]));
					sy_graph.addEdge(Integer.parseInt(tmp2[1]),Integer.parseInt(tmp2[0]),Integer.parseInt(tmp2[2]));
				}
			} else {	//Kante noch nicht vorhanden, also neu einfügen
				sy_graph.addEdge(Integer.parseInt(tmp2[0]),Integer.parseInt(tmp2[1]),Integer.parseInt(tmp2[2]));
				sy_graph.addEdge(Integer.parseInt(tmp2[1]),Integer.parseInt(tmp2[0]),Integer.parseInt(tmp2[2]));
			}
		}

		// Test, ob alle Kanten eingelesen wurden: 
		System.out.println("Number of Vertices:       " + sy_graph.getNumberOfVertexes());	// 199
		System.out.println("Number of directed Edges: " + sy_graph.getNumberOfEdges());	  	// 862
		double wSum = 0.0;
		for (Integer v : sy_graph.getVertexSet())
			for (Integer w : sy_graph.getSuccessorVertexSet(v))
				wSum += sy_graph.getWeight(v,w);
		System.out.println("Sum of all Weights:       " + wSum);	// 1972.0
		
		return sy_graph;
	}


	/**
	 * Fabrikmethode zur Erzeugung einer Heuristik für die Schätzung
	 * der Distanz zweier Knoten im Scotland-Yard-Spielplan.
	 * Die Heuristik wird für A* benötigt.
	 * <p>
	 * Liest die (x,y)-Koordinaten (Pixelkoordinaten) aller Knoten von der Datei
	 * ScotlandYard_Knoten.txt in eine Map ein.
	 * Die zurückgelieferte Heuristik-Funktion estimatedCost
	 * berechnet einen skalierten Euklidischen Abstand.
	 * @return Heuristik für Scotland-Yard.
	 * @throws FileNotFoundException
	 */
	public static Heuristic<Integer> getHeuristic() throws FileNotFoundException {
		return new ScotlandYardHeuristic();
	}

	/**
	 * Scotland-Yard Anwendung.
	 * @param args wird nicht verewendet.
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {

		DirectedGraph<Integer> syGraph = getGraph();
		
		//Heuristic<Integer> syHeuristic = null; // Dijkstra
		Heuristic<Integer> syHeuristic = getHeuristic(); // A*

		ShortestPath<Integer> sySp = new ShortestPath<Integer>(syGraph,syHeuristic);	//A*
		ShortestPath<Integer> sySp2 = new ShortestPath<Integer>(syGraph,null);		//Djikstra

		//Djikstra
		System.out.println("\nDjikstra:");
		sySp2.searchShortestPath(65,157);
		System.out.println("Distance = " + sySp2.getDistance()); // 9.0

		sySp2.searchShortestPath(1,175);
		System.out.println("Distance = " + sySp2.getDistance()); // 25.0

		sySp2.searchShortestPath(1,173);
		System.out.println("Distance = " + sySp2.getDistance()); // 22.0

		//A*
		System.out.println("\nA*:");
		sySp.searchShortestPath(65,157);
		System.out.println("Distance = " + sySp.getDistance()); // 9.0

		sySp.searchShortestPath(1,175);
		System.out.println("Distance = " + sySp.getDistance()); // 25.0

		sySp.searchShortestPath(1,173);
		System.out.println("Distance = " + sySp.getDistance()); // 22.0

		SYSimulation sim;
		try {
			sim = new SYSimulation();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		sySp.setSimulator(sim);
		sySp2.setSimulator(sim);

		int path = 3;			//<===================================================================================================================

		if(path == 1){
			sim.startSequence("Shortest path from 65 to 157");
			sySp.searchShortestPath(65,157); // 9.0
			sySp2.searchShortestPath(65,157); // 9.0
		}else if(path == 2){
			sim.startSequence("Shortest path from 1 to 175");
			sySp.searchShortestPath(1,175); //25.0
			sySp2.searchShortestPath(1,175); //25.0
		}else if(path == 3){
			sim.startSequence("Shortest path from 1 to 173");
			sySp.searchShortestPath(1,173); //22.0
			sySp2.searchShortestPath(1,173); //22.0
		}

		// bei Heuristik-Faktor von 1/10 wird nicht der optimale Pfad produziert.
		// bei 1/30 funktioniert es.

		System.out.println("\nDistance Djikstra = " + sySp.getDistance());
		System.out.println("Distance A* = " + sySp2.getDistance());
		List<Integer> sp = sySp.getShortestPath();
		List<Integer> sp2 = sySp2.getShortestPath();
		List<Integer> besuchteKnoten = sySp.test;
		// List<Integer> besuchteKnoten = sySp2.test;

		for(int x : besuchteKnoten){
			sim.visitStation(x, Color.BLUE);
		}

		int a = -1;
		for (int b : sp) {
			if (a != -1)
			sim.drive(a, b, Color.RED.darker());
			sim.visitStation(b);
			a = b;
		}

		a = -1;
		for (int b : sp2) {
			if (a != -1)
				sim.drive(a, b, Color.GREEN.darker());
			sim.visitStation(b);
			a = b;
		}
        sim.stopSequence();
    }
}

class ScotlandYardHeuristic implements Heuristic<Integer> {
	private Map<Integer,Point> coord = new HashMap<>(); // Ordnet jedem Knoten seine Koordinaten zu

	double faktor = 1.0/90.0;

	private static class Point {
		int x;
		int y;
		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public ScotlandYardHeuristic() throws FileNotFoundException {
		Scanner in = new Scanner(new File("ScotlandYard_Knoten.txt"));

		while(in.hasNext()) {
			String tmp = in.nextLine();
			String[] tmp2 = tmp.split("[\s\t]+");
			coord.put(Integer.parseInt(tmp2[0]), new Point(Integer.parseInt(tmp2[1]),Integer.parseInt(tmp2[2])));
		}
		in.close();
	}

	public double estimatedCost(Integer u, Integer v) {
		Point a = coord.get(u);
		Point b = coord.get(v);
		return faktor * (Math.sqrt(Math.pow((b.x-a.x),2)+ Math.pow((b.y-a.y),2)));
	}
}