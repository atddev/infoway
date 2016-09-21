package com.example.atd.infoway;

import com.example.atd.infoway.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    static List<Item> dbList;
    static Context context;
    ItemsAdapter(Context context, List<Item> dbList ){
        this.dbList = new ArrayList<Item>();
        this.context = context;
        this.dbList = dbList;

    }

    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.ViewHolder holder, int position) {

        holder.name.setText(dbList.get(position).getName());
        holder.usern.setText(User.getInstance().getUsername());

        //decode image from base64 to bitmap
        byte[] decodedString = Base64.decode(dbList.get(position).getPic(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // set image
        holder.pict.setImageBitmap(decodedByte);

    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name,usern;
        public ImageView pict;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            name = (TextView) itemLayoutView
                    .findViewById(R.id.itname);
            usern = (TextView)itemLayoutView.findViewById(R.id.usern);
            itemLayoutView.setOnClickListener(this);

            pict = (ImageView) itemLayoutView
                    .findViewById(R.id.PicView);
        }

        @Override
        public void onClick(View v) {

            /*
            Intent intent = new Intent(context,DetailsActivity.class);

            Bundle extras = new Bundle();
            extras.putInt("position",getAdapterPosition());
            intent.putExtras(extras);

            /*
            int i=getAdapterPosition();
            intent.putExtra("position", getAdapterPosition());*/
            //context.startActivity(intent);

            Toast.makeText(ItemsAdapter.context, "you have clicked Row " + getAdapterPosition(), Toast.LENGTH_LONG).show();

        }
    }
}