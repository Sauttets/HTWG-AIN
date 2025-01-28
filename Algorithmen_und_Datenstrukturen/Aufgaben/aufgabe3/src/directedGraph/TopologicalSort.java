// O. Bittel;
// 22.02.2017

package directedGraph;

import java.util.*;

/**
 * Klasse zur Erstellung einer topologischen Sortierung.
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class TopologicalSort<V> {
    private List<V> ts = new LinkedList<>(); // topologisch sortierte Folge

	/**
	 * Führt eine topologische Sortierung für g durch.
	 * @param g gerichteter Graph.
	 */
	public TopologicalSort(DirectedGraph<V> g) {
		topSort(g);
    }

	public List<V> topSort(DirectedGraph<V> g)
	{
		Map<V, Integer> inDegree = new TreeMap<>();
		LinkedList<V> q = new LinkedList<>();

		for(V x : g.getVertexSet())
		{
			inDegree.put(x, g.getPredecessorVertexSet(x).size());
			if(inDegree.get(x) == 0)
			{
				q.add(x);
			}
		}

		while(!q.isEmpty())
		{
			V v = q.remove(0);
			ts.add(v);
			for (V x : g.getSuccessorVertexSet(v))
			{
				int val = inDegree.get(x);
				inDegree.put(x, val-1);
				if(inDegree.get(x) == 0)
				{
					q.add(x);
				}
			}
		}
		if(ts.size() != g.getNumberOfVertexes())
		{
			return null;
		}else{
			return ts;
		}
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
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
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

		DirectedGraph<String> morgens = new AdjacencyListDirectedGraph<>();
		morgens.addEdge("Unterhose","Hose");
		//morgens.addEdge("Schal", "Hose");
		morgens.addEdge("Hose","Gürtel");
		morgens.addEdge("Hose","Schuhe");

		morgens.addEdge("Socken","Schuhe");

		morgens.addEdge("Unterhemd","Hemd");
		morgens.addEdge("Hemd","Pulli");
		morgens.addEdge("Pulli", "Mantel");

		morgens.addEdge("Gürtel","Mantel");
		morgens.addEdge("Pulli","Mantel");

		morgens.addEdge("Mantel","Schal");

		morgens.addEdge("Schuhe","Handschuhe");
		morgens.addEdge("Schal","Handschuhe");
		morgens.addEdge("Mütze","Handschuhe");
		TopologicalSort<String> mor = new TopologicalSort<>(morgens);
		System.out.println(mor.topologicalSortedList());
	}
}
