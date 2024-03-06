package com.example.gamecenter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class Adapter2048 extends ArrayAdapter<Integer> {
    private Activity context;
    private Integer[] numeros;
    public Adapter2048 (Activity context, Integer[] numeros){
        super(context, R.layout.squarenumber, numeros);
        this.numeros = numeros;
        this.context = context;
    }

    static class ViewHolder {
        TextView numero;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.squarenumber, parent, false);
            holder = new ViewHolder();
            holder.numero = convertView.findViewById(R.id.tile);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (numeros[position] != 0) {
            // Establecer el color de fondo según el número
            int color = getColorForNumber(numeros[position]);
            holder.numero.setBackgroundColor(color);
            holder.numero.setText(String.valueOf(numeros[position]));
        } else {
            holder.numero.setText("");
            holder.numero.setBackgroundColor(ContextCompat.getColor(context, R.color.colorCasillaVacia));
        }

        return convertView;
    }

    public Integer[] getNumeros() {
        return numeros;
    }
    public void setNumeros(Integer[] numeros) {
        this.numeros = numeros;
    }

    public int getColorForNumber(int number) {

        int[] colorsIds = {
                R.color.colorNumero2,
                R.color.colorNumero4,
                R.color.colorNumero8,
                R.color.colorNumero16,
                R.color.colorNumero32,
                R.color.colorNumero64,
                R.color.colorNumero128,
                R.color.colorNumero256,
                R.color.colorNumero512,
                R.color.colorNumero1024,
                R.color.colorNumero2048
        };

        for (int i = 0; i < colorsIds.length; i++){
            if ((number / 2) != 1){
                number = (number/2);

            } else {
                return ContextCompat.getColor(context, colorsIds[i]);
            }

            if(i == (colorsIds.length-1)){
                i = 0;
            }
        }

        return 0;
    }
}
