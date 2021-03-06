package com.anandniketan.anandniketanskool360shilajTeacher.AsyncTasks;

import android.os.AsyncTask;

import com.anandniketan.anandniketanskool360shilajTeacher.Models.TimeTable.InsertLectureModel;
import com.anandniketan.anandniketanskool360shilajTeacher.Utility.AppConfiguration;
import com.anandniketan.anandniketanskool360shilajTeacher.WebServicesCall.WebServicesCall;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by admsandroid on 11/7/2017.
 */

public class InsertTimetableAsyncTask  extends AsyncTask<Void, Void, InsertLectureModel> {
    HashMap<String, String> param = new HashMap<String, String>();

    public InsertTimetableAsyncTask(HashMap<String, String> param) {
        this.param = param;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected InsertLectureModel doInBackground(Void... params) {
        String responseString = null;
        InsertLectureModel insertLectureModel = null;
        try {
            responseString = WebServicesCall.RunScript(AppConfiguration.getUrl(AppConfiguration.InsertTimetable), param);
            Gson gson = new Gson();
            insertLectureModel = gson.fromJson(responseString, InsertLectureModel.class);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return insertLectureModel;
    }

    @Override
    protected void onPostExecute(InsertLectureModel result) {
        super.onPostExecute(result);
    }
}

