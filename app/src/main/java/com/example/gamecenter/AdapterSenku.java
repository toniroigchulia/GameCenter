package com.example.gamecenter;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class AdapterSenku extends ArrayAdapter<SenkuTile> {
    private Activity context;
    private SenkuTile[] senkuTiles;
    public AdapterSenku (Activity context, SenkuTile[] senkuTiles){
        super(context, R.layout.senkutile, senkuTiles);
        this.senkuTiles = senkuTiles;
        this.context = context;
    }

    static class ViewHolder {
        TextView senkuTiles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        ViewHolder holder = new ViewHolder();
        if(item == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            item = inflater.inflate(R.layout.senkutile, null);
            holder.senkuTiles = item.findViewById(R.id.tile);
            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(50);
        if(senkuTiles[position].isCorner()) {

            holder.senkuTiles.setVisibility(View.INVISIBLE);
        } else if(senkuTiles[position].isEmpty() && !senkuTiles[position].isPossible()) {

            gd.setColor(ContextCompat.getColor(context, R.color.emptySenkuTile));
            holder.senkuTiles.setBackground(gd);
        } else if(senkuTiles[position].isSelected()) {

            gd.setColor(ContextCompat.getColor(context, R.color.senkuTile));
            gd.setStroke(8, ContextCompat.getColor(context, R.color.selectedSenkuTile));
            holder.senkuTiles.setBackground(gd);
        } else if(senkuTiles[position].isPossible()) {

            gd.setColor(ContextCompat.getColor(context, R.color.selectedSenkuTile));
            holder.senkuTiles.setBackground(gd);
        } else {

            gd.setColor(ContextCompat.getColor(context, R.color.senkuTile));
            holder.senkuTiles.setBackground(gd);
        }

        return (item);
    }

    public SenkuTile[] getSenkuTiles() {
        return senkuTiles;
    }

    public void setSenkuTiles(SenkuTile[] senkuTiles) {
        this.senkuTiles = senkuTiles;
    }
}
