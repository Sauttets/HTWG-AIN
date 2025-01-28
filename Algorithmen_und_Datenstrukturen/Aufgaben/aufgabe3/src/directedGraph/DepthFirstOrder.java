// O. Bittel;
// 22.02.2017
package directedGraph;

import java.util.*;

/**
 * Klasse für Tiefensuche.
 *
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class DepthFirstOrder<V> {

    private final List<V> preOrder = new LinkedList<>();
    private final List<V> postOrder = new LinkedList<>();
    private final DirectedGraph<V> myGraph;
    private int numberOfDFTrees = 0;
	// ...

    /**
     * Führt eine Tiefensuche für g durch.
     *
     * @param g gerichteter Graph.
     */
    public DepthFirstOrder(DirectedGraph<V> g) {
        myGraph = g;
        visitDF();
    }

    private void visitDF()
    {

        visitDFPre(myGraph);
        visitDFPost(myGraph);
    }

    /**
     * Liefert eine nicht modifizierbare Liste (unmodifiable view) mit einer
     * Pre-Order-Reihenfolge zurück.
     *
     * @return Pre-Order-Reihenfolge der Tiefensuche.
     */
    public List<V> preOrder() {
        return Collections.unmodifiableList(preOrder);
    }

    private void visitDFPre(DirectedGraph<V> g)
    {
        Set<V> besucht = new TreeSet<>();
        for(V x : g.getVertexSet())
        {
            if(!besucht.contains(x))    //es gibt einen weiteren Baum
            {
                numberOfDFTrees++;
                visitDFPre(x,g,besucht);
            }
        }
    }

    private void visitDFPre(V v, DirectedGraph<V> g, Set<V> besucht)
    {
        besucht.add(v);
        preOrder.add(v);
        for(V x : g.getSuccessorVertexSet(v))
        {
            if(!besucht.contains(x))
            {
                visitDFPre(x,g,besucht);
            }
        }
    }

    /**
     * Liefert eine nicht modifizierbare Liste (unmodifiable view) mit einer
     * Post-Order-Reihenfolge zurück.
     *
     * @return Post-Order-Reihenfolge der Tiefensuche.
     */
    public List<V> postOrder() {
        return Collections.unmodifiableList(postOrder);
    }

    private void visitDFPost(DirectedGraph<V> g)
    {
        numberOfDFTrees = 0;            //weil er sonst auch die Bäume von Pre mitzählt
        Set<V> besucht = new TreeSet<>();
        for(V x : g.getVertexSet())
        {
            if(!besucht.contains(x))    //es gibt einen weiteren Baum
            {
                numberOfDFTrees++;
                visitDFPost(x,g,besucht);
            }
        }
    }

    private void visitDFPost(V v, DirectedGraph<V> g, Set<V> besucht)
    {
        besucht.add(v);
        for(V x : g.getSuccessorVertexSet(v))
        {
            if(!besucht.contains(x))
            {
                visitDFPost(x,g,besucht);
            }
        }
        postOrder.add(v);
    }

    /**
     *
     * @return Anzahl der Bäume des Tiefensuchwalds.
     */
    public int numberOfDFTrees() {
        return numberOfDFTrees;
    }

    public static void main(String[] args) {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        g.addEdge(1, 2);
        g.addEdge(2, 5);
        g.addEdge(5, 1);
        g.addEdge(2, 6);
        g.addEdge(3, 7);
        g.addEdge(4, 3);
        g.addEdge(4, 6);
        //g.addEdge(7,3);
        g.addEdge(7, 4);

        DepthFirstOrder<Integer> dfs = new DepthFirstOrder<>(g);
        System.out.println(dfs.numberOfDFTrees());	// 2
        System.out.println(dfs.preOrder());		// [1, 2, 5, 6, 3, 7, 4]
        System.out.println(dfs.postOrder());		// [5, 6, 2, 1, 4, 7, 3]

    }
}
