package app;

import pieces.*;

/**
 * Object that holds the chessboard in a 8x8 array of Squares
 *
 * @author Michael Triano
 * @author Joseph Wolak
 */
public class Board {

    /**
     * 2d array of Squares
     */
    public Square[][] square;

    /**
     * Constructor
     *
     * Creates a new 8x8 array of Squares and initialize each square to
     * the correct chess piece and player color in the beginning of a
     * standard chess match
     */
    public Board() {

        //8x8 board
        square = new Square[8][8];

        //Blacks pieces
        square[0][0] = new Square(new Rook(), "b");
        square[0][1] = new Square(new Knight(), "b");
        square[0][2] = new Square(new Bishop(), "b");
        square[0][3] = new Square(new Queen(), "b");
        square[0][4] = new Square(new King(), "b");
        square[0][5] = new Square(new Bishop(), "b");
        square[0][6] = new Square(new Knight(), "b");
        square[0][7] = new Square(new Rook(), "b");

        for(int i = 0; i < 8; i++) {
            square[1][i] = new Square(new Pawn(), "b");
        }

        //Whites pieces
        square[7][0] = new Square(new Rook(), "w");
        square[7][1] = new Square(new Knight(), "w");
        square[7][2] = new Square(new Bishop(), "w");
        square[7][3] = new Square(new Queen(), "w");
        square[7][4] = new Square(new King(), "w");
        square[7][5] = new Square(new Bishop(), "w");
        square[7][6] = new Square(new Knight(), "w");
        square[7][7] = new Square(new Rook(), "w");

        for(int i = 0; i < 8; i++) {
            square[6][i] = new Square(new Pawn(), "w");
        }


    }

    /**
     * Prints the current state of the chess board formatted
     */
    public void display() {
        /**
         * For printing ## every other square
         */
        boolean hash = false;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(square[i][j] == null && hash)
                    System.out.print("## ");
                else if(square[i][j] == null)
                    System.out.print("   ");
                else
                    System.out.print(square[i][j] + " ");

                hash = !hash;
            }
            System.out.println(8 - i);
            hash = !hash;
        }
        System.out.println(" a  b  c  d  e  f  g  h\n");
    }
}
