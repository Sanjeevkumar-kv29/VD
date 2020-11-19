package com.example.vd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vd.R;

import java.util.ArrayList;


/**
 * Created by User on 2/12/2018.
 */

public class childsRVAdapter extends RecyclerView.Adapter<childsRVAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mrefferalmobno = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;

    public childsRVAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls,ArrayList<String> refferalmobno) {
        mNames = names;
        mImageUrls = imageUrls;
        mrefferalmobno = refferalmobno;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.childrvlyt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext).asBitmap().load(mImageUrls.get(position)).into(holder.image);

        holder.name.setText(mNames.get(position));
        holder.mobno.setText(mrefferalmobno.get(position));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Intent categoryIntent = new Intent(view.getContext(), SubCategoryDetails.class);

                // kyuki hum chahte h ki user jab koi category pr click karega to wo uss category me to jayega hi
                // but usko waha usss category ka naam bhi dikhega.....isliye name bhi pass karenge

                //categoryIntent.putExtra("CategoryName", mNames.get(position));
                //view.getContext().startActivity(categoryIntent);

                //Log.d(TAG, "onClick: clicked on an image: " + mNames.get(position));
                //Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        TextView mobno;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.rvprofile_image);
            name = itemView.findViewById(R.id.rvname);
            mobno = itemView.findViewById(R.id.rvreferralmobno);
        }
    }
}
