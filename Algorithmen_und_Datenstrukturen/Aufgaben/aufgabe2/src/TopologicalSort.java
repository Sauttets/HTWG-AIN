// O. Bittel;
// 26.09.22


import java.util.*;

/**
 * Klasse zur Erstellung einer topologischen Sortierung.
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class TopologicalSort<V> {
	private List<V> ts = new LinkedList<>(); // topologisch sortierte Folge
	// ...

	/**
	 * Führt eine topologische Sortierung für g durch.
	 * @param g gerichteter Graph.
	 */
	public TopologicalSort(DirectedGraph<V> g) {
		tropSort(g);
	}

	private void tropSort(DirectedGraph<V> g) {
		Map<V, Integer> inDegree = new TreeMap<>(); // Anz. noch nicht besuchter Vorgänger
		Queue<V> q = new LinkedList<>(); // Knoten mit inDegree=0

		for (V v : g.getVertexSet()) {
			inDegree.put(v, g.getInDegree(v));
			if (inDegree.get(v) == 0)
				q.add(v);
		}
		while (! q.isEmpty()) {
			V v = q.remove();
			ts.add(v);
			for (V w : g.getSuccessorVertexSet(v)){ //jeden Nachfolger w von v
				inDegree.put(w, inDegree.get(w) - 1);	//Die anzahl der Vorgänger für den Knotem um 1 verringern (put überschreibt Value wenn knoten schon vorhanden)
				if (inDegree.get(w) == 0)
					q.add(w);
			}

		}
		System.out.println(ts.size());
		System.out.println(g.getNumberOfVertexes());
		if (ts.size() != g.getNumberOfVertexes())
			ts = null;
	}

	/**
	 * Liefert eine nicht modifizierbare Liste (unmodifiable view) zurück,
	 * die topologisch sortiert ist.
	 * @return topologisch sortierte Liste
	 */
	public List<V> topologicalSortedList() {
		return Collections.unmodifiableList(ts);
	}


	public static void main(String[] args) {
	/*	DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1, 2);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(3, 5);
		g.addEdge(4, 6);
		g.addEdge(5, 6);
		g.addEdge(6, 7);
		System.out.println(g);

		TopologicalSort<Integer> ts = new TopologicalSort<>(g);

		if (ts.topologicalSortedList() != null) {
			System.out.println(ts.topologicalSortedList()); // [1, 2, 3, 4, 5, 6, 7]
		}
		*/

		anziehen();
	}

	public static void anziehen(){
		System.out.println("");
		DirectedGraph<String> g = new AdjacencyListDirectedGraph<>();
		g.addEdge("Unterhose","Hose");
		g.addEdge("Socken","Schuhe");
		g.addEdge("Hose","Schuhe");
		g.addEdge("Hose","Gürtel");
		g.addEdge("Gürtel","Mantel");
		g.addEdge("Schuhe","Handschuhe");
		g.addEdge("Unterhemd","Hemd");
		g.addEdge("Hemd","Pulli");
		g.addEdge("Pulli","Mantel");
		g.addEdge("Mantel","Schal");
		g.addEdge("Schal","Handschuhe");
		g.addEdge("Mütze","Handschuhe");
//		g.addEdge("Hose","Schal");
		//g.addEdge("Schal","Hose");

		System.out.println(g);
		System.out.println(g.getNumberOfVertexes());
		TopologicalSort<String> ts = new TopologicalSort<>(g);

		if (ts.topologicalSortedList() != null) {
			System.out.println(ts.topologicalSortedList());
		}

	}
}