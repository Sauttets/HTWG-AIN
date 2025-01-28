package TelNetApplication;

import java.awt.*;
import java.util.*;
import java.util.List;

import Aufgabenblatt4.StdDraw;

public class TelNet {
    private int lbg;
    private List<TelVerbindung> minTree;
    private HashMap<TelKnoten, Integer> kMap;
    private int size = 0;

    public TelNet(int lbg) {
        this.lbg = lbg;
        this.minTree = new LinkedList<>();
        this.kMap = new HashMap<>();
    }

    public boolean addTelKnoten(int x, int y) {
        TelKnoten k = new TelKnoten(x, y);
        //Knoten schon vorhanden
        if (this.kMap.containsKey(k))
            return false;
        //Knoten hinzufügen
        this.kMap.put(k, size++);
            return true;
    }

    //Kruskal's algorithm
    public boolean computeOptTelNet() {
        UnionFind uf = new UnionFind(size);
        PriorityQueue<TelVerbindung> verbindungen = new PriorityQueue<>(size, Comparator.comparing(x -> x.c));
        List<TelVerbindung> minSpanTree = new LinkedList<>();

        fillPriorityQueue(verbindungen);

        //überflüssige Verbindungen entfernen und minSpanTree erstellen
        while (uf.size() != 1 && !verbindungen.isEmpty()) {
            TelVerbindung currentVerbindung = verbindungen.poll();
            int t1 = uf.find(kMap.get(currentVerbindung.u));
            int t2 = uf.find(kMap.get(currentVerbindung.v));

            //Verbindung hinzufügen, wenn sie nicht schon vorhanden ist
            if (t1 != t2)
            {
                uf.union(t1, t2);
                minSpanTree.add(currentVerbindung);
            }
        if (verbindungen.isEmpty() && uf.size() != 1)
            return false;
        else
            this.minTree = minSpanTree;

        return true;
    }

    //Füllt die PriorityQueue mit allen möglichen Verbindungen
    private void fillPriorityQueue(PriorityQueue<TelVerbindung> edges) {
        for (var entry1 : kMap.entrySet()) {
            var telKnoten1 = entry1.getKey();

            for (var entry2 : kMap.entrySet()) {
                var telKnoten2 = entry2.getKey();

                if (telKnoten1.equals(telKnoten2)) {
                    continue;
                }

                int cost = getCost(telKnoten1, telKnoten2);

                if (cost <= this.lbg) {
                    edges.add(new TelVerbindung(telKnoten1, telKnoten2, cost));
                }
            }
        }
    }


    private int getCost(TelKnoten v, TelKnoten w) {
        return Math.abs(v.x - w.x) + Math.abs(v.y - w.y);
    }

    public List<TelVerbindung> getOptTelNet() throws java.lang.IllegalStateException {
        return this.minTree;
    }

    //addiert alle Kantengewichtungen
    public int getOptTelNetKosten() throws java.lang.IllegalStateException {
        int totalCost = 0;
        for (var e : this.minTree) {
            totalCost += e.c;
        }
        return totalCost;
    }

    public void drawOptTelNet(int xMax, int yMax) throws java.lang.IllegalStateException {
        StdDraw.setCanvasSize(1024, 1024);
        StdDraw.setXscale(0, xMax + 1);
        StdDraw.setYscale(0, yMax + 1);

        for (var e : this.minTree)
        {
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(e.u.x, e.u.y, e.v.x, e.u.y);
            StdDraw.line(e.v.x, e.v.y, e.v.x, e.u.y);

            StdDraw.setPenColor(Color.BLUE);
            StdDraw.filledSquare(e.u.x, e.u.y, 0.5);
            StdDraw.filledSquare(e.v.x, e.v.y, 0.5);

        }
        StdDraw.show(0);
    }

    public void generateRandomTelNet(int n, int xMax, int yMax) {
        Random rand = new Random();
        int i = 0;
        while (i < n) {
            if (addTelKnoten(rand.nextInt(xMax + 1), rand.nextInt(yMax + 1)))
                i++;
        }
    }

    private static void testAbb3()
    {
        TelNet telNet = new TelNet(7);
        telNet.addTelKnoten(1, 1);
        telNet.addTelKnoten(3, 1);
        telNet.addTelKnoten(4, 2);
        telNet.addTelKnoten(3, 4);
        telNet.addTelKnoten(2, 6);
        telNet.addTelKnoten(4, 7);
        telNet.addTelKnoten(7, 6);

        if(telNet.computeOptTelNet()) {
            System.out.println("Kosten: " + telNet.getOptTelNetKosten());

            telNet.drawOptTelNet(7, 7);
        }
    }

    private static void testRnd()
    {
        TelNet telNet = new TelNet(100);
        telNet.generateRandomTelNet(1000, 1000, 1000);

        telNet.computeOptTelNet();
        System.out.println("Kosten: " + telNet.getOptTelNetKosten());
        telNet.drawOptTelNet(1000, 1000);

    }

    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        return "";
    }

    public static void main(String[] args)
    {
        //testAbb3();
        testRnd();
    }

}
