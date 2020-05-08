package com.atway.cc.imuandcamera;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atway.cc.imuandcamera.db.RecordEntity;
import com.atway.cc.imuandcamera.db.RecordEntityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/> 列表数据，并且缓存到文件中
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class ItemFragment extends Fragment {

    RecordEntityViewModel recordViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);


        // view model
        recordViewModel =  ViewModelProviders.of(this).get(RecordEntityViewModel.class);

        // 监听加载数据
        recordViewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<List<RecordEntity>>() {
            @Override
            public void onChanged(List<RecordEntity> recordEntities) {
                adapter.setmValues(recordEntities);
                adapter.notifyDataSetChanged();
            }
        });

        // 监听删除数据
        adapter.getDeletedRecord().observe(getViewLifecycleOwner(), new Observer<RecordEntity>() {
            @Override
            public void onChanged(RecordEntity recordEntity) {
                if(recordEntity!=null){

                    AlertDialog.Builder alterDialog = new AlertDialog.Builder(getContext());
                    alterDialog.setTitle("提示");
                    alterDialog.setMessage("确定要删除吗？");
                    alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            recordViewModel.deleteRecords(recordEntity);
                            recordViewModel.getRecords();
                        }
                    });
                    alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alterDialog.show();

                }
            }
        });

        // 新增
        FloatingActionButton fbtnAdd = view.findViewById(R.id.fbtnAdd);
        fbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                NavController navController = Navigation.findNavController(view);
                //NavController navController = Navigation.findNavController(vi, R.id.nav_host_fragment);
                navController.navigate(R.id.action_itemFragment_to_editFragment);
            }
        });


        return view;
    }

}
