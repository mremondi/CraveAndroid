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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etLoginEmail = (EditText) findViewById(R.id.etLoginEmail);
        final EditText etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);

        SharedPreferences prefs = getSharedPreferences("UserData", 0);
        String email = prefs.getString("email", "");
        String user_id = prefs.getString("user_id", "");

        etLoginEmail.setText(email);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CraveAPI craveAPI = retrofit.create(CraveAPI.class);

        Button btnLogin = (Button) findViewById(R.id.btnLoginLogin);
        if (btnLogin != null){
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(etLoginEmail == null || etLoginPassword == null){
                        Log.d("UH OH", "BAD NULL");
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

                              SharedPreferences prefs = getSharedPreferences("UserData", 0);
                              SharedPreferences.Editor editor = prefs.edit();
                              editor.putString("email", email);
                              editor.putString("user_id", response.body().getId());
                              Log.d("USER ID", response.body().getId());
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
