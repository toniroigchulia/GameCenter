package com.example.gamecenter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gamecenter.BD.GameDB;
import com.example.gamecenter.BD.Usuario;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FragmentRegister extends Fragment {

    GameDB db;
    private ExecutorService executor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).isInLoginOrRegisterFragment = true;
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        this.db = ((MainActivity) requireActivity()).db;
        this.executor = ((MainActivity) requireActivity()).executor;
        setUtilButtons(rootView);

        return rootView;
    }

    private void setUtilButtons(View rootView) {
        Button crearButton = rootView.findViewById(R.id.crearButton);
        Button volverButton = rootView.findViewById(R.id.volverButton);

        //Boton para register
        crearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validDataEntry(rootView)){
                    ((MainActivity) requireActivity()).isInLoginOrRegisterFragment = false;
                    getActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        //Boton para volver
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

    private boolean validDataEntry(View v) {
        try {
            EditText username = v.findViewById(R.id.username);
            EditText password = v.findViewById(R.id.password);
            EditText confirmPassword = v.findViewById(R.id.confirmPassword);
            if (!FilledFields(String.valueOf(username.getText()), String.valueOf(password.getText()), String.valueOf(confirmPassword.getText()))) {
                Log.d("PRUEBA", "1");
                showToast("Tienes que rellenar todos los campos");
                return false;
            } else if (!isValidUser(String.valueOf(username.getText()))) {
                Log.d("PRUEBA", "2");
                showToast("El nombre de usuario que has escrito no es valido");
                username.setText("");
                return false;
            } else if (userAlreadyExist(String.valueOf(username.getText()))) {
                Log.d("PRUEBA", "3");
                showToast("El usuario ya existe");
                username.setText("");
                return false;
            } else if (!String.valueOf(password.getText()).equals(String.valueOf(confirmPassword.getText()))) {
                Log.d("PRUEBA", "4");
                showToast("Las contraseñas son diferentes");
                password.setText("");
                confirmPassword.setText("");
                return false;
            } else {
                Usuario usuario = new Usuario();
                usuario.setUser(String.valueOf(username.getText()));
                usuario.setPassword(String.valueOf(password.getText()));
                showToast("Session Creada");
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.RegisterDAO().insert(usuario);
                    }
                });
                ((MainActivity) requireActivity()).setUser(usuario);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Crear Session error: " + e);
            return false;
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean userAlreadyExist(String user) {
        Future<Boolean> future = executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return db.RegisterDAO().alreadyExist(user);
            }
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean FilledFields(String user,String password,String confirmpassword){
        if (user.equals("") && password.equals("") && confirmpassword.equals("")){
            return false;
        }else{
            return true;
        }
    }

    private boolean isValidUser(String user) {
        String userRegex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(userRegex);
        Matcher matcher = pattern.matcher(user);
        return matcher.matches();
    }
}
