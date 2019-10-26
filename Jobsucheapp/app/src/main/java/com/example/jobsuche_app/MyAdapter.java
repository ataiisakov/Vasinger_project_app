package com.example.jobsuche_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Job> mJobList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public MyAdapter(Context mContext, ArrayList<Job> mJobList) {
        this.mContext = mContext;
        this.mJobList = mJobList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Job currJob = mJobList.get(position);
        String imgUrl = currJob.getImgUrl();
        String description = currJob.getDescription();
        String bundesland = currJob.getBundesland();

        holder.mTextViewDesc.setText(description);
        holder.mTextViewLand.setText(bundesland);

        Picasso
                .get()
                .load(imgUrl).fit().centerInside().into(holder.mImageView);
     }

    @Override
    public int getItemCount() {
        return mJobList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView mImageView;
    TextView mTextViewDesc;
    TextView mTextViewLand;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageLogo);
            mTextViewDesc= itemView.findViewById(R.id.textDesc);
            mTextViewLand = itemView.findViewById(R.id.textLand);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
