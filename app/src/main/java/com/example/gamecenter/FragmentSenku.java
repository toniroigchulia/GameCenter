package com.example.gamecenter;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.gamecenter.BD.GameDB;
import com.example.gamecenter.BD.Usuario;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;

public class FragmentSenku extends Fragment {

    private SenkuTile[][] senkuTiles = new SenkuTile[7][7];
    private GridView tileList;
    private AdapterSenku tileAdapter;
    private boolean playing;
    private SenkuTile[] senkuArrayAnterior;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private static final long START_TIME_IN_MILLIS = 10 * 60 * 1001;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private Button lastMove;
    private TextView currentTime;
    private TextView recordTime;

    GameDB db;
    private ExecutorService executor;
    private Usuario usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_senku, container, false);
        tileList = rootView.findViewById(R.id.senkuTileList);
        lastMove = rootView.findViewById(R.id.lastMoveButton);
        timerTextView = rootView.findViewById(R.id.timerTextView);

        currentTime = rootView.findViewById(R.id.timerTextView);
        recordTime = rootView.findViewById(R.id.timerRecordTextView);

        this.db = ((MainActivity) requireActivity()).db;
        this.executor = ((MainActivity) requireActivity()).executor;
        this.usuario = ((MainActivity) requireActivity()).user;

        recordTime.setText(milisegundosATiempoCadena(usuario.getBestTimeSenku()));
        startTimer();

        setUtilButtons(rootView);
        tileAdapter = new AdapterSenku(getActivity(), initGame());
        tileList.setAdapter(tileAdapter);
        tileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                SenkuTile[] senkuTilesArrayClick = matrizToArray(senkuTiles);
                if (!senkuTilesArrayClick[position].isCorner() && playing){
                    // Movimiento si nos queremos mover a una casilla con posible movimiento
                    if (senkuTilesArrayClick[position].isPossible() && playing){
                        setSenkuArrayAnterior(senkuTilesArrayClick);
                        senkuTilesArrayClick = realizateMove(senkuTilesArrayClick, position);
                        senkuTilesArrayClick[position].setEmpty(false);
                    }

                    // Comprobamos si el jugador ha ganado
                    if (checkWin(senkuTilesArrayClick)) {
                        endGame(1);
                    }

                    // Comprabamos si no se pueden realizar mas movimientos
                    if(!checkMoreMoves(senkuTilesArrayClick) && playing){
                        endGame(2);
                    }

                    // Seleccionamos una casilla valida para ver sus possibles movimientos
                    senkuTilesArrayClick = deselectAll(senkuTilesArrayClick);
                    if(!senkuTilesArrayClick[position].isEmpty() && playing){
                        senkuTilesArrayClick[position].setSelected(true);
                        senkuTilesArrayClick = possibleMove(senkuTilesArrayClick);
                    }

                    adapterUpdate(senkuTilesArrayClick);
                }
            }
        });

        return rootView;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                endGame(2);
            }
        }.start();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void resetTimer() {
        stopTimer();
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        startTimer();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private SenkuTile[] initGame() {
        playing = true;
        lastMove.setEnabled(false);
        SenkuTile[] senkuTilesArray = matrizToArray(senkuTiles);
        for (int i = 0; i < senkuTilesArray.length; i++){

            if (checkCorner(i)){
                senkuTilesArray[i] = new SenkuTile(false, true);
            } else {
                if (i == 24){
                    senkuTilesArray[i] = new SenkuTile(true, false);
                } else {
                    senkuTilesArray[i] = new SenkuTile();
                }
            }
        }
        senkuTiles = arrayToMatriz(senkuTilesArray);
        return  senkuTilesArray;
    }

    private void setUtilButtons(View rootView) {
        Button backButton = rootView.findViewById(R.id.backButton);
        Button lastMoveButton = rootView.findViewById(R.id.lastMoveButton);
        Button restarButton = rootView.findViewById(R.id.resetButton);

        //Boton para volver al ultimo movimiento
        lastMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing){
                    senkuTiles = arrayToMatriz(senkuArrayAnterior);
                    adapterUpdate(senkuArrayAnterior);
                    lastMove.setEnabled(false);
                }
            }
        });

        //Boton para reiniciar la partida
        restarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterUpdate(initGame());
                resetTimer();
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

    private void adapterUpdate(SenkuTile[] tilesArray) {
        this.tileAdapter.setSenkuTiles(tilesArray);
        this.tileAdapter.notifyDataSetChanged();
    }

    private boolean checkCorner(int j){
        int[] checkPosition = {0,1,5,6,7,8,12,13,35,36,40,41,42,43,47,48};
        for (int i = 0; i < checkPosition.length; i++){
            if (j == checkPosition[i]){
                return true;
            }
        }
        return false;
    }

    private SenkuTile[] matrizToArray (SenkuTile[][] senkuTiles) {
        SenkuTile[] tilesArray = new SenkuTile[49];
        int arrayIndex = 0;

        for (int i = 0; i < senkuTiles.length; i++){
            for (int j = 0; j < senkuTiles[i].length; j ++){
                tilesArray[arrayIndex] = senkuTiles[i][j];
                arrayIndex = arrayIndex + 1;
            }
        }

        return tilesArray;
    }

    private SenkuTile[][] arrayToMatriz (SenkuTile[] senkuTiles){
        SenkuTile[][] matrizSenku = new SenkuTile[7][7];
        int arrayIndex = 0;
        for (int i = 0; i < matrizSenku.length; i++){
            for (int j = 0; j < matrizSenku[i].length; j ++){
                matrizSenku[i][j] = senkuTiles[arrayIndex];
                arrayIndex = arrayIndex + 1;
            }
        }

        return matrizSenku;
    }

    private int[] arrayPositionToMatrizPosition (int position, SenkuTile[][] matrizSenku) {
        int[] matrizArrayPosition = new int[2];
        int arrayIndex = 0;
        for (int i = 0; i < matrizSenku.length; i++){
            for (int j = 0; j < matrizSenku[i].length; j ++){
                if (arrayIndex == position){
                    matrizArrayPosition[0] = i;
                    matrizArrayPosition[1] = j;
                }
                arrayIndex = arrayIndex + 1;
            }
        }

        return  matrizArrayPosition;
    }

    private SenkuTile[] realizateMove (SenkuTile[] senkuTiles, int movePosition){
        SenkuTile[][] matrizSenku = arrayToMatriz(senkuTiles);
        int[] matrizArrayPosition = arrayPositionToMatrizPosition(movePosition, matrizSenku);

        for (int i = 0; i < matrizSenku.length; i++){
            for (int j = 0; j < matrizSenku[i].length; j ++){
                if (matrizSenku[i][j].isSelected()){
                    try {
                        if(i == matrizArrayPosition[0]){
                            if (j < matrizArrayPosition[1]){
                                matrizSenku[i][j+1].setVoid();
                            } else {
                                matrizSenku[i][j-1].setVoid();
                            }
                        } else {
                            if (i < matrizArrayPosition[0]){
                                matrizSenku[i+1][j].setVoid();
                            } else {
                                matrizSenku[i-1][j].setVoid();
                            }
                        }
                    } catch (Exception e){

                    }
                    matrizSenku[i][j].setVoid();
                }
            }
        }

        return matrizToArray(matrizSenku);
    }

    private SenkuTile[] possibleMove (SenkuTile[] senkuTiles){
        SenkuTile[][] matrizSenku = arrayToMatriz(senkuTiles);
        int[] direcciones = new int[]{1, -1, 1, -1};

        for (int i = 0; i < matrizSenku.length; i++){
            for (int j = 0; j < matrizSenku[i].length; j ++){
                if(matrizSenku[i][j].isSelected()){
                    for (int k = 0; k < direcciones.length; k++){
                        try {
                            if(k == 0 || k == 1){
                                if((!matrizSenku[i][j+(direcciones[k])].isEmpty() &&
                                        matrizSenku[i][j+(direcciones[k]*2)].isEmpty()) && !(matrizSenku[i][j+(direcciones[k]*2)].isCorner() || matrizSenku[i][j+(direcciones[k])].isCorner())) {

                                    matrizSenku[i][j+(direcciones[k]*2)].setPossible(true);
                                }
                            } else {
                                if((!matrizSenku[i+(direcciones[k])][j].isEmpty() &&
                                        matrizSenku[i+(direcciones[k]*2)][j].isEmpty()) && !(matrizSenku[i+(direcciones[k]*2)][j].isCorner() || matrizSenku[i+(direcciones[k])][j].isCorner())) {

                                    matrizSenku[i+(direcciones[k]*2)][j].setPossible(true);
                                }
                            }
                        } catch (Exception e){

                        }
                    }
                }
            }
        }

        return matrizToArray(matrizSenku);
    }

    private SenkuTile[] deselectAll(SenkuTile[] senkuTiles){
        for (int i = 0; i < senkuTiles.length; i++){
            if(senkuTiles[i].isSelected()){
                senkuTiles[i].setSelected(false);
            } else if(senkuTiles[i].isPossible()){
                senkuTiles[i].setPossible(false);
            }
        }

        return senkuTiles;
    }

    private boolean checkWin(SenkuTile[] senkuTiles){
        boolean oneBall = false;

        for (int i = 0; i < senkuTiles.length; i++) {
            System.out.println(!senkuTiles[i].isEmpty() + " " + !senkuTiles[i].isCorner());
            if(!senkuTiles[i].isEmpty() && !oneBall && !senkuTiles[i].isCorner()) {

                oneBall = true;
            } else if (!senkuTiles[i].isEmpty() && oneBall && !senkuTiles[i].isCorner()) {

                return false;
            }
        }

        return true;
    }

    private boolean checkMoreMoves(SenkuTile[] senkuTiles){
        for (int i = 0; i < senkuTiles.length; i++){
            senkuTiles = deselectAll(senkuTiles);

            if(!senkuTiles[i].isCorner()) {
                if(!senkuTiles[i].isEmpty()){
                    senkuTiles[i].setSelected(true);
                }

                senkuTiles = possibleMove(senkuTiles);

                for (int j = 0; j < senkuTiles.length; j++) {
                    if (senkuTiles[j].isPossible()) {
                        return true;
                    }
                }
            }

            senkuTiles = deselectAll(senkuTiles);
        }

        return false;
    }

    private void endGame (int caso) {
        GameMessage messageFragment = new GameMessage(caso);
        messageFragment.show(getChildFragmentManager(), "game_message_fragment");
        playing = false;
        lastMove.setEnabled(false);
        stopTimer();

        if(caso == 1){
            if((tiempoCadenaAMilisegundos(String.valueOf(currentTime.getText()))) > (tiempoCadenaAMilisegundos(String.valueOf(recordTime.getText())))){
                recordTime.setText(String.valueOf(currentTime.getText()));
                usuario.setBestTimeSenku(tiempoCadenaAMilisegundos(String.valueOf(recordTime.getText())));
                ((MainActivity) requireContext()).setUser(usuario);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.UpdateUserDAO().updateUser(usuario);
                    }
                });
            }
        }
    }

    public void setSenkuArrayAnterior(SenkuTile[] newSenkuArrayAnterior){
        this.senkuArrayAnterior = Arrays.copyOf(newSenkuArrayAnterior, newSenkuArrayAnterior.length);
        for (int i = 0; i < senkuArrayAnterior.length; i++) {
            this.senkuArrayAnterior[i] = new SenkuTile(newSenkuArrayAnterior[i]);
        }
        lastMove.setEnabled(true);
    }

    public int tiempoCadenaAMilisegundos(String tiempoCadena) {
        String[] partes = tiempoCadena.split(":");
        int minutos = Integer.parseInt(partes[0]);
        int segundos = Integer.parseInt(partes[1]);
        return (minutos * 60 + segundos) * 1000;
    }

    public String milisegundosATiempoCadena(int milisegundos) {
        int minutos = (int) (milisegundos / 1000) / 60;
        int segundos = (int) (milisegundos / 1000) % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }
}
