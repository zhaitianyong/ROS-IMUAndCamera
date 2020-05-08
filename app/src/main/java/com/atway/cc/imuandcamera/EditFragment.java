package com.atway.cc.imuandcamera;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atway.cc.imuandcamera.databinding.FragmentEditBinding;
import com.atway.cc.imuandcamera.db.RecordEntity;
import com.atway.cc.imuandcamera.db.RecordEntityViewModel;

import org.apache.commons.lang.StringUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {

    private RecordEntityViewModel recordViewModel;
    private FragmentEditBinding binding;

    private RecordEntity  recordEntity;
    private int id;
    private boolean isEdit=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("ID");
            isEdit = true;
        }else{
            isEdit = false;
        }
        Log.d("EditFragment", String.valueOf(isEdit));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recordViewModel = ViewModelProviders.of(this).get(RecordEntityViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false);
        // Inflate the layout for this fragment

        //        fragmentEditBinding.setModel(recordViewModel);
        //        fragmentEditBinding.setLifecycleOwner(this);
        if(isEdit){
            recordEntity = recordViewModel.findById(id);

            binding.editTextTitle.setText(recordEntity.getTitle());
            binding.editTextUrl.setText(recordEntity.getMasterUrl());
        }

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.editTextTitle.getText().toString();
                String masterUrl = binding.editTextUrl.getText().toString();

                // 添加
                if(StringUtils.isNotEmpty(title) &&
                    StringUtils.isNotEmpty(masterUrl)){
                    if(!isEdit){
                        recordEntity = new RecordEntity();
                        recordEntity.setTitle(title);
                        recordEntity.setMasterUrl(masterUrl);
                        recordViewModel.insertRecords(recordEntity);
                    }else{
                        recordEntity.setTitle(title);
                        recordEntity.setMasterUrl(masterUrl);
                        recordViewModel.updateRecords(recordEntity);
                    }
                    recordViewModel.getRecords();
                    Navigation.findNavController(v).navigateUp();
                }
            }
        });


        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
        return binding.getRoot();
    }


}
