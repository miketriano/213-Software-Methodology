package miketriano.pieces;


import miketriano.chess.*;

/**
 * The basis for a standard chess piece
 *
 * @author Michael Triano
 * @author Joseph Wolak
 */
public abstract class Piece {
    /**
     * Tracks if the piece has made a move yet
     */
    public boolean firstMove;
    /**
     * Tracks if pawn is eligable for promotion
     */
    public char promotion;
    /**
     * Tracks if en passant is possible
     */
    public char enpassant;
    /**
     * Tracks if castling is possible
     */
    public char castling;

    /**
     * Used by each chess piece to validate if a move is legal
     *
     * @param start Where the piece is currently
     * @param end Where the piece wants to go
     * @param board The chess board
     * @return True if the piece can move ot the end point legally, false otherwise
     */
    public abstract boolean isValidMove(Point start, Point end, Board board);
}
