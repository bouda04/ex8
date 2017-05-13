package com.example.tcalc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by bouda04 on 3/12/2016.
 */

public class MyDialog extends DialogFragment {
    public final static int EXIT_DIALOG = 1;
    public final static int PRECISION_DIALOG = 2;

    private ResultsListener listener;
    private int reqCode;

    static MyDialog newInstance(int requestCode) {
        MyDialog dlg = new MyDialog();
        Bundle args = new Bundle();
        args.putInt("rc", requestCode);
        dlg.setArguments(args);
        return dlg;
    }

    public void setCallback(ResultsListener listener) {
        this.listener = listener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.reqCode = getArguments().getInt("rc");
        if (reqCode== EXIT_DIALOG)
            return buildExitDialog().create();
        else
            return buildPrecisionDialog().create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ResultsListener)activity;
        } catch(ClassCastException e) {
            throw new ClassCastException("hosting activity must implement ResultsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private AlertDialog.Builder buildExitDialog(){
        return new AlertDialog.Builder(getActivity())
                .setTitle("Closing the application")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listener.onFinishedDialog(reqCode, "ok");
                            }
                        }
                )
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                );
    }

    private AlertDialog.Builder buildPrecisionDialog(){
        final View view = getActivity().getLayoutInflater().inflate(R.layout.precision, null);
        final SeekBar precisionBar = (SeekBar) view.findViewById(R.id.seekBar1);
        final TextView tvPrecision= (TextView)(view.findViewById(R.id.tvValue));
        final int currPrecision = ((MainActivity)getActivity()).getCurrentPrecision();
        tvPrecision.setText(String.format("%."+currPrecision +"f", 123.0));
        precisionBar.setProgress(currPrecision);
        precisionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvPrecision.setText(String.format("%."+progress +"f", 123.0));
                precisionBar.setProgress(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle("Set the numbers precision")
                .setView(view)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listener.onFinishedDialog(reqCode, precisionBar.getProgress());
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                );
    }

    public interface ResultsListener {
        void onFinishedDialog(int requestCode, Object results);
    }
}
