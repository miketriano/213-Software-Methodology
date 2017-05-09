package miketriano.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }

    public void startGame(View view) {
        startActivity(new Intent(this, GameActivity.class));
    }

    public void playback(View view) {
        startActivity(new Intent(this, PlaybackListActivity.class));
    }
}
