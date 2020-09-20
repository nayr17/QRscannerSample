package com.example.qrscannersample.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscannersample.Model.ListItem;
import com.example.qrscannersample.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterViewHolder>
{
    List<ListItem> listItemsArrayList;
    Context context;
    public Adapter(List<ListItem>listItemsArrayList, Context context)
    {
        this.listItemsArrayList = listItemsArrayList;
        this.context = context;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout,parent,false);
        return new AdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder holder, int position)
    {
        ListItem listItem = listItemsArrayList.get(position);

        holder.textViewType.setText(listItem.getType());
        holder.textViewCode.setText(listItem.getType());
        Linkify.addLinks(holder.textViewCode,Linkify.ALL);



    }

    @Override
    public int getItemCount()
    {
        return listItemsArrayList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewCode, textViewType;
        CardView cardView;
        public AdapterViewHolder(final View itemView)
        {
            super(itemView);
            textViewCode = (TextView)itemView.findViewById(R.id.textViewCode);
            textViewType = (TextView)itemView.findViewById(R.id.textViewType);
            cardView =(CardView)itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type = listItemsArrayList.get(getAdapterPosition()).getType();

                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_TEXT,type);
                    i.setType("text/plain");
                    itemView.getContext().startActivity(i);


                }
            });
        }



    }
}
