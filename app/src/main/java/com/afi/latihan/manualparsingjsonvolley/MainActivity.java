package com.afi.latihan.manualparsingjsonvolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Url Endpoint for get data JSON
    public static String BASE_URL = "https://reqres.in/api/users?page=1";

    // get data array from model
    private ArrayList<User> list = new ArrayList<>();

    // Variable
    RecyclerView rvUser;
    ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Lookup the recyclerview in activity layout
        rvUser = findViewById(R.id.rv_user);
        // if true, to optimize size of recyclerview
        rvUser.setHasFixedSize(true);

        pbLoading = findViewById(R.id.pb_loading);

        // get data json from AsyncHttpClint
        getUser();
    }

    // Volley
    private void getUser(){
        pbLoading.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Jika Berhasil
                        pbLoading.setVisibility(View.INVISIBLE);
                        parseJson(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Jika Error
                        pbLoading.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
        queue.add(stringRequest);
    }

    // Manual Parsing json
    private void parseJson(String response){
        try {
            // untuk masuk ke JSONObject dengan tanda {} (kurung kurawal)
            JSONObject jsonObject  = new JSONObject(response);
            // untuk masuk ke JSONArray dengan tanda [] (kurung siku) dengan key "data" (key nya tergantung dengan json yang kalian pakai)
            JSONArray dataArray = jsonObject.getJSONArray("data");
            // lalu melakukan looping(pengulangan) dari data array tersebut dari awal sampai akhir data
            for (int i = 0; i < dataArray.length(); i++){
                // JSON object looping
                JSONObject dataObject = dataArray.getJSONObject(i);
                // get data with key harus sama persis dengan json
                int id = dataObject.getInt("id");
                String firstName = dataObject.getString("first_name");
                String email = dataObject.getString("email");
                String avatar = dataObject.getString("avatar");

                // set data to model
                User user = new User();
                user.setId(id);
                user.setFirstName(firstName);
                user.setEmail(email);
                user.setAvatar(avatar);

                // initialize User
                list.add(user);
            }
            // get LayoutManager
            showRecyclerList();
        }catch (JSONException e){
            Log.e("Volley", "unexpected JSON exception", e);
        }
    }

    public void showRecyclerList(){
        // Set layout manager to position the items
        rvUser.setLayoutManager(new LinearLayoutManager(this));
        // Create adapter passing in the sample user data
        ListUserAdapter listUserAdapter = new ListUserAdapter(list);
        // Attach the adapter to the recyclerview to populate items
        rvUser.setAdapter(listUserAdapter);
        pbLoading.setVisibility(View.GONE);
    }
}
