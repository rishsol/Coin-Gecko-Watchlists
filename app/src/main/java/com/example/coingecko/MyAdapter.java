package com.example.coingecko;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<Model> coinList;
    Context context;

    public MyAdapter(ArrayList<Model> coinList, Context context) {
        this.coinList = coinList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Model model = coinList.get(position);
        holder.coinSymbol.setText(model.getSymbol());
        //holder.coinPrice.setText(0);
    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView coinSymbol;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coinSymbol = itemView.findViewById(R.id.coinSymbol);
            //coinPrice = itemView.findViewById(R.id.coinPrice);
        }
    }


}
