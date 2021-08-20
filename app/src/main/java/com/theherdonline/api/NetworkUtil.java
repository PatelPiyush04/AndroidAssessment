package com.theherdonline.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.theherdonline.db.entity.ResError;
import okhttp3.ResponseBody;

public class NetworkUtil {


    public static ResError parseErrorResponse(String body)
    {
        ResError resError = new ResError();
        String message = "unknown error";
        String error = "unknown error";

        if (body != null)
        {
            JSONObject jsonObject = null;
            resError.setMsg(body);
            resError.setError(body);
            try {
                jsonObject = new JSONObject(body);
            }
            catch (JSONException e)
            {
                resError.setMsg(body);
                resError.setError(body);
            }
            try {
                message = jsonObject.getString("message");
                resError.setMsg(message);
            }
            catch (JSONException e) {
            }
            try {
                error = jsonObject.getString("error");
                resError.setError(error);
            }
            catch (JSONException e)
            {
            }

        }
        return resError;
    }

    public static String parseErrorResponse(ResponseBody response)
    {
        String message = "";
        if (response != null)
        {
            try {
                ResError resError = NetworkUtil.parseErrorResponse(response.string());
                message = resError.getMsg();
            }
            catch (IOException e)
            {
                message = e.getMessage();
            }
        }
        return message;
    }

    public static ResError parseErrorResponseV2(ResponseBody response)
    {

        ResError resError = new ResError();
        if (response != null)
        {
            try {
                resError = NetworkUtil.parseErrorResponse(response.string());
                //message = resError.getMsg();
            }
            catch (IOException e)
            {
                resError.setMsg(e.getMessage());

            }
        }
        return resError;
    }

    public static String parseUrl(String url)
    {
        boolean bTest = true;
        return bTest ? "/mockapi" + url : url;

    }

}
