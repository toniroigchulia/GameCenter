package com.example.gamecenter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.gamecenter.BD.GameDB;
import com.example.gamecenter.BD.Usuario;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Fragment2048 extends Fragment implements View.OnTouchListener {

    private int[][] tileNumbers = new int[4][4];
    private GridView tileList;
    private Adapter2048 tileAdapter;
    private float startX, startY, endX, endY;
    private static final int MIN_DISTANCE = 150;
    private TextView currentScore;
    private boolean playing = true;
    GameDB db;
    private ExecutorService executor;
    private Usuario usuario;

    private TextView score;
    private TextView maxScore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_2048, container, false);
        try {

            tileList = rootView.findViewById(R.id.tileList);
            score = rootView.findViewById(R.id.currentScorePuntos);
            maxScore = rootView.findViewById(R.id.maxScorePuntos);
            currentScore = rootView.findViewById(R.id.currentScorePuntos);

            this.db = ((MainActivity) requireActivity()).db;
            this.executor = ((MainActivity) requireActivity()).executor;
            this.usuario = ((MainActivity) requireActivity()).user;

            maxScore.setText(String.valueOf(usuario.getBestScore2048()));

            initGame();
            setUtilButtons(rootView);

            tileAdapter = new Adapter2048(getActivity(), matrizToArray(tileNumbers));
            tileList.setAdapter(tileAdapter);
            tileList.setOnTouchListener(this);


        } catch (Exception e) {
            System.out.println("Error crear view 2048: " + e);
        }

        return rootView;
    }

    private void setUtilButtons(View rootView) {
        Button backButton = rootView.findViewById(R.id.backButton);
        Button restarButton = rootView.findViewById(R.id.resetButton);

        //Boton para reiniciar la partida
        restarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterUpdate(matrizToArray(initGame()));
            }
        });

        //Boton para volver atras
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private int[][] initGame() {
        playing = true;
        currentScore.setText(String.valueOf(0));
        for (int i = 0; i < tileNumbers.length; i++){
            for (int j = 0; j < tileNumbers[i].length; j ++){

                tileNumbers[i][j] = 0;
            }
        }

        tileNumbers = generateNew2(tileNumbers);
        tileNumbers = generateNew2(tileNumbers);

        return tileNumbers;
    }

    private void adapterUpdate(Integer[] arrayNumeros) {
        if (tileAdapter == null) {
            tileAdapter = new Adapter2048(getActivity(), arrayNumeros);
            tileList.setAdapter(tileAdapter);
        } else {
            tileAdapter.setNumeros(arrayNumeros);
            tileAdapter.notifyDataSetChanged();
        }
    }

    private Integer[] matrizToArray (int[][] numbers) {
        Integer[] array = new Integer[16];
        int arrayIndex = 0;

        for (int i = 0; i < numbers.length; i++){
            for (int j = 0; j < numbers[i].length; j ++){
                array[arrayIndex] = numbers[i][j];
                arrayIndex = arrayIndex + 1;
            }
        }

        return array;
    }

    private int[][] generateNew2(int[][] numbers) {
        Random rand = new Random();
        boolean continueTrying = true;
        boolean avaliableSpace = false;

        for (int i = 0; i < numbers.length; i++){
            for (int j = 0; j < numbers.length; j++) {
                if(numbers[i][j] == 0){
                    avaliableSpace = true;
                }
            }
        }

        while (continueTrying && avaliableSpace){
            int posXrand = rand.nextInt(4);
            int posYrand = rand.nextInt(4);

            if(numbers[posYrand][posXrand] == 0){
                numbers[posYrand][posXrand] = 2;
                continueTrying = false;
            }
        }

        return numbers;
    }

    private int[][] moveTiles(int direccion, int[][] numbers) {
        boolean cambio = true;
        switch (direccion) {
            // Derecha
            case 1:
                while (cambio) {
                    cambio = false;
                    for (int i = 0; i < numbers.length; i++){
                        for (int j = numbers[i].length-2; j >= 0; j--){
                            if(numbers[i][j] != 0 && numbers[i][j+1] == 0){
                                numbers[i][j+1] = numbers[i][j];
                                numbers[i][j] = 0;
                                cambio = true;
                            } else if (numbers[i][j] != 0 && numbers[i][j+1] == numbers[i][j]) {
                                numbers[i][j+1] = numbers[i][j] + numbers[i][j];
                                numbers[i][j] = 0;
                                currentScore.setText(String.valueOf((Integer.parseInt((String) currentScore.getText())) + numbers[i][j+1]));
                                cambio = true;
                            }
                        }
                    }
                }
                break;
            // Izquierda
            case 2:
                while (cambio){
                    cambio = false;
                    for (int i = 0; i < numbers.length; i++){
                        for (int j = 1; j < numbers.length; j++){
                            if(numbers[i][j] != 0 && numbers[i][j-1] == 0){
                                numbers[i][j-1] = numbers[i][j];
                                numbers[i][j] = 0;
                                cambio = true;
                            } else if (numbers[i][j] != 0 && numbers[i][j-1] == numbers[i][j]) {
                                numbers[i][j-1] = numbers[i][j] + numbers[i][j];
                                numbers[i][j] = 0;
                                currentScore.setText(String.valueOf((Integer.parseInt((String) currentScore.getText())) + numbers[i][j-1]));
                                cambio = true;
                            }
                        }
                    }
                }
                break;
            // Arriba
            case 3:
                while (cambio){
                    cambio = false;
                    for (int i = 1; i < numbers.length; i++){
                        for (int j = 0; j < numbers.length; j++){
                            if(numbers[i][j] != 0 && numbers[i-1][j] == 0){
                                numbers[i-1][j] = numbers[i][j];
                                numbers[i][j] = 0;
                                cambio = true;
                            } else if (numbers[i][j] != 0 && numbers[i-1][j] == numbers[i][j]) {
                                numbers[i-1][j] = numbers[i][j] + numbers[i][j];
                                numbers[i][j] = 0;
                                currentScore.setText(String.valueOf((Integer.parseInt((String) currentScore.getText())) + numbers[i-1][j]));
                                cambio = true;
                            }
                        }
                    }
                }
                break;
            // Abajo
            case 4:
                while (cambio){
                    cambio = false;
                    for (int i = numbers.length-2; i >= 0; i--){
                        for (int j = 0; j < numbers.length; j++){
                            if(numbers[i][j] != 0 && numbers[i+1][j] == 0){
                                numbers[i+1][j] = numbers[i][j];
                                numbers[i][j] = 0;
                                cambio = true;
                            } else if (numbers[i][j] != 0 && numbers[i+1][j] == numbers[i][j]) {
                                numbers[i+1][j] = numbers[i][j] + numbers[i][j];
                                numbers[i][j] = 0;
                                currentScore.setText(String.valueOf((Integer.parseInt((String) currentScore.getText())) + numbers[i+1][j]));
                                cambio = true;
                            }
                        }
                    }
                }
                break;
        }

        generateNew2(numbers);
        if(!checkMoreMoves(numbers)) {
            endGame();
        }
        adapterUpdate(matrizToArray(numbers));
        return numbers;
    }

    private boolean checkMoreMoves(int[][] numbers) {
        boolean emptyCell = false;

        for (int i = 0; i < numbers.length; i++){
            for (int j = 0; j < numbers.length; j++) {
                if(numbers[i][j] == 0) {
                    emptyCell = true;
                }

                // Comprobación de la casilla arriba
                if (i > 0) {
                    if(numbers[i][j] == numbers[i-1][j]){
                        return true;
                    }
                }

                // Comprobación de la casilla abajo
                if (i < 3) {
                    if(numbers[i][j] == numbers[i+1][j]){
                        return true;
                    }
                }

                // Comprobación de la casilla izquierda
                if (j > 0) {
                    if(numbers[i][j] == numbers[i][j-1]){
                        return true;
                    }
                }

                // Comprobación de la casilla derecha
                if (j < 3) {
                    if(numbers[i][j] == numbers[i][j+1]){
                        return true;
                    }
                }
            }
        }

        if(emptyCell) {
            return true;
        } else {
            return false;
        }
    }

    public void endGame() {
        try {
            playing = false;
            GameMessage messageFragment = new GameMessage(2);
            messageFragment.show(getChildFragmentManager(), "game_message_fragment");

            if(Integer.valueOf(String.valueOf(score.getText())) > Integer.valueOf(String.valueOf(maxScore.getText()))){
                maxScore.setText(score.getText());
                usuario.setBestScore2048(Integer.valueOf(String.valueOf(score.getText())));
                ((MainActivity) requireContext()).setUser(usuario);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.UpdateUserDAO().updateUser(usuario);
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error end game 2048: " + e);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(playing) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    endX = event.getX();
                    endY = event.getY();

                    float deltaX = endX - startX;
                    float deltaY = endY - startY;

                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            if (deltaX > 0) {
                                this.tileNumbers = moveTiles(1, this.tileNumbers);
                            } else {
                                this.tileNumbers = moveTiles(2, this.tileNumbers);
                            }
                        }
                    } else {
                        if (Math.abs(deltaY) > MIN_DISTANCE) {
                            if (deltaY > 0) {
                                this.tileNumbers = moveTiles(4, this.tileNumbers);
                            } else {
                                this.tileNumbers = moveTiles(3, this.tileNumbers);
                            }
                        }
                    }
                    break;
            }
        }
        return true;
    }
}
