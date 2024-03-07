package com.example.gamecenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentLogin extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).isInLoginOrRegisterFragment = true;
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        setUtilButtons(rootView);
        return rootView;
    }

    private void setUtilButtons(View rootView) {
        Button loginButton = rootView.findViewById(R.id.loginButton);
        Button registerButton = rootView.findViewById(R.id.registerButton);

        //Boton para login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
                ((MainActivity) requireActivity()).isInLoginOrRegisterFragment = false;
            }
        });

        //Boton para register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new FragmentRegister());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}
