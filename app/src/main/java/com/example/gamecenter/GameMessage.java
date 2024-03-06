package com.example.gamecenter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class GameMessage extends DialogFragment {
    private TextView mensaje;
    private int caso;

    public GameMessage(int caso){
        this.caso = caso;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.endgame, container, false);
        mensaje = view.findViewById(R.id.messageTextView);

        if(caso == 1){

            mensaje.setText("HAS GANADO");
        } else {

            mensaje.setText("HAS PERDIDO");
        }
        return view;
    }
}
