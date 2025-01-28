// O. Bittel;
// 01.04.2021

package shortestPath;

import directedGraph.*;
import sim.SYSimulation;

import java.util.*;

// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {
	
	SYSimulation sim = null;
	
	Map<V,Double> dist; 		// Distanz für jeden Knoten
	Map<V,V> pred; 				// Vorgänger für jeden Knoten
	IndexMinPQ<V,Double> cand; 	// Kandidaten als PriorityQueue PQ
	DirectedGraph<V> graph;
	Heuristic<V> heur;
	V start;
	V end;
	List<V> test;
	// ...

	/**
	 * Konstruiert ein Objekt, das im Graph g k&uuml;rzeste Wege 
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch 
	 * mit dem Dijkstra-Verfahren.
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 * dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		dist = new HashMap<>();
		pred = new HashMap<>();
		cand = new IndexMinPQ<>();
		graph = g;
		heur = h;
	}

	/**
	 * Diese Methode sollte nur verwendet werden, 
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p><blockquote><pre>
	 *    if (sim != null)
	 *       sim.visitStation((Integer) v, Color.blue);
	 * </pre></blockquote>
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {		//habens von void zu boolean geändert
		dist = new HashMap<>();
		pred = new HashMap<>();
		cand = new IndexMinPQ<>();
		test = new LinkedList<>();
		start = s;
		end = g;
		for(V v : graph.getVertexSet()){
			dist.put(v, Double.POSITIVE_INFINITY);
			pred.put(v, null);
		}
		dist.put(s, 0.0);
		cand.add(s,0 + heurCalc(s, g));
		while(!cand.isEmpty()){
			V v = cand.removeMin();
			if(!test.contains(v)){
				test.add(v);
			}
			//System.out.printf("Besuche Knoten %s mit d = %f\n", v.toString(), dist.get(v));
			if(v.equals(g)){
				return;
			}
			for(V w : graph.getSuccessorVertexSet(v)){
				if(dist.get(w) == Double.POSITIVE_INFINITY){
					pred.put(w, v);
					dist.put(w, dist.get(v) + graph.getWeight(v,w));
					cand.add(w, dist.get(w) + heurCalc(w, g));
				}else if(dist.get(v) +graph.getWeight(v,w) < dist.get(w)){
					pred.put(w,v);
					dist.put(w, dist.get(v) + graph.getWeight(v,w));
					cand.change(w, dist.get(w) + heurCalc(w, g));
				}
			}
		}
	}

	private double heurCalc(V v, V w){
		if(heur == null){
			return 0.0;
		}
		return heur.estimatedCost(v,w);
	}

	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		if(pred.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		LinkedList<V> ret = new LinkedList<>();
		V tmp = end;
		ret.add(end);
		for (int i = 0; i < pred.size(); ++i){
			tmp = pred.get(tmp);
			ret.add(tmp);
			if(tmp.equals(start)){
				break;
			}
		}
		Collections.reverse(ret);
		return ret;
	}

	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
		return dist.get(end);
	}

}
