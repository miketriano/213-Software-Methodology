package miketriano.chess;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class ConfirmDialogFragment extends DialogFragment {

    public interface ConfirmDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private ConfirmDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (ConfirmDialogListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public static ConfirmDialogFragment newInstance(String type) {

        ConfirmDialogFragment fragment = new ConfirmDialogFragment();

        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        System.out.println(getArguments().getString("type"));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(getArguments().getString("type").equals("draw"))
            builder.setMessage(R.string.draw);

        if(getArguments().getString("type").equals("resign"))
            builder.setMessage(R.string.resign);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       mListener.onDialogPositiveClick(ConfirmDialogFragment.this);
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       mListener.onDialogNegativeClick(ConfirmDialogFragment.this);
                   }
               });

        return builder.create();
    }

}
