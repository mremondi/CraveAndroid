package com.cravings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.cravings.data.User;
import com.cravings.network.CraveAPI;
import com.cravings.network.RetrofitConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tvSplashTitle = (TextView) findViewById(R.id.tvSplashTitle);
        Typeface font = Typeface.createFromAsset(getAssets(), "Milkshake.ttf");
        tvSplashTitle.setTypeface(font);

        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final Button btnRegister = (Button) findViewById(R.id.btnRegister);
        final SharedPreferences prefs = getSharedPreferences(LoginActivity.USER_DATA, 0);

        final CraveAPI craveAPI = RetrofitConnection.setUpRetrofit();

        if (prefs.getBoolean(LoginActivity.LOGGED_IN, false)){
            Call<User> loginUser = craveAPI.loginUser(
                    prefs.getString(LoginActivity.EMAIL,""),
                    prefs.getString(LoginActivity.PASSWORD, ""));
            loginUser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.d("USER LOGGED IN", response.body().getEmail());
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    btnLogin.setVisibility(View.VISIBLE);
                    btnRegister.setVisibility(View.VISIBLE);
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    });


                    btnRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        }
                    });
                }
            });
        }
        else{
            btnLogin.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.VISIBLE);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }
}
