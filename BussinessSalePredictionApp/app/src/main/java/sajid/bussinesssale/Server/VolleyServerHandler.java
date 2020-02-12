package sajid.bussinesssale.Server;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import sajid.bussinesssale.Controller;
import sajid.bussinesssale.Interfaces.ResponseCallBack;

public class VolleyServerHandler {

    public static void getServerData(final ResponseCallBack serverResponse) {
        RequestQueue queue = Volley.newRequestQueue(Controller.activity);

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, ServerConfig.LIST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JSONresponse) {
                        serverResponse.onSuccess(JSONresponse);
//                        Utils.showResponse(JSONresponse);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        serverResponse.onFailure("Error Loading Server Data");
                    }
                }
            );
        Log.e("URL",ServerConfig.LIST_URL);

        queue.add(jsObjRequest);
    }

    public static void isServerUpdatesAvailable(int DB_version,final ResponseCallBack serverResponse) {
        RequestQueue queue = Volley.newRequestQueue(Controller.activity);

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                ServerConfig.CHECKUPDATES_URL + DB_version + "/", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JSONresponse) {
                        boolean status = false;
                        try {
                            status = JSONresponse.getBoolean(ServerConfig.RESPONSE.UPDATEAVAILABLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        serverResponse.onSuccess(status);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        serverResponse.onFailure("Checking Updates Failed");
                    }
                }
            );

        Log.e("URL",ServerConfig.CHECKUPDATES_URL + DB_version + "/");

        queue.add(jsObjRequest);
    }
//    public static void signupUser(String username, String password) {
//        RequestQueue queue = Volley.newRequestQueue(Controller.activity);
//
//        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
////                URL
//                , null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject JSONresponse) {
//                        boolean status = false;
//                        try {
//                            status = JSONresponse.getBoolean(ServerConfig.RESPONSE.UPDATEAVAILABLE);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        serverResponse.onSuccess(status);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                serverResponse.onFailure("Checking Updates Failed");
//            }
//        }
//        );
//
//        Log.e("URL",ServerConfig.CHECKUPDATES_URL + DB_version + "/");
//
//        queue.add(jsObjRequest);
//    }
}

// Model View Controller
// POJO
//        plain old java object
//        Singleton Class


