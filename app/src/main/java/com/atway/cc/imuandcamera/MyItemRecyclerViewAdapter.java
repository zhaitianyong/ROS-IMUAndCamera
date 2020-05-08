package com.atway.cc.imuandcamera;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atway.cc.imuandcamera.db.RecordEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * TODO: Replace the implementation with code for your data type.
 */

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private  final String TAG = "RecyclerViewAdapter";
    private  List<RecordEntity> mValues=new ArrayList<>();
    private MutableLiveData<RecordEntity> deletedRecord;

    public LiveData<RecordEntity> getDeletedRecord() {
        if(deletedRecord==null){
            deletedRecord =  new MutableLiveData<>();
            deletedRecord.setValue(null);
        }

        return deletedRecord;
    }



    public void setmValues(List<RecordEntity> mValues) {
        this.mValues = mValues;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mUrlView.setText(mValues.get(position).getMasterUrl());

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (null != mListener) {
////                    // Notify the active callbacks interface (the activity, if the
////                    // fragment is attached to one) that an item has been selected.
////                    mListener.onListFragmentInteraction(holder.mItem);
////                }
//                Log.d(TAG, "onClick: ");
//
//            }
//        });


        holder.imageButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 首先判断是否可以链接

                Intent intent = new Intent(holder.mView.getContext(), CameraActivity.class);
                Bundle bundle = new Bundle();  //得到bundle对象
                bundle.putString("masterUrl", mValues.get(position).getMasterUrl());
                bundle.putString("title", mValues.get(position).getTitle());
                intent.putExtras(bundle); //通过intent将bundle传到另个Activity
                holder.mView.getContext().startActivity(intent);
                Log.d(TAG, "onClick: imageButtonStart");
            }
        });
        holder.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: imageButtonEdit");
                Bundle bundle = new Bundle();
                bundle.putInt("ID", mValues.get(position).getId());
                NavController navController = Navigation.findNavController(holder.mView);
                navController.navigate(R.id.action_itemFragment_to_editFragment, bundle);
            }
        });
        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletedRecord.setValue(mValues.get(position));
                Log.d(TAG, "onClick: imageButtonDelete");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mUrlView;

        public final ImageButton imageButtonEdit;
        public final ImageButton imageButtonStart;
        public final ImageButton imageButtonDelete;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.textViewTitle);
            mUrlView = (TextView) view.findViewById(R.id.textViewUrl);
            imageButtonDelete = view.findViewById(R.id.imageButtonDelete);
            imageButtonEdit = view.findViewById(R.id.imageButtonEdit);
            imageButtonStart = view.findViewById(R.id.imageButtonStart);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUrlView.getText() + "'";
        }
    }
}

