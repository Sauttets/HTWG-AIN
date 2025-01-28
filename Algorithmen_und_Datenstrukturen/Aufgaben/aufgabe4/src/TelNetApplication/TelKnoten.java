package TelNetApplication;

import java.util.Objects;

public class TelKnoten {
    public final int x;
    public final int y;

    public TelKnoten(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d - %d)", this.x, this.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof TelKnoten knoten)) {
            return false;
        }
        return this.x == knoten.x && this.y == knoten.y;
    }
}
