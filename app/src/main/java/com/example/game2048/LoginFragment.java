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
import com.example.game2048.databinding.FragmentLoginBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private String playUserId;
    private String playUserName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentLoginBinding binding = FragmentLoginBinding.
                inflate(inflater, container, false);

        String userId         =       getArguments().getString("userId");
        String userPassword     =       getArguments().getString("userPassword");
        if (userId != null && userPassword != null) {
            binding.userIdLoginEditText.setText(userId);
            binding.userPasswordLoginEditText.setText(userPassword);
        }

        URLInfo urlInfo = new URLInfo();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlInfo.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final PlayGameInterface request = retrofit.create(PlayGameInterface.class);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                playUserId           =   binding.userIdLoginEditText.getText().toString().trim();
                String playUserPassword      =      binding.userPasswordLoginEditText.getText().toString().trim();
                Call<String> checkCall       =      request.checkResult(Long.valueOf(playUserId), playUserPassword);
                checkCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.i("UserCheck", "onResponse: " + response.body().toString());
                        System.out.println("UserCheck: " + response.body().toString());
                        if (response.body().equalsIgnoreCase("true")) {
                            Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();

                            Call<String> userNameCall = request.getUserNameById(Long.parseLong(playUserId));
                            userNameCall.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    playUserName = response.body();
                                    System.out.println("Play User Name: " + playUserName);
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.i("Login", "onFailure: " + t);
                                    Log.i("Get UserName Failure", "Get User Name by Id error");
                                }
                            });

                            Bundle bundle = new Bundle();
                            bundle.putString("userId", playUserId);
                            bundle.putString("userName", playUserName);
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_startFragment, bundle);
                        } else {
                            Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        System.out.println(t.toString());
                        System.out.println("UserCheck: " + "failed");
                        Log.i("UserCheck", "onFailure: " + "failed");
                    }
                });
            }
        });

        return binding.getRoot();
    }
}
