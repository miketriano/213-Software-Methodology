package miketriano.pieces;


import miketriano.chess.*;

/**
 * King chess piece, subclass of Piece
 *
 * @author Michael Triano
 * @author Joseph Wolak
 */
public class King extends Piece {

    public King() {
        firstMove = true;
        castling = '0';
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

        //Move to adjacent square
        if((numSpacesX == 1 && numSpacesY == 0) ||
           (numSpacesX == 0 && numSpacesY == 1) ||
           (numSpacesX == 1 && numSpacesY == 1)) {
            firstMove = false;
            castling = '0';
            return true;
        }


        //Castling
        if(numSpacesX == 2 && numSpacesY == 0 && firstMove) {

            Square leftRookSquare = board.square[start.getY()][0];
            Square rightRookSquare = board.square[start.getY()][7];

            //Moving left
            if(start.getX() > end.getX()) {
                if(leftRookSquare != null &&
                   leftRookSquare.piece instanceof Rook &&
                   leftRookSquare.piece.firstMove &&
                   board.square[start.getY()][start.getX()].player.equals(leftRookSquare.player)) {

                    //Make sure path is clear
                    for(int i = start.getX() - 1; i > 0; i--)
                        if(board.square[start.getY()][i] != null)
                            return false;


                    castling = 'l';
                    return true;
                }
            }

            //Moving right
            else {
                if(rightRookSquare != null &&
                   rightRookSquare.piece instanceof Rook &&
                   rightRookSquare.piece.firstMove &&
                   board.square[start.getY()][start.getX()].player.equals(rightRookSquare.player)) {

                    //Make sure path is clear
                    for(int i = start.getX() + 1; i < 7; i++)
                        if(board.square[start.getY()][i] != null)
                            return false;


                    castling = 'r';
                    return true;
                }
            }

        }


        return false;
  }
}
