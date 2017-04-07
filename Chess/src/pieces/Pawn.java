package pieces;

import app.*;

/**
 * Pawn chess piece, subclass of Piece
 *
 * @author Michael Triano
 * @author Joseph Wolak
 */
public class Pawn extends Piece{

    //Tracks if last move made was 2 spaces
    public boolean doubleJump;
    //public boolean firstMove;

    //Which piece to promote the pawn to
    public Pawn() {
        firstMove = true;
        doubleJump = false;
    }

    @Override
    public boolean isValidMove(Point start, Point end, Board board) {

        Square startSquare = board.square[start.getY()][start.getX()];
        Square endSquare = board.square[end.getY()][end.getX()];


        //Number of spaces moved in X and Y direction
        int numSpacesX = Math.abs(start.getX() - end.getX());
        int numSpacesY;

        //Check to make sure not landing on their own piece
        if(endSquare != null && startSquare.player.equals(endSquare.player)) {
            enpassant = '0';
            return false;
        }
        
        //Whites piece, moving up
        if(startSquare.player.equals("w"))
            numSpacesY = start.getY() - end.getY();

        //Blacks piece, moving down
        else
            numSpacesY = end.getY() - start.getY();

        //Trying to move up/down, check if space is empty
        if(numSpacesX == 0 && endSquare == null) {

            //Moving up/down 1 space
            if(numSpacesY == 1) {
                firstMove = false;

                //Check for promotion
                if(startSquare.player.equals("w") && end.getY() == 0 ||
                   startSquare.player.equals("b") && end.getY() == 7)
                    board.square[start.getY()][start.getX()] = new Square(parsePromotion(), startSquare.player);

                enpassant = '0';
                return true;
            }

            //Moving up/down 2 spaces, check if first move
            if(numSpacesY == 2 && firstMove == true) {

                //Check if path is clear
                int midY = (start.getY() + end.getY()) / 2;
                if(board.square[midY][start.getX()] == null) {
                    firstMove = false;
                    doubleJump = true;
                    enpassant = '0';
                    return true;
                }

            }
        }


        //Diagonal capture
        if(numSpacesX == 1 && numSpacesY == 1) {

            //Check to make sure trying to capture opponents piece and not landing on their own
            if(endSquare != null && !startSquare.player.equals(endSquare.player)) {

                //Check for promotion
                if(startSquare.player.equals("w") && end.getY() == 0 ||
                   startSquare.player.equals("b") && end.getY() == 7)
                    board.square[start.getY()][start.getX()] = new Square(parsePromotion(), startSquare.player);

                enpassant = '0';
                return true;
            }

            //En passant
            Square adjacentSquare = board.square[start.getY()][end.getX()];
            if(adjacentSquare != null &&
               !adjacentSquare.player.equals(startSquare.player) &&
               adjacentSquare.piece instanceof Pawn &&
                    ((Pawn) adjacentSquare.piece).doubleJump) {

                enpassant = '1';
                return true;
            }
        }


        enpassant = '0';
        return false;
    }



    /**
     * Checks the promotion field and returns the appropriate piece
     *
     * @return The piece to promote the Pawn to (Rook, Knight, Bishop, or Queen)
     */
    public Piece parsePromotion() {

        switch(promotion) {
            case 'R': return new Rook();
            case 'N': return new Knight();
            case 'B': return new Bishop();
            default: return new Queen();
        }
    }
    

}