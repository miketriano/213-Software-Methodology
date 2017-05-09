package miketriano.chess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

public class PlaybackActivity extends AppCompatActivity {

    private GridView boardGridview;

    private ImageAdapter mImageAdapter;

    private Game game;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        boardGridview = (GridView) findViewById(R.id.boardGridview);
        //height = width for chess board since it is a square
        boardGridview.getLayoutParams().height = boardGridview.getLayoutParams().width;


        mImageAdapter = new ImageAdapter(this);
        boardGridview.setAdapter(mImageAdapter);

        GameActivity.game = new Game();

        game = PlaybackListActivity.sGameList.get(getIntent().getIntExtra("index", 0));

        index = 0;

        updateGrid();

    }

    /*
        Next move button listener
     */
    public void nextMove(View view) {


        List<Move> moves = game.moves;

        if(index == moves.size()) {
            Toast.makeText(PlaybackActivity.this, R.string.no_moves, Toast.LENGTH_LONG).show();
        } else {
            Move m = moves.get(index);

            GameActivity.game.movePiece(m.start, m.end);

            updateGrid();

            index++;
        }


    }


    public void updateGrid() {
        mImageAdapter.notifyDataSetChanged();
        boardGridview.invalidateViews();
        boardGridview.setAdapter(mImageAdapter);
    }
}
