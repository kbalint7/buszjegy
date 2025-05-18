package com.mobilalk.buszjegy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TicketItemAdapter extends RecyclerView.Adapter<TicketItemAdapter.ViewHolder> {
    private ArrayList<TicketItem> mTicketItemsData;
    private ArrayList<TicketItem> mTicketItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    public TicketItemAdapter(Context context, ArrayList<TicketItem> itemsData) {
        this.mTicketItemsData = itemsData;
        this.mTicketItemsDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TicketItemAdapter.ViewHolder holder, int position) {
        TicketItem currentItem = mTicketItemsData.get(position);

        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mTicketItemsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleText;
        private TextView mDescriptionText;
        private TextView mPriceText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.itemTitle);
            mDescriptionText = itemView.findViewById(R.id.description);
            mPriceText = itemView.findViewById(R.id.price);
        }

        public void bindTo(TicketItem currentItem) {
            mTitleText.setText(currentItem.getTitle());
            mDescriptionText.setText(currentItem.getDescription());
            mPriceText.setText(currentItem.getPrice());

            itemView.findViewById(R.id.buy).setOnClickListener(view -> ((TicketListActivity)mContext).buyItem(currentItem));
        }
    }
}
