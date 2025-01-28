package TelNetApplication;

import java.util.Arrays;

public class UnionFind {
    private int p[];
    private int size;

    //konstruktor um alle knoten mit -1 zu initialisieren
    //jeder knoten ist anfangs eine root

    public UnionFind(int n) {
        this.p = new int[n];
        Arrays.fill(p, -1);
        this.size = n;
    }

    //Liefert die root eines beliebigen Knoten
    public int find(int e) {
        while(p[e] >= 0) {
            e = p[e];
        }
        return e;
    }

    public int size() {
        return this.size;
    }

    public void union(int s1, int s2) {
        if (p[s1] >= 0 || p[s2] >= 0) {
            return;
        }
        if (s1 == s2) {
            return;
        }

        if (-p[s1] < -p[s2]) {  // Höhe von s1 < Höhe von s2
            p[s1] = s2;
        } else {
            if (-p[s1] == -p[s2]) {
                p[s1]--; // Höhe von s1 erhöht sich um 1
            }
            p[s2] = s1;
        }
        size--;
    }

    public static void main(String[] args) {
        int size = 20;
        UnionFind uf = new UnionFind(size);
        uf.union(2, 1);
        uf.union(2, 2);
        uf.union(2, 3);
        uf.union(2, 4);
        System.out.println("Number of partitions: " + uf.size()); // size = 17
        System.out.println(Arrays.toString(uf.p));
        for(int i = 0; i < size; i++) {
            if (uf.p[i] >= 0)
                System.out.println("value: " + i + " has root: "+  uf.find(i));
        }
        System.out.println();

        uf.union(10, 12);
        uf.union(10, 13);
        uf.union(10, 14);
        uf.union(10, 15);
        System.out.println("Number of partitions: " + uf.size()); // size = 13
        System.out.println(Arrays.toString(uf.p));
        for(int i = 0; i < size; i++) {
            if (uf.p[i] >= 0)
                System.out.println("value: " + i + " has root: "+  uf.find(i));
        }
        System.out.println();
        uf.union(2, 10);
        System.out.println("Number of partitions: " + uf.size()); // size = 12
        System.out.println(Arrays.toString(uf.p));
        for(int i = 0; i < size; i++) {
            if (uf.p[i] >= 0)
                System.out.println("value: " + i + " has root: "+  uf.find(i));
        }
        System.out.println();
        for(int i = 0; i < size; i++) {
                System.out.println("value: " + i + " has root: "+  uf.find(i));
        }
    }

}
