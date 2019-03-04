package com.orbitview.salesmanagement.controller;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.orbitview.salesmanagement.R;
import com.orbitview.salesmanagement.model.Employee;
import com.orbitview.salesmanagement.model.LoginResponse;
import com.orbitview.salesmanagement.network.ApiInterface;
import com.orbitview.salesmanagement.network.InitRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText usernameText;
    private EditText passwordText;
    private Button loginButton;
    private AppController appController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

         appController = (AppController)getApplication();

         usernameText = findViewById(R.id.inputUserName);
         passwordText = findViewById(R.id.inputPassword);
         loginButton = findViewById(R.id.btnLogin);

         appController.userName = usernameText.getText().toString();
         loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String username = usernameText.getText().toString();
        final String password = passwordText.getText().toString();

        //Authentication Logic
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL[Config.BUILD_TYPE],
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("Response", response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String responseCode = jsonObject.getString("response_code");
//
//                            if(responseCode.equals("0")) {
//                                Employee employee = new Employee(
//                                        jsonObject.getString("first_name"),
//                                        jsonObject.getString("last_name"),
//                                        jsonObject.getString("phone_number"),
//                                        jsonObject.getString("email"),
//                                        jsonObject.getString("address")
//                                );
//
//                                LoginActivity.this.appController.employee = employee;
//                                LoginActivity.this.appController.userName = username;
//
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                startActivity(intent);
//                            }else
//                                //Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
//                                showMessage("Invalid Username or Password");
//                        } catch (JSONException e) {
//                            Log.e(TAG, "Exception "+e.getMessage());
//                            //Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
//                            showMessage("Invalid Username or Password");
//                        }
//
//                        progressDialog.hide();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Response", error.toString());
//                        progressDialog.hide();
//                        showMessage("Failed to logged in");
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("username", username);
//                params.put("password", password);
//                Log.i("Login Param ","Username: "+username);
//                Log.i("Login Param ","Password: "+password);
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
        ApiInterface apiInterface = InitRetrofit.getInstance();

        Call<LoginResponse> loginCall = apiInterface.doLogin(username, password);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.e("Response", String.valueOf(response.isSuccessful()));

                if(response.isSuccessful()) {
                    if(response.body().getResponseCode().equals("0")) {
                        Employee employee = new Employee(
                                response.body().getFirstName(),
                                response.body().getLastName(),
                                response.body().getPhoneNumber(),
                                response.body().getEmail(),
                                response.body().getAddress()
                        );

                        LoginActivity.this.appController.employee = employee;
                        LoginActivity.this.appController.userName = username;

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        //Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                        showMessage("Invalid Username or Password");
                    }
                } else {
//                    Log.e(TAG, "Exception "+response.body().getStatus());
                    //Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    showMessage("Failed to logged in");
                }

                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("Response", t.toString());
                progressDialog.hide();
                showMessage("Failed to logged in");
            }
        });

    }


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }


    public void onLoginFailed() {
        showMessage("login Failed");
        loginButton.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        usernameText.setText("");
        passwordText.setText("");
        usernameText.setFocusable(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if (username.isEmpty()) {
            usernameText.setError("enter a valid username");
            valid = false;
        } else {
            usernameText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("enter a valid password");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    private void showMessage(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContentView = (LinearLayout) toast.getView();
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.ic_info_outline_black_24dp);
        toastContentView.addView(imageView, 0);
        toast.show();

    }
}
