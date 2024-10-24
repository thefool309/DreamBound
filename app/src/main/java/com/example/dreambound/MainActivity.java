package com.example.dreambound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    private GameView gameView;
    private FaeDex faeDex;
    private LinearLayout bagMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);

        // Overlay UI setup
        FrameLayout overlayLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.activity_main, null);
        addContentView(overlayLayout, new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        // Initialize FaeDex
        faeDex = new FaeDex();

        // Create Faes
        Fae fae1 = new Fae("Aeloria", "friendly", "Healing Light", "A gentle Fae with healing powers.", 100, 150, 50, 50);
        Fae fae2 = new Fae("Nerion", "enemy", "Shadow Strike", "A cunning Fae that attacks from the shadows.", 200, 250, 50, 50);
        faeDex.addFae(fae1);
        faeDex.addFae(fae2);

        // Set up Bag button and submenu
        bagMenu = findViewById(R.id.bagMenu);
        Button bagButton = findViewById(R.id.bagButton);
        bagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bagMenu.getVisibility() == View.GONE) {
                    gameView.pause();
                    bagMenu.setVisibility(View.VISIBLE);
                } else {
                    bagMenu.setVisibility(View.GONE);
                    gameView.resume();
                }
            }
        });

        // FaeDex button to launch FaeDexActivity
        Button faeDexButton = findViewById(R.id.faeDexButton);
        faeDexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FaeDexActivity.class);
                intent.putExtra("faeDex", faeDex); // Pass faeDex as Serializable
                startActivity(intent);
            }
        });

        // Implement the functionality for other buttons as needed
        // Button itemsButton = findViewById(R.id.itemsButton);
        // Button partyButton = findViewById(R.id.partyButton);
        // Button saveLoadButton = findViewById(R.id.saveLoadButton);
        // Button settingsButton = findViewById(R.id.settingsButton);
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

