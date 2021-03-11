package com.papin.rxtasks;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View {

    private static final String TAG = "tag";
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this);
        //presenter.task1(); ok
        //presenter.task2(); ok
        //presenter.task3(); ok
        //presenter.task4(); ok
        //presenter.task11(); ok
        //presenter.task4();
    }


    @Override
    public void showResultFirst(List<Story> result) {
        Log.d(TAG, "showResultFirst: " + result.get(0).getAuthor());
    }

    @Override
    public void showResultSecond(List<Author> result) {
        String res="";
        for (Author author : result) {
            res+=author.getName()+" ";
        }
        Log.d(TAG, "showResultSecond: "+res);

    }
}