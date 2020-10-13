package com.example.game2048;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.game2048.Entity.URLInfo;
import com.example.game2048.databinding.FragmentRegisterBinding;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         binding = FragmentRegisterBinding.
                inflate(inflater, container, false);
        URLInfo urlInfo = new URLInfo();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlInfo.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final PlayGameInterface request = retrofit.create(PlayGameInterface.class);

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String userId       =   binding.userIdRegisterEditText.getText().toString().trim();
                System.out.println("userId: " + userId);
                String userName     =   binding.userNameRegisterEditText.getText().toString().trim();
                System.out.println("userName: " + userName);
                final String userPassword =   binding.userPasswordRegisterEditText.getText().toString().trim();
                String userGender   =   "None";
                if (binding.radioMan.isChecked()) userGender = "Man";
                else if (binding.radioWoman.isChecked()) userGender = "Woman";


                Call<String> addNewUserCall     =     request.addPlayUser(Long.parseLong(userId),
                        userName, userPassword, userGender);
                addNewUserCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().equalsIgnoreCase("No")) {
                            Toast.makeText(getActivity(), "The UserId has been register", Toast.LENGTH_SHORT).show();
                        } else if (response.body().equalsIgnoreCase("true")) {
                            Toast.makeText(getActivity(), "Register Success", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("userId", userId);
                            bundle.putString("userPassword", userPassword);
                            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment, bundle);
                        } else {
                            Toast.makeText(getActivity(), "User register occur error.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.i("Register", "user register error");
                    }
                });
            }
        });

        return binding.getRoot();
    }
}
