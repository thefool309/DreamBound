package com.example.dreambound;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements CollisionHandler.CollisionListener {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = new GameView(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new GameFragment())
                .commit();
    }

    @Override
    public void onCollisionWithCreature() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new BattleFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
