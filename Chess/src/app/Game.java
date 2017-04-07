package app;

import pieces.*;
import java.util.Scanner;

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
    public enum Turn { WHITE, BLACK };

    /**
     * Current turn (white or black)
     */
    Turn turn;
    /**
     * The chess board
     */
    Board board;

    /**
     * Constructor
     *
     * Creates a new Board object and calls the start() method
     */
    public Game() {
        board = new Board();
        start(board);
    }

    /**
     * Begins the game of chess. Loops for inputs by the user and alters
     * the chess board until a winner is declared
     *
     * @param board The chess board
     */
    public void start(Board board) {
        Scanner reader = new Scanner(System.in);
        String input;
        turn = Turn.WHITE;
        boolean winnerDeclared = false;
        boolean lastMoveLegal = true;
        boolean drawOffered = false;

        while(!winnerDeclared) {

            //Only print board if a move was executed
            if(lastMoveLegal)
                board.display();

            //White's turn
            if(turn == Turn.WHITE)
                System.out.print("White's move: ");

            //Black's turn
            else
                System.out.print("Black's move: ");

            input = reader.nextLine();

            //Players agree on a draw
            if(input.equals("draw") && drawOffered)
                break;

            //Turn was valid, next players turn
            if(movePiece(input, turn)) {
                if(turn == Turn.WHITE) {
                    turn = Turn.BLACK;
                    clearEnpassant("b");
                }
                else {
                    turn = Turn.WHITE;
                    clearEnpassant("w");
                }

                lastMoveLegal = true;

                //Player made a valid move and offered a draw
                if(input.length() == 11 && input.substring(6, 11).equals("draw?"))
                    drawOffered = true;
                else
                    drawOffered = false;

                //Player resigned
                if(input.equals("resign"))
                    winnerDeclared = true;
                
                //Check if player is in check
                if(detectCheck()) {
                    System.out.println("\nCheck");


                    //Check if player is in checkmate, if so they lose
                    boolean checkMate = false;
                    if(turn == turn.WHITE)
                        checkMate = detectMate("w");
                    else
                        checkMate = detectMate("b");

                    if(checkMate) {
                        if(turn == turn.WHITE)
                            turn = turn.BLACK;
                        else
                            turn = turn.WHITE;
                        winnerDeclared = true;
                        System.out.println("Checkmate");
                        System.out.println();
                        board.display();

                    }

                }





            //Invalid move
            } else {
                lastMoveLegal = false;
                System.out.println("\nIllegal move, try again");
            }

            System.out.println();
        }

        //Print Winner
        if(winnerDeclared && turn == Turn.WHITE)
            System.out.println("White wins");
        else if(winnerDeclared && turn == Turn.BLACK)
            System.out.println("Black wins");
        else
            System.out.println("\nDraw");
    }


    /**
     * Depending on which players turn it is, detects if they are
     * requesting a valid move
     *
     * @param s The user's input in FileRank (start) FileRank (end) (IE a2 a4)
     * @param turn The current players turn, either white or black
     * @return True if the move was valid for the chess piece or false otherwise
     */
    public boolean movePiece(String s, Turn turn) {

        //Player resigned
        if(s.equals("resign"))
            return true;

        //Invalid input
        if(s.length() < 5)
            return false;

        Point start = getPosition(s.substring(0,2));
        Point end = getPosition(s.substring(3,5));

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

        //Check input included a promotion character
        if(s.length() >= 7) {
            startSquare.piece.promotion = s.charAt(6);
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
     * Parses an input into a x and y coordinate (IE: a1 gets converted to (0, 7))
     *
     * @param s FileRank (IE a1)
     * @return A new Point object with the x and y coordinates pertaining to the location on a chessboard
     */
    public Point getPosition(String s) {

        char c1, c2;
        int x, y;

        c1 = s.charAt(0);
        c2 = s.charAt(1);

        //x coordinate
        switch(c1) {
            case 'a': x = 0; break;
            case 'b': x = 1; break;
            case 'c': x = 2; break;
            case 'd': x = 3; break;
            case 'e': x = 4; break;
            case 'f': x = 5; break;
            case 'g': x = 6; break;
            case 'h': x = 7; break;
            default: return null;
        }

        //y coordinate
        y = Character.getNumericValue(c2);
        y = 8 - y; //Maps input to index of board (IE 8 goes to 0, 7 to 1, etc)

        //Check bounds;
        if(y < 0 || y > 7)
            return null;

        return new Point(x, y);
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
