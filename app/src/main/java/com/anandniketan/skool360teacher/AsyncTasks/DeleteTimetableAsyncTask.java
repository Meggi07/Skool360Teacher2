package com.anandniketan.skool360teacher.AsyncTasks;

import android.os.AsyncTask;

import com.anandniketan.skool360teacher.Models.DeleteLectureModel;
import com.anandniketan.skool360teacher.Models.DeviceVersionModel;
import com.anandniketan.skool360teacher.Utility.AppConfiguration;
import com.anandniketan.skool360teacher.WebServicesCall.WebServicesCall;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by admsandroid on 11/7/2017.
 */

public class DeleteTimetableAsyncTask  extends AsyncTask<Void, Void, DeleteLectureModel> {
    HashMap<String, String> param = new HashMap<String, String>();

    public DeleteTimetableAsyncTask(HashMap<String, String> param) {
        this.param = param;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected DeleteLectureModel doInBackground(Void... params) {
        String responseString = null;
        DeleteLectureModel deleteLectureModel = null;
        try {
            responseString = WebServicesCall.RunScript(AppConfiguration.getUrl(AppConfiguration.DeleteTimetable), param);
            Gson gson = new Gson();
            deleteLectureModel = gson.fromJson(responseString, DeleteLectureModel.class);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return deleteLectureModel;
    }

    @Override
    protected void onPostExecute(DeleteLectureModel result) {
        super.onPostExecute(result);
    }
}
