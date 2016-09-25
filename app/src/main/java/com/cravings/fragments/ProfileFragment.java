package com.cravings.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cravings.R;
import com.cravings.data.User;
import com.cravings.network.CraveAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mremondi on 7/25/16.
 */
public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.profile_fragment, null, false);

        final EditText etProfileName = (EditText) rootView.findViewById(R.id.etProfileName);
        final EditText etProfileEmail = (EditText) rootView.findViewById(R.id.etProfileEmail);
        final EditText etProfilePassword1 = (EditText) rootView.findViewById(R.id.etProfilePassword1);
        final EditText etProfilePassword2 = (EditText) rootView.findViewById(R.id.etProfilePassword2);


        final Button btnProfileChangePassword = (Button) rootView.findViewById(R.id.btnProfileChangePassword);
        Button btnProfileSaveChanges = (Button) rootView.findViewById(R.id.btnProfileSaveChanges);


        /*********************Load Profile**********************/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CraveAPI craveAPI = retrofit.create(CraveAPI.class);

        // GET CURRENT USER ID
        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", 0);
        final String user_id = prefs.getString("user_id", "");

        Call<User> loadUser = craveAPI.getUserProfile(user_id);
        loadUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                etProfileName.setText(response.body().getProfile().getName());
                etProfileEmail.setText(response.body().getEmail());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        /******************************************************/

        /*********************POST PROFILE CHANGES**********************/

        btnProfileSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<User> postUserChanges = craveAPI.updateUser(etProfileName.getText().toString(),
                                                                etProfileEmail.getText().toString(),
                                                                user_id);
                postUserChanges.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        etProfileName.setText(response.body().getProfile().getName());
                        etProfileEmail.setText(response.body().getEmail());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });

        /***************************************************************/

        /************************POST PASSWORD CHANGE***********************/

        btnProfileChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnProfileChangePassword.setText("Save New Password");
                etProfilePassword1.setVisibility(View.VISIBLE);
                etProfilePassword2.setVisibility(View.VISIBLE);

                btnProfileChangePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!etProfilePassword1.getText().toString().equals(etProfilePassword2.getText().toString())){
                            Toast.makeText(rootView.getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Call<User> updatePassword = craveAPI.updatePassword(etProfilePassword1.getText().toString(), user_id);
                        updatePassword.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                Toast.makeText(rootView.getContext(), "Password has been changed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                    }
                });
            }
        });
        /*******************************************************************/

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
