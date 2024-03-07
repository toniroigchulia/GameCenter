package com.example.gamecenter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.gamecenter.BD.GameDB;
import com.example.gamecenter.BD.Usuario;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class FragmentLogin extends Fragment {

    GameDB db;
    private ExecutorService executor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).isInLoginOrRegisterFragment = true;
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        this.db = ((MainActivity) requireActivity()).db;
        this.executor = ((MainActivity) requireActivity()).executor;
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
                if(iniciosesion(rootView)){
                    ((MainActivity) requireActivity()).isInLoginOrRegisterFragment = false;
                    getActivity().getOnBackPressedDispatcher().onBackPressed();
                }
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

    public boolean iniciosesion(View view){
        try {
            EditText user = view.findViewById(R.id.usernameLogin);
            EditText password = view.findViewById(R.id.passwordLogin);
            String usertext = String.valueOf(user.getText());
            String passwordtext = String.valueOf(password.getText());

            if (checkCorrectLogin(usertext,passwordtext)){
                guardarDatosDeSession(usertext,passwordtext);
                return true;
            }else{
                return false;
            }
        } catch (Exception e){
            System.out.println("Error inicio de session: " + e);
            return false;
        }
    }

    private boolean checkCorrectLogin(String user,String password){
        try {
        Future<Boolean> future = executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return db.LogInDAO().existUser(user, password);
            }
        });
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void guardarDatosDeSession(String usertext, String passwordtext){
        Future<Usuario> future = executor.submit(new Callable<Usuario>() {
            @Override
            public Usuario call() throws Exception {
                return db.LogInDAO().login(usertext,passwordtext);
            }
        });

        try {
            Usuario usuario = future.get();
            ((MainActivity) requireContext()).setUser(usuario);
            ((MainActivity) requireContext()).name.setText(String.valueOf(usuario.getUser()));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
