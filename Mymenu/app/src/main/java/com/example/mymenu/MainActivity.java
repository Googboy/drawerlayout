package com.example.mymenu;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    private MainUI mainUI;
    private leftmenu leftmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainUI = new MainUI(this);
        setContentView(mainUI);
        leftmenu = new leftmenu();

    }


}
