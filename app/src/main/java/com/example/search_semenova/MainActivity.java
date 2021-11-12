package com.example.search_semenova;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView mTextView = (TextView) findViewById(R.id.textView);
        EditText mEditText = (EditText) findViewById(R.id.mEditText);
        mProgressBar.setVisibility(View.VISIBLE);

        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
        final Call<GitResult> call = gitHubService.getUsers(mEditText.getText().toString());
        call.enqueue(new Callback<GitResult>() {
            @Override
            public void onResponse(Call<GitResult> call, Response<GitResult> response) {
                if (response.isSuccessful()){
                    GitResult result = response.body();
                    String user = "Аккаунт GitHub: " + result.getItems().get(0).getLogin();
                    mTextView.setText(user);
                    Log.i("Git", String.valueOf(result.getItems().size()));
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mTextView.setText(errorBody.string());
                        mProgressBar.setVisibility(View.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GitResult> call, Throwable throwable) {
                mTextView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }
}