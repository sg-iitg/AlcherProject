package com.example.recyclerviewimplementation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog extends AppCompatDialogFragment {
    Context ctx;
    ExampleDialog(Context ctx) {
        this.ctx = ctx;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Enter")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String college = ((EditText)view.findViewById(R.id.college)).getText().toString();
                        String name = ((EditText)view.findViewById(R.id.name)).getText().toString();
                        String mobile = ((EditText)view.findViewById(R.id.mobile)).getText().toString();
                        String age = ((EditText)view.findViewById(R.id.age)).getText().toString();
                        String gender = ((EditText)view.findViewById(R.id.gender)).getText().toString();
                        String email = ((EditText)view.findViewById(R.id.email)).getText().toString();

                        BackgroundTask backgroundTask = new BackgroundTask(ctx);
                        backgroundTask.execute(college, name, mobile, age, gender, email);
                    }
                });
        return builder.create();
    }
}

