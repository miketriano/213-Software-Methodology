package app;

import pieces.*;

/**
 * Represents the current state of each square on the 8x8 chess board
 *
 * @author Michael Triano
 * @author Joseph Wolak
 */
public class Square {

    /**
     * Chess piece on the square
     */
    public Piece piece;
    /**
     * The player (white or black) who owns the piece on the square
     */
    public String player;

    /**
     * Constructor
     *
     * @param p Piece
     * @param pl Player color
     */
    public Square(Piece p, String pl) {
        this.piece = p;
        this.player = pl;
    }

    /**
     * Overridden toString method
     *
     * @return The formatted color and chess piece (IE white's king: wK)
     */
    @Override
    public String toString() {
        if(piece instanceof King) return player + 'K';
        if(piece instanceof Queen) return player + 'Q';
        if(piece instanceof Rook) return player + 'R';
        if(piece instanceof Knight) return player + 'N';
        if(piece instanceof Bishop) return player + 'B';
        if(piece instanceof Pawn) return player + 'p';
        else return "  ";
    }
}
