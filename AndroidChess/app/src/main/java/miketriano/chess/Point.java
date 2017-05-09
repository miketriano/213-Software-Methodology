package miketriano.chess;

/**
 * Used to hold the x and y coordinate of a specific square on the chess board
 *
 * @author Michael Triano
 * @author Joseph Wolak
 */
public class Point {
    int x, y;

    /**
     * Constructor
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for int x
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for int y
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "( " + x + " , " + y + " )";
    }
}
