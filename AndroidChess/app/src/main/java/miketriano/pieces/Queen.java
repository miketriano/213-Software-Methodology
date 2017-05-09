package miketriano.pieces;


import miketriano.chess.*;

/**
 * Queen chess piece, subclass of Piece
 *
 * @author Michael Triano
 * @author Joseph Wolak
 */
public class Queen extends Piece {

    public Queen() {
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



        /*

        	VERTICAL AND HORIZONTAL MOVEMENT

         */

		//Moving up
		if(numSpacesX == 0 && numSpacesY > 0){
			if(start.getY() > end.getY()) {
				for (int i = start.getY() - 1; i > end.getY(); i--) {
					if (board.square[i][start.getX()] != null)
						return false;
				}

				return true;
			}
		}

		//Moving down
		if(numSpacesX == 0 && numSpacesY > 0){
			if(start.getY() < end.getY()) {
				for (int i = start.getY() + 1; i < end.getY(); i++) {
					if (board.square[i][start.getX()] != null)
						return false;
				}

				return true;
			}
		}

		//Moving left
		if(numSpacesX > 0 && numSpacesY == 0){
			if(start.getX() > end.getX()) {
				for (int i = start.getX() - 1; i > end.getX(); i--) {
					if (board.square[start.getY()][i] != null)
						return false;
				}

				return true;
			}
		}

		//Moving right
		if(numSpacesX > 0 && numSpacesY == 0){
			if(start.getX() < end.getX()) {
				for (int i = start.getX() + 1; i < end.getX(); i++) {
					if (board.square[start.getY()][i] != null)
						return false;
				}

				return true;
			}
		}



		/*

			DIAGONAL MOVEMENT

		 */

		//Up and left diagonal
		if(numSpacesX == numSpacesY) {
			if (start.getX() > end.getX() && start.getY() > end.getY()) {
				for (int i = start.getY() - 1, j = start.getX() - 1; i > end.getY() && j > end.getX(); i--, j--) {
					if (board.square[i][j] != null)
						return false;
				}
				return true;
			}
		}

		//Up and right diagonal
		if(numSpacesX == numSpacesY) {
			if (start.getX() < end.getX() && start.getY() > end.getY()) {
				for (int i = start.getY() - 1, j = start.getX() + 1; i > end.getY() && j < end.getX(); i--, j++) {
					if (board.square[i][j] != null)
						return false;
				}
				return true;
			}
		}

		//Bottom and left diagonal
		if(numSpacesX == numSpacesY) {
			if (start.getX() > end.getX() && start.getY() < end.getY()) {
				for (int i = start.getY() + 1, j = start.getX() - 1; i < end.getY() && j > end.getX(); i++, j--) {
					if (board.square[i][j] != null)
						return false;
				}
				return true;
			}
		}

		//Bottom and right diagonal
		if(numSpacesX == numSpacesY) {
			if (start.getX() < end.getX() && start.getY() < end.getY()) {
				for (int i = start.getY() + 1, j = start.getX() + 1; i < end.getY() && j < end.getX(); i++, j++) {
					if (board.square[i][j] != null)
						return false;
				}
				return true;
			}
		}

        
        return false;
    }
}