package TelNetApplication;

public class TelVerbindung {
    public final TelKnoten u;
    public final TelKnoten v;
    public final int c;

    public TelVerbindung(TelKnoten u, TelKnoten v, int c) {
        this.u = u; //Startknoten
        this.v = v; //Endknoten
        this.c = c; //Kosten
    }

    @Override
    public String toString() {
        return String.format("%s => %s -- %d", this.u, this.v, this.c);
    }
}
