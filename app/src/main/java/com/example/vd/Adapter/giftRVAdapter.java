package com.example.vd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vd.R;
import com.example.vd.SignupBankdetail;

import java.util.ArrayList;
import java.util.HashMap;


public class giftRVAdapter extends RecyclerView.Adapter<giftRVAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mNames;
    private ArrayList<String> mrefferalmobno;
    private ArrayList<String> mImageUrls;
    private Context mContext;
    private HashMap<String, Object> musersignupDatamap;

    public giftRVAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls, ArrayList<String> disc, HashMap<String, Object> usersignupDatamap) {
        mNames = names;
        mImageUrls = imageUrls;
        mrefferalmobno = disc;
        mContext = context;
        musersignupDatamap = usersignupDatamap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.getgifts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext).asBitmap().load(mImageUrls.get(position)).into(holder.image);

        holder.name.setText(mNames.get(position));
        holder.mobno.setText(mrefferalmobno.get(position));

        holder.giftlyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                musersignupDatamap.put("giftName",mNames.get(position));
                musersignupDatamap.put("giftImage",mImageUrls.get(position));
                musersignupDatamap.put("giftDescription",mrefferalmobno.get(position));

                Intent categoryIntent = new Intent(view.getContext(), SignupBankdetail.class);

                // kyuki hum chahte h ki user jab koi category pr click karega to wo uss category me to jayega hi
                // but usko waha usss category ka naam bhi dikhega.....isliye name bhi pass karenge
                categoryIntent.putExtra("UserSignupData",musersignupDatamap);

                view.getContext().startActivity(categoryIntent);
                Log.d(TAG, "onClick: clicked on an image: " + musersignupDatamap);
                Log.d(TAG, "onClick: clicked on an image: " + mNames.get(position));
                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
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
        LinearLayout giftlyt;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.giftimg);
            name = itemView.findViewById(R.id.giftname);
            mobno = itemView.findViewById(R.id.giftdisc);
            giftlyt = itemView.findViewById(R.id.gift);
        }
    }
}
