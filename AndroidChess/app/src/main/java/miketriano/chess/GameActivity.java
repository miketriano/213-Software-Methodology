package miketriano.chess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import miketriano.pieces.*;


public class GameActivity extends AppCompatActivity
                          implements ConfirmDialogFragment.ConfirmDialogListener {

    Point start = null, end = null;

    static Game game;

    Game backupGame;

    GridView boardGridview;

    ImageAdapter mImageAdapter;

    private static Context sContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        GameActivity.sContext = GameActivity.this;

        game = new Game();

        backupGame = game;

        setupChessGrid();
    }

    /*
        Undo button listener

        Bugs: won't work if user makes invalid move
              can't move pawn twice again after undo
     */
    public void undo(View view) {
        game = new Game(backupGame.board.square, backupGame.moves, backupGame.turn);
        updateGrid();
        updateTurnText();
    }


    /*
        AI button listener
     */
    public void ai(View view) {

        String p = new String(game.turn.toString().charAt(0) + "");
        p = p.toLowerCase();


        for(int a = 0; a < 8; a++) {
            for(int b = 0; b < 8; b++) {
                Square s = game.board.square[a][b];
                if(s != null && s.player.equals(p)) {
                    for(int c = 0; c < 8; c++) {
                        for(int d = 0; d < 8; d++) {
                            backupGame = new Game(game.board.square, game.moves, game.turn);
                            if(game.movePiece(new Point(b, a), new Point(d, c))) {
                                checkPromotion(new Point(d, c));
                                updateGrid();
                                updateTurnText();

                                if(game.detectCheck()) {
                                    if(game.detectMate("")) {
                                        endGame();
                                        return;
                                    }
                                }

                                return;
                            }
                        }
                    }
                }
            }
        }
    }


    /*
        Draw button listener
     */
    public void draw(View view) {
        ConfirmDialogFragment confirmDialogFragment = ConfirmDialogFragment.newInstance("draw");
        confirmDialogFragment.show(getSupportFragmentManager(), "draw");
    }


    /*
        Resign button listener
     */
    public void resign(View view) {
        ConfirmDialogFragment confirmDialogFragment = ConfirmDialogFragment.newInstance("resign");
        confirmDialogFragment.show(getSupportFragmentManager(), "resign");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        endGame();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //Do nothing
    }

    public void endGame() {

        //Allow user to enter title for the match
        final EditText titleText = new EditText(this);

        String outcome = "";
        if(game.turn == Game.Turn.WHITE)
            outcome = getResources().getString(R.string.black_won);
        if(game.turn == game.turn.BLACK)
            outcome = getResources().getString(R.string.white_won);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(outcome)
                .setMessage("Enter Title")
                .setView(titleText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        game.setTitle(titleText.getText().toString());

                        //Save the game
                        PlaybackListActivity.sGameList.add(game);

                        //Go back to HomeActivity
                        finish();
                    }
                })
                .setNegativeButton("Don't Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Go back to HomeActivity
                        finish();
                    }
                })
                .setCancelable(false)
                .create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public void checkPromotion(Point end) {

        Square endSquare = game.board.square[end.getY()][end.getX()];

        //Check for promotion
        if(endSquare.piece instanceof Pawn) {
            if(endSquare.player.equals("w") && end.getY() == 0 ||
               endSquare.player.equals("b") && end.getY() == 7) {

                promotionDialog(end);
            }
        }
    }

    public void promotionDialog(Point e) {

        final Point end = e;

        final String[] pieces = {"Rook", "Knight", "Bishop", "Queen"};

        AlertDialog alertDialog = new AlertDialog.Builder(sContext)
                .setTitle("Promotion")
                .setItems(pieces, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println(pieces[which]);
                        switch(pieces[which]) {
                            case "Rook":
                                game.board.square[end.getY()][end.getX()] = new Square(new Rook(), game.board.square[end.getY()][end.getX()].player);
                                break;

                            case "Knight":
                                game.board.square[end.getY()][end.getX()] = new Square(new Knight(), game.board.square[end.getY()][end.getX()].player);
                                break;

                            case "Bishop":
                                game.board.square[end.getY()][end.getX()] = new Square(new Bishop(), game.board.square[end.getY()][end.getX()].player);
                                break;

                            case "Queen":
                                game.board.square[end.getY()][end.getX()] = new Square(new Queen(), game.board.square[end.getY()][end.getX()].player);
                                break;
                        }

                        updateGrid();
                    }
                })
                .setCancelable(false)
                .create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();


    }

    public void setupChessGrid() {

        //Setup GridView which displays the chess board
        boardGridview = (GridView) findViewById(R.id.boardGridview);
        mImageAdapter = new ImageAdapter(this);
        boardGridview.setAdapter(mImageAdapter);

        //hight = width for chess board since it is a square
        boardGridview.getLayoutParams().height = boardGridview.getLayoutParams().width;

        //Each time a square is clicked on the board
        boardGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // Get the square the user clicked on and set it to start if its the first click,
                // or end if its the second
                if(start == null) {
                    start = getPoint(position);
                }

                else {

                    end = getPoint(position);

                    backupGame = new Game(game.board.square, game.moves, game.turn);

                    //Try to move piece
                    if(game.movePiece(start, end)) {
                        checkPromotion(end);
                        updateGrid();
                        updateTurnText();

                        //Check
                        if(game.detectCheck()) {
                            Toast.makeText(GameActivity.this, R.string.check, Toast.LENGTH_SHORT).show();

                            game.board.display();

                            //Checkmate
                            if(game.detectMate(game.turn.toString().substring(0, 1).toLowerCase())) {
                                Toast.makeText(GameActivity.this, R.string.checkmate, Toast.LENGTH_LONG).show();
                                endGame();
                            }
                        }

                    } else {
                        Toast.makeText(GameActivity.this, R.string.invalid_move, Toast.LENGTH_SHORT).show();
                    }

                    //Clear points
                    start = null;
                    end = null;
                }

            }
        });
    }

    public void updateGrid() {
        mImageAdapter.notifyDataSetChanged();
        boardGridview.invalidateViews();
        boardGridview.setAdapter(mImageAdapter);
    }

    public void updateTurnText() {
        TextView turnText = (TextView) findViewById(R.id.turnText);
        turnText.setText(game.turn.toString() + "'S MOVE");
    }

    public Point getPoint(int position) {
        return new Point(position % 8, position / 8);
    }


}
