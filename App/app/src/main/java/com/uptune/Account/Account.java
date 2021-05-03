package com.uptune.Account;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.uptune.MainActivity;
import com.uptune.R;

public class Account extends Fragment {

    Dialog dialog;
    MaterialCardView btnSettings, btnSell, btnSearch, btnMyFiles, btnRating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton logout = view.findViewById(R.id.btn_logout);

        btnSell = view.findViewById(R.id.btn_sell);
        btnSettings = view.findViewById(R.id.btn_settings);
        btnRating = view.findViewById(R.id.btn_rating);
        btnSearch = view.findViewById(R.id.btn_search);
        btnMyFiles = view.findViewById(R.id.btn_my_files);

        btnMyFiles.setOnClickListener(e -> {
            Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
        });
        btnSettings.setOnClickListener(e -> {
            Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
        });
        btnSearch.setOnClickListener(e -> {
            Toast.makeText(getContext(), "3", Toast.LENGTH_SHORT).show();
        });
        btnSell.setOnClickListener(e -> {
            Toast.makeText(getContext(), "4", Toast.LENGTH_SHORT).show();
        });
        btnRating.setOnClickListener(e -> {
            Toast.makeText(getContext(), "5", Toast.LENGTH_SHORT).show();
        });


        dialog = new Dialog(getContext());
        logout.setOnClickListener(v -> {
            openLogoutDialog();
        });
    }

    private void openLogoutDialog() {
        dialog.setContentView(R.layout.dialog_no_connection);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnCancel = dialog.findViewById(R.id.cancel);
        TextView txt = dialog.findViewById(R.id.txtDialog);
        Button btnConfirm = dialog.findViewById(R.id.confirm);
        btnConfirm.setText("Exit");
        txt.setText("Are you sure to log out?");
        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

}