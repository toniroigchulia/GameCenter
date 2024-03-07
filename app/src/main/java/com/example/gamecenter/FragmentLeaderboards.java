package com.example.gamecenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gamecenter.BD.GameDB;
import com.example.gamecenter.BD.Usuario;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class FragmentLeaderboards extends Fragment {

    private RecyclerView recyclerView;
    private AdapterLeaderboard adapter;
    private GameDB db;
    private ExecutorService executor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leaderboards, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewBoard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Configurar el LinearLayoutManager
        adapter = new AdapterLeaderboard();
        recyclerView.setAdapter(adapter);

        this.db = ((MainActivity) requireActivity()).db;
        this.executor = ((MainActivity) requireActivity()).executor;

        Future<List<Usuario>> future = executor.submit(new Callable<List<Usuario>>() {
            @Override
            public List<Usuario> call() throws Exception {
                return db.RegisterDAO().getAllUsers();
            }
        });

        try {
            adapter.setData(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUtilButtons(rootView);

        return rootView;
    }

    private void setUtilButtons(View rootView) {
        Button backButton = rootView.findViewById(R.id.btnVolver);

        //Boton para volver atras
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }
}
