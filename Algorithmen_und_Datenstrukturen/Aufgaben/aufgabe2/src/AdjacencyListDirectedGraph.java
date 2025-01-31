// O. Bittel;
// 26.09.2022


import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;

/**
 * Implementierung von DirectedGraph mit einer doppelten TreeMap 
 * für die Nachfolgerknoten und einer einer doppelten TreeMap 
 * für die Vorgängerknoten. 
 * <p>
 * Beachte: V muss vom Typ Comparable&lt;V&gt; sein.
 * <p>
 * Entspicht einer Adjazenzlisten-Implementierung 
 * mit schnellem Zugriff auf die Knoten.
 * @author Oliver Bittel
 * @since 19.03.2018
 * @param <V> Knotentyp.
 */
public class AdjacencyListDirectedGraph<V> implements DirectedGraph<V> {
    // doppelte Map für die Nachfolgerknoten:
    private  Map<V, Map<V, Double>> succ = new TreeMap<>();
    
    // doppelte Map für die Vorgängerknoten:
    private  Map<V, Map<V, Double>> pred = new TreeMap<>();

    private int numberEdge = 0;

	@Override
	public boolean addVertex(V v) {
		//wenn vertex noch nicht vorhanden in passende liste einfügen
		if (!succ.containsKey(v))
			succ.put(v, new TreeMap<>());
		if(!pred.containsKey(v))
			pred.put(v, new TreeMap<>());
		return true;
    }

    @Override
    public boolean addEdge(V start, V end, double weight) {
		addVertex(start);
		addVertex(end);
		if(!succ.get(start).containsKey(end) && !pred.get(end).containsKey(start))
			numberEdge++;
		succ.get(start).put(end,weight);
		pred.get(end).put(start,weight);
		return true;
    }


    @Override
    public boolean addEdge(V start, V end) {
		return addEdge(start , end,1.0);
    }

    @Override
    public boolean containsVertex(V v) {
		// wenn v in succ und pred vorhanden -> true
		return succ.containsKey(v) && pred.containsKey(v);
    }

    @Override
    public boolean containsEdge(V start, V end) {
		//wenn succ einen pred hat und pred einen succ hat -> true
		return succ.get(start).containsKey(end) && pred.get(end).containsKey(start);
    }

    @Override
    public double getWeight(V start, V end) {
		//wenn pred und succ von ihrer edge wissen
		if(succ.containsKey(start) && pred.containsKey(end))
			return succ.get(start).get(end);
		else
			return Double.POSITIVE_INFINITY;
    }

	
    @Override
    public int getInDegree(V v) {
		if(succ.containsKey(v) && pred.containsKey(v)){
			return pred.get(v).size();
		}
		else
			return -1;    }

    @Override
    public int getOutDegree(V v) {
		if(succ.containsKey(v) && pred.containsKey(v)){
			return succ.get(v).size();
		}
		else
			return -1;
	}
	
	@Override
    public Set<V> getVertexSet() {
		return Collections.unmodifiableSet(succ.keySet()); // nicht modifizierbare Sicht
    }

    @Override
    public Set<V> getPredecessorVertexSet(V v) {
		return Collections.unmodifiableSet(pred.get(v).keySet());
    }

    @Override
    public Set<V> getSuccessorVertexSet(V v) {
		return Collections.unmodifiableSet(succ.get(v).keySet());
    }

    @Override
    public int getNumberOfVertexes() {
		return succ.size();
    }

    @Override
    public int getNumberOfEdges() {
		return numberEdge;
    }
	
	@Override
    public 
	DirectedGraph<V> invert() {
		Map<V, Map<V, Double>> tmp = succ; // Einfach Zeiger austausch besser
		succ = pred;
		pred = tmp;
		return this;
	}

	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (var v : getVertexSet()) {
			for (var t : getSuccessorVertexSet(v)) {
				str.append(v).append(" --> ").append(t).append(" weight = ").append(getWeight(v, t)).append("\n");
			}
		}
		return str.toString();
	}
	
	
	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(2,5);
		g.addEdge(5,1);
		g.addEdge(2,6);
		g.addEdge(3,7);
		g.addEdge(4,3);
		g.addEdge(4,6);
		g.addEdge(7,4);
		
		
		System.out.println(g.getNumberOfVertexes());	// 7
		System.out.println(g.getNumberOfEdges());		// 8
		System.out.println(g.getVertexSet());	// 1, 2, ..., 7
		System.out.println(g);
			// 1 --> 2 weight = 1.0 
			// 2 --> 5 weight = 1.0
			// 2 --> 6 weight = 1.0
			// 3 --> 7 weight = 1.0
			// ...
		
		System.out.println("");
		System.out.println(g.getOutDegree(2));				// 2
		System.out.println(g.getSuccessorVertexSet(2));	// 5, 6
		System.out.println(g.getInDegree(6));				// 2
		System.out.println(g.getPredecessorVertexSet(6));	// 2, 4
		
		System.out.println("");
		System.out.println(g.containsEdge(1,2));	// true
		System.out.println(g.containsEdge(2,1));	// false
		System.out.println(g.getWeight(1,2));	// 1.0	
		g.addEdge(1, 2, 5.0);
		System.out.println(g.getWeight(1,2));	// 5.0	
		
		System.out.println("");
		System.out.println(g.invert());
			// 1 --> 5 weight = 1.0
			// 2 --> 1 weight = 5.0
			// 3 --> 4 weight = 1.0 
			// 4 --> 7 weight = 1.0
			// ...
			
		Set<Integer> s = g.getSuccessorVertexSet(2);
		System.out.println(s);
		s.remove(5);	// Laufzeitfehler! Warum?
	}
}
