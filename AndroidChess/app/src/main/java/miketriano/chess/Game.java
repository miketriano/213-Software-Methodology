package miketriano.chess;

import miketriano.pieces.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Holds all the internal logic and structure for a game of chess
 * including the current players turn, parsing inputs, moving pieces,
 * and declaring a winner
 *
 * @author Michael Triano
 * @author Joseph Wolak
 */
public class Game {

    /**
     * Keeps track of the current players turn
     */
    public enum Turn { WHITE, BLACK }
    ;

    /**
     * Current turn (white or black)
     */
    Turn turn;
    /**
     * The chess board
     */
    Board board;

    String title;

    Date date;

    //The history of moves during the game
    List<Move> moves;


    /**
     * Constructor
     *
     * Creates a new Board object and calls the start() method
     */
    public Game() {
        board = new Board();
        moves = new ArrayList<>();
        title = "No Title";
        date = new Date();
        turn = Turn.WHITE;
    }

    public Game(Square[][] s, List<Move> m, Turn t) {

        board = new Board(s);
        this.moves = new ArrayList<>();
        this.moves.addAll(m);
        this.turn = t;
        date = new Date();
        title = "No Title";
    }


    public void setTitle(String t) {
        this.title = t;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    /**
     * Depending on which players turn it is, detects if they are
     * requesting a valid move
     */
    public boolean movePiece(Point start, Point end) {

        if(start == null || end == null)
            return false;

        //Check to make sure square is not blank
        if(board.square[start.getY()][start.getX()] == null)
            return false;

        Square startSquare = board.square[start.getY()][start.getX()];

        //Check to make sure player is moving their own piece
        if((startSquare.player.equals("b") && turn == Turn.WHITE) ||
           (startSquare.player.equals("w") && turn == Turn.BLACK)) {

            return false;
        }


        //Check to make sure the specific piece (pawn, bishop, etc) is allowed to move in that direction
        if(!startSquare.piece.isValidMove(start, end, board))
            return false;


        //Player executed en passant
        if(startSquare.piece.enpassant == '1') {
            board.square[start.getY()][end.getX()] = null;
        }

        //Player executed castling
        if(startSquare.piece.castling == 'l') {
            board.square[end.getY()][end.getX() + 1] = board.square[end.getY()][0];
            board.square[end.getY()][0] = null;
        }
        if(startSquare.piece.castling == 'r') {
            board.square[end.getY()][end.getX() - 1] = board.square[end.getY()][7];
            board.square[end.getY()][7] = null;
        }

        //Move the piece
        board.square[end.getY()][end.getX()] = board.square[start.getY()][start.getX()];
        board.square[start.getY()][start.getX()] = null;


        //Next players turn
        if(turn == Turn.WHITE) {
            turn = Turn.BLACK;
            clearEnpassant("b");
        }

        else {
            turn = Turn.WHITE;
            clearEnpassant("w");
        }

        moves.add(new Move(start, end));

        return true;

    }

    /**
     * Detects if a player is currently in check
     *
     * @return True if a player is in check, false otherwise
     */
    public boolean detectCheck() {

        //Holds the location of each player's king, and the piece to compare
        Point whitesKing = null, blacksKing = null, test;

        //Search for each king
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Square s = board.square[i][j];
                if(s == null) {
                    //Skip null spot
                }
                else if((s.piece instanceof King) && s.player.equals("w"))
                    whitesKing = new Point(j , i);
                else if((s.piece instanceof  King) && s.player.equals("b"))
                    blacksKing = new Point(j , i);
                
            }
           
        }

        //Compare each piece to see if it can attack opponents king
        int x = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Square s = board.square[i][j];
                if(s == null) {
                    //Skip null spot
                } else if(s.player.equals("w")) {
                    test = new Point(j , i);
                    if(s.piece.isValidMove(test, blacksKing, board)) {
                        //System.out.println("White's " + s.piece.getClass().getSimpleName() + " can attack black's king");
                        //board.display();
                        return true;    //Black in check

                    }
                } else if(s.player.equals("b")) {
                    test = new Point(j , i);
                    if(s.piece.isValidMove(test, whitesKing, board)) {
                        //System.out.println("Black's " + s.piece.getClass().getSimpleName() + " can attack white's king");
                        //board.display();
                        return true;    //White in check

                    }
                }
            }
        }

        return false;
    }

    /**
     * Detects if a player is in Checkmate
     *
     * @param p The player to check for checkmate (w or b)
     * @return True if player is in checkmate, false otherwise
     */
    public boolean detectMate(String p) {

        //Saves the firstMove variable
        boolean firstMove;

        //Loop through board and find white's pieces
        for(int a = 0; a < 8; a++) {
            for(int b = 0; b < 8; b++) {
                if(board.square[a][b] != null &&
                        board.square[a][b].player.equals(p)) {
                    Point start = new Point(b , a);

                    //Check every piece on the board to see if it can move there
                    for(int c = 0; c < 8; c++) {
                        for(int d = 0; d < 8; d++) {
                            Point end = new Point(d, c);

                            //Save firstMove variable
                            firstMove = board.square[a][b].piece.firstMove;

                            //It can move to given piece
                            if(board.square[a][b].piece.isValidMove(start, end, board)) {

                                //Move the piece
                                Square startSquare = board.square[a][b];
                                Square endSquare = board.square[end.getY()][end.getX()];
                                board.square[end.getY()][end.getX()] = board.square[a][b];
                                board.square[a][b] = null;

                                //Found a situation where player can move piece and not be in check anymore
                                if(!detectCheck()) {
                                    board.square[end.getY()][end.getX()] = endSquare;
                                    board.square[a][b] = startSquare;
                                    return false;
                                }

                                //Put the board back
                                board.square[end.getY()][end.getX()] = endSquare;
                                board.square[a][b] = startSquare;

                                //Reset the firstMove variable
                                board.square[a][b].piece.firstMove = firstMove;

                            }
                        }
                    }
                }
            }
        }


        return true;
    }


    /**
     * Goes through each pawn and clears their enpassant value
     *
     * @param player Player to compare (white or black)
     */
    public void clearEnpassant(String player) {

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Square s = board.square[i][j];
                if(s == null) {

                }
                else if(s.piece instanceof Pawn && s.player.equals(player))
                    ((Pawn) board.square[i][j].piece).doubleJump = false;
            }
        }
    }

}
