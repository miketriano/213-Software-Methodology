package miketriano.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlaybackListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    public static List<Game> sGameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_list);

        System.out.println(sGameList.size());

        mRecyclerView = (RecyclerView) findViewById(R.id.playback_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        PlaybackAdapter playbackAdapter = new PlaybackAdapter(sGameList);
        mRecyclerView.setAdapter(playbackAdapter);
    }

    private class PlaybackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Game> mGameList;

        private class PlaybackHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView mTitleTextView;
            private TextView mDateTextView;


            public PlaybackHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);

                mTitleTextView = (TextView) itemView.findViewById(R.id.name_text_view);
                mDateTextView = (TextView) itemView.findViewById(R.id.date_text_view);
            }

            public void bind(String s, String d) {
                mTitleTextView.setText(s);
                mDateTextView.setText(d);
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlaybackActivity.class);
                intent.putExtra("index", getLayoutPosition());
                startActivity(intent);
            }
        }


        public PlaybackAdapter(List<Game> list) {
            mGameList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(PlaybackListActivity.this);
            View view = layoutInflater.inflate(R.layout.list_activity_playback, parent, false);

            return new PlaybackHolder(view);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            PlaybackHolder playbackHolder = (PlaybackHolder) holder;
            String title = mGameList.get(position).getTitle();
            String date = new SimpleDateFormat("EEEE, MMM d, yyyy").format(mGameList.get(position).getDate());

            playbackHolder.bind(title, date);
        }

        @Override
        public int getItemCount() {
            return mGameList.size();
        }
    }
}
