package com.anandniketan.skool360teacher.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admsandroid on 11/6/2017.
 */

public class UpdateTestDetailModel {

    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("FinalArray")
    @Expose
    private List<Object> finalArray = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Object> getFinalArray() {
        return finalArray;
    }

    public void setFinalArray(List<Object> finalArray) {
        this.finalArray = finalArray;
    }
}
