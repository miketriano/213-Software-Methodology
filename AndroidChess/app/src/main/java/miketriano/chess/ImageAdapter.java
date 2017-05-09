package miketriano.chess;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import miketriano.pieces.*;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return 64; //8x8 squares on chessboard
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if(convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(getImage(position));
        return imageView;
    }

    private int getImage(int position) {

        Square square = GameActivity.game.board.square[position / 8][position % 8];

        //Blank square;
        if(square == null)
            return R.drawable.blank_square;

        Piece piece = square.piece;
        String player = square.player;

        //White's pieces
        if(player.equals("w")) {
            if(piece instanceof King)
                return R.drawable.w_king;
            if(piece instanceof Queen)
                return R.drawable.w_queen;
            if(piece instanceof Rook)
                return R.drawable.w_rook;
            if(piece instanceof Knight)
                return R.drawable.w_knight;
            if(piece instanceof Bishop)
                return R.drawable.w_bishop;
            if(piece instanceof Pawn)
                return R.drawable.w_pawn;
        }

        //Black's pieces;
        else if(player.equals("b")) {
            if(piece instanceof King)
                return R.drawable.b_king;
            if(piece instanceof Queen)
                return R.drawable.b_queen;
            if(piece instanceof Rook)
                return R.drawable.b_rook;
            if(piece instanceof Knight)
                return R.drawable.b_knight;
            if(piece instanceof Bishop)
                return R.drawable.b_bishop;
            if(piece instanceof Pawn)
                return R.drawable.b_pawn;
        }

        return R.drawable.blank_square;
    }

}
