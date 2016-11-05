package com.cravings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etRegisterEmail = (EditText) findViewById(R.id.etRegisterEmail);
        final EditText etRegisterPassword1 = (EditText) findViewById(R.id.etRegisterPassword1);
        final EditText etRegisterPassword2 = (EditText) findViewById(R.id.etRegisterPassword2);


        // set up retrofit
        final CraveAPI craveAPI = CraveConnection.setUpRetrofit();

        Button btnRegister = (Button) findViewById(R.id.btnRegisterRegister);
        if (btnRegister != null){
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(etRegisterEmail == null || etRegisterPassword1 == null || etRegisterPassword2 == null){
                        return;
                    }
                    else if(etRegisterEmail.getText() == null || etRegisterPassword1.getText() == null
                            || etRegisterPassword2.getText() == null){
                        Toast.makeText(RegisterActivity.this, "Please enter your email and password to login", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if (!etRegisterPassword1.getText().toString().equals(etRegisterPassword2.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final String email = etRegisterEmail.getText().toString();
                    final String password = etRegisterPassword1.getText().toString();

                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(password);


                    Call<User> userRegister = craveAPI.registerUser(email, password);
                    userRegister.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            // on success move to MainActivity

                            SharedPreferences prefs = getSharedPreferences("UserData", 0);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(LoginActivity.EMAIL, email);
                            editor.putString(LoginActivity.PASSWORD, password);
                            editor.putString(LoginActivity.USER_ID, response.body().getId());
                            editor.apply();

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {}
                    });
                }
            });
        }
    }
}
