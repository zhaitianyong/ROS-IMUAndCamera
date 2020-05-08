package com.atway.cc.imuandcamera.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>存储的数据内容
 * TODO: Replace all uses of this class before publishing your app.
 */
public class RecordEntityViewModel extends AndroidViewModel {

    private RecordEntityDao recordEntityDao;


    private LiveData<List<RecordEntity>> records;

    public RecordEntityViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        recordEntityDao = appDatabase.getRecordEntityDao();

    }

    public RecordEntity findById(int id){
        return  recordEntityDao.findById(id);
    }

    public LiveData<List<RecordEntity>> getRecords() {
        records = recordEntityDao.getAll();
        return records;
    }
    public void insertRecords(RecordEntity... recordEntities){
        new InsertAsyncTask(recordEntityDao).execute(recordEntities);
    }
    public void updateRecords(RecordEntity... recordEntities){
        new UpdateAsyncTask(recordEntityDao).execute(recordEntities);
    }
    public void deleteRecords(RecordEntity... recordEntities){
        new DeleteAsyncTask(recordEntityDao).execute(recordEntities);
    }


    static class InsertAsyncTask extends AsyncTask<RecordEntity, Void, Void>{
        private  RecordEntityDao recordEntityDao;
        InsertAsyncTask(RecordEntityDao recordEntityDao){

            this.recordEntityDao = recordEntityDao;
        }


        @Override
        protected Void doInBackground(RecordEntity... recordEntities) {
            this.recordEntityDao.insert(recordEntities);
            return null;
        }
    }
    static class UpdateAsyncTask extends AsyncTask<RecordEntity, Void, Void>{
        private  RecordEntityDao recordEntityDao;
        UpdateAsyncTask(RecordEntityDao recordEntityDao){

            this.recordEntityDao = recordEntityDao;
        }


        @Override
        protected Void doInBackground(RecordEntity... recordEntities) {
            recordEntityDao.update(recordEntities);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<RecordEntity, Void, Void>{
        private  RecordEntityDao recordEntityDao;
        DeleteAsyncTask(RecordEntityDao recordEntityDao){

            this.recordEntityDao = recordEntityDao;
        }


        @Override
        protected Void doInBackground(RecordEntity... recordEntities) {
            recordEntityDao.delete(recordEntities);
            return null;
        }
    }
}
