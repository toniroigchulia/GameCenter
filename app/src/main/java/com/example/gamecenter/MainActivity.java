package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.fragment.app.FragmentManager;

import com.example.gamecenter.BD.GameDB;
import com.example.gamecenter.BD.Usuario;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<DataItem> dataList;
    private MainAdapter adapter;
    private boolean isRecyclerViewVisible;
    public boolean isInLoginOrRegisterFragment = false;

    public Usuario user;
    GameDB db;
    public ExecutorService executor;

    public TextView name;
    public RelativeLayout menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        recyclerView = findViewById(R.id.recyclerView);
        name = findViewById(R.id.textViewUsername);
        menu = findViewById(R.id.menu);

        this.db = GameDB.getInstance(this);
        this.executor = Executors.newSingleThreadExecutor();
        loginScreen();

        dataList = generateData();
        setUtilButtons(menu);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(dataList, this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    Fragment fragment = null;
                    isRecyclerViewVisible = false;
                    setInvisible();
                    switch (position) {
                        case 0:
                            fragment = new FragmentSenku();
                            break;
                        case 1:
                            fragment = new Fragment2048();
                            break;
                        default:
                            break;
                    }

                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!isRecyclerViewVisible) {
                    isRecyclerViewVisible = true;
                    setVisible();
                    getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setUtilButtons(View rootView) {
        Button cerrarButton = rootView.findViewById(R.id.btnCerrarSesion);
        Button boardButton = rootView.findViewById(R.id.btnLeaderboards);

        //Boton para cerrar session
        cerrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        //Boton para ir a los leaderboards
        boardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaderboards();
            }
        });
    }

    private List<DataItem> generateData() {
        List<DataItem> data = new ArrayList<>();
        for (int i = 0; i <= 1; i++) {
            switch (i) {
                case 0:
                    data.add(new DataItem("Senku", R.drawable.backgroundsenkucard, "0"));
                    break;
                case 1:
                    data.add(new DataItem("2048", R.drawable.backgroundcard2048, "0"));
                    break;
                default:
                    break;
            }
        }
        return data;
    }

    private void loginScreen() {
        isRecyclerViewVisible = false;
        setInvisible();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FragmentLogin())
                .addToBackStack(null)
                .commit();
    }

    private void leaderboards() {
        isRecyclerViewVisible = false;
        setInvisible();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FragmentLeaderboards())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (isInLoginOrRegisterFragment) {
            // Ignorada
        } else {
            super.onBackPressed();
        }
    }

    public void setUser(Usuario user) {
        this.user = user;
        this.adapter.setUser(user);
        this.adapter.notifyDataSetChanged();
    }

    public void setInvisible() {
        recyclerView.setVisibility(View.GONE);
        findViewById(R.id.btnLeaderboards).setVisibility(View.GONE);
        findViewById(R.id.btnCerrarSesion).setVisibility(View.GONE);
        name.setVisibility(View.GONE);
    }

    public void setVisible() {
        recyclerView.setVisibility(View.VISIBLE);
        findViewById(R.id.btnLeaderboards).setVisibility(View.VISIBLE);
        findViewById(R.id.btnCerrarSesion).setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
    }
}