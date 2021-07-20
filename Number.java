 /*  Name: Henry Chen
  *  PennKey: henchen
  *  Recitation: 212
  *
  *  A class representing a number (tile) for the Proj2048.
  *
  */
public class Number {
    private int value;
    private boolean merge;
    
    // Constructor: tiles hold a value.
    public Number(int value) {
        this.value = value;
    }
    /**
     * Input: A different tile in the 2D array.
     * Output: boolean
     * Description: Tests if two tiles can be merged together.
     */
    public boolean mergeWith(Number different) {
        if (different == null) {
            return false;
        }
        if (different.getValue() != value) {
            return false;
        }
        boolean temp = !different.merge && !merge;
        return temp;
    }
    /**
     * Input: N/A
     * Output: int
     * Description: merges two tiles together to form a new tile of value
     * of the sum of the two tiles.
     */
    public int mergeTogether(Number tile) {
        value = value * 2;
        merge = true;
        return value;
    }
    /**
     * Input: boolean
     * Output: void
     * Description: after a tile has been merged, it cannot be merged again
     * during that move.
     */
    public void merged(boolean merge) {
        this.merge = merge;
    }
    public int getValue() {
        return value;
    }
}