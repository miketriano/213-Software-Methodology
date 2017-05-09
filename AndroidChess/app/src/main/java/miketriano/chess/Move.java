package miketriano.chess;


public class Move {
    Point start, end;

    Move(Point s, Point e) {
        this.start = s;
        this.end = e;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}