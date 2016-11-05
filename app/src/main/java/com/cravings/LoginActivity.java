package com.cravings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.cravings.data.User;
import com.cravings.network.CraveAPI;
import com.cravings.network.CraveConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static final String USER_DATA = "USER_DATA";
    public static final String USER_ID = "USER_ID";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";
    public static final String LOGGED_IN = "LOGGED_IN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etLoginEmail = (EditText) findViewById(R.id.etLoginEmail);
        final EditText etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);

        SharedPreferences prefs = getSharedPreferences(USER_DATA, 0);
        String email = prefs.getString(EMAIL, "");
        String password = prefs.getString(PASSWORD, "");
        String user_id = prefs.getString(USER_ID, "");

        etLoginEmail.setText(email);
        etLoginPassword.setText(password);


        // set up retrofit
        final CraveAPI craveAPI = CraveConnection.setUpRetrofit();

        Button btnLogin = (Button) findViewById(R.id.btnLoginLogin);
        if (btnLogin != null){
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(etLoginEmail == null || etLoginPassword == null){
                        return;
                    }
                    else if(etLoginEmail.getText() == null || etLoginPassword.getText() == null){
                        Toast.makeText(LoginActivity.this, "Please enter your email and password to login", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final String email = etLoginEmail.getText().toString();
                    final String password = etLoginPassword.getText().toString();

                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(password);


                    Call<User> userLogin = craveAPI.loginUser(email, password);
                    userLogin.enqueue(new Callback<User>() {
                          @Override
                          public void onResponse(Call<User> call, Response<User> response) {
                              // on success move to MainActivity
                              if(response.body() == null){
                                  Toast.makeText(LoginActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                                  return;
                              }

                              SharedPreferences prefs = getSharedPreferences(USER_DATA, 0);
                              SharedPreferences.Editor editor = prefs.edit();
                              editor.putString(EMAIL, email);
                              editor.putString(PASSWORD, password);
                              editor.putString(USER_ID, response.body().getId());
                              editor.putBoolean(LOGGED_IN, true);
                              editor.apply();

                              Intent i = new Intent(getApplicationContext(), MainActivity.class);
                              startActivity(i);
                          }

                          @Override
                          public void onFailure(Call<User> call, Throwable t) {
                              // on failure retry
                              Log.d("THROWS", t.getMessage());
                              Toast.makeText(LoginActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                          }
                      });
                }
            });
        }
    }
}
