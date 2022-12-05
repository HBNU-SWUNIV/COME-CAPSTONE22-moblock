package com.example.hyperledgervirtualmoneyproject.ui.shopList.ShopListRecycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyperledgervirtualmoneyproject.R;

import java.util.ArrayList;

public class ShopListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private ArrayList<ShopListPaintTitle> mDataset;

    public interface OnItemClickListener{
        void onItemClicked(int position, String data);
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public ShopListAdapter(ArrayList<ShopListPaintTitle> myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if(viewType == VIEW_TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_shoplist, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String data = "";
                    int position = itemViewHolder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        data = itemViewHolder.getAddress().toString();
                    }
                    itemClickListener.onItemClicked(position, data);
                }
            });
            return itemViewHolder;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String data = "";
                    int position = itemViewHolder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        data = itemViewHolder.getAddress();
                    }
                    itemClickListener.onItemClicked(position, data);
                }
            });
            return itemViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof ItemViewHolder){
            populateItemRows((ItemViewHolder) viewHolder, position);
        }
//        else if (viewHolder instanceof LoadingViewHolder){
//            showLoadingView((LoadingViewHolder) viewHolder, position);
//        }
    }

    public  int getItemCount(){
        return mDataset == null ? 0 : mDataset.size();
    }

    @Override
    public int getItemViewType(int position){
        return mDataset.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView shopImg;
        private TextView name, phoneNumber, address;

        public  ItemViewHolder(@NonNull View itemView){
            super(itemView);
            shopImg = (ImageView) itemView.findViewById(R.id.shopImg);
            name = (TextView) itemView.findViewById(R.id.shopName);
            phoneNumber = (TextView) itemView.findViewById(R.id.shopPhoneNumber);
            address = (TextView) itemView.findViewById(R.id.shopAddress);
        }

        public String getAddress() {
            return address.getText().toString();
        }
    }
//
//    private class LoadingViewHolder extends RecyclerView.ViewHolder {
//        private ProgressBar progressBar;
//
//        public LoadingViewHolder(@NonNull View itemView) {
//            super(itemView);
//            progressBar = itemView.findViewById(R.id.progressBar);
//        }
//    }

//    private void showLoadingView(LoadingViewHolder viewHolder, int position){}

    private void populateItemRows(ItemViewHolder viewHolder, int position){
        ShopListPaintTitle item = mDataset.get(position);
        viewHolder.shopImg.setImageBitmap(item.img);
        viewHolder.name.setText(item.name);
        viewHolder.phoneNumber.setText(item.phoneNumber);
        viewHolder.address.setText(item.address);
    }
}
