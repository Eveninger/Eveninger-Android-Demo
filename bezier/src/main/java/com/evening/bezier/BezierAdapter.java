package com.evening.bezier;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nighter on 17/7/20.
 */

public class BezierAdapter extends RecyclerView.Adapter<BezierAdapter.BezierViewHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mData;
    private OnItemClickListener mOnItemClickListener;

    public BezierAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public BezierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BezierViewHolder(mLayoutInflater.inflate(R.layout.item_bezier, parent, false));
    }

    @Override
    public void onBindViewHolder(BezierViewHolder holder, int position) {
        holder.bind(mData.get(position), mOnItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setData(List<String> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    static class BezierViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;

        public BezierViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.item_title);
        }

        public void bind(String title, final OnItemClickListener itemClickListener) {
            mTitle.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
