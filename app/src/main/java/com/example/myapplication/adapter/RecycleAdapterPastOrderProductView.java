package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.listeners.OnRecyclerViewClickListener;
import com.example.myapplication.model.DishObject;

import java.util.ArrayList;


/**
 * Created by Pradeep on 01-Sep-2019.
 */

public class RecycleAdapterPastOrderProductView extends RecyclerView.Adapter<RecycleAdapterPastOrderProductView.ViewHolder> {

    private Context context;
    private ArrayList<DishObject> listDish;

    private OnRecyclerViewClickListener clickListener;

    public RecycleAdapterPastOrderProductView(Context context, ArrayList<DishObject> listDish) {
        this.context = context;
        this.listDish = listDish;
    }

    public void setClickListener(OnRecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_past_order_product_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DishObject dishObject = listDish.get(position);

        holder.tvItemName.setText(dishObject.getProductName());
        holder.tvItemQuantity.setText(dishObject.getProductQuantity() + "");
    }

    @Override
    public int getItemCount() {
        return listDish.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvItemQuantity;

        public ViewHolder(View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tv_itemName);
            tvItemQuantity = itemView.findViewById(R.id.tv_itemQuantity);
        }

    }
}
