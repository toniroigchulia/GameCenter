package com.example.gamecenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentRegister extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).isInLoginOrRegisterFragment = true;
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        setUtilButtons(rootView);
        return rootView;
    }

    private void setUtilButtons(View rootView) {
        Button crearButton = rootView.findViewById(R.id.crearButton);
        Button volverButton = rootView.findViewById(R.id.volverButton);

        //Boton para login
        crearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).isInLoginOrRegisterFragment = false;
            }
        });

        //Boton para register
        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new FragmentLogin());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}
