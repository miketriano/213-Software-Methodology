package pieces;

import app.*;

/**
 * Knight chess piece, subclass of Piece
 *
 * @author Michael Triano
 * @author Joseph Wolak
 */
public class Knight extends Piece {

    public Knight() {
        firstMove = true;
    }

    @Override
    public boolean isValidMove(Point start, Point end, Board board) {

        Square startSquare = board.square[start.getY()][start.getX()];
        Square endSquare = board.square[end.getY()][end.getX()];

        //Number of spaces moved in X and Y direction
        int numSpacesX = Math.abs(start.getX() - end.getX());
        int numSpacesY = Math.abs(start.getY() - end.getY());

        //Check to make sure not landing on their own piece
        if(endSquare != null && startSquare.player.equals(endSquare.player))
            return false;

        //Left or right 1, up or down 2
        if(numSpacesX == 1 && numSpacesY == 2)
            return true;

        //Left or right 2, up or down 1
        if(numSpacesX == 2 && numSpacesY == 1)
            return true;


        return false;
    }
}
