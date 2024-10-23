package com.example.dreambound;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private Fae fae;
    private FaeDex faeDex;
    private ScrollView faeDexScrollView;
    private TextView faeListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        setContentView(gameView);

        //Overlay UI
        FrameLayout overlayLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.activity_main, null);
        addContentView(overlayLayout, new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        // Initialize com.example.dreambound.FaeDex
        faeDex = new FaeDex();

        // Create 10 unique Faes and add
        Fae fae1 = new Fae("Aeloria", "friendly", "Healing Light", "A gentle com.example.dreambound.Fae with healing powers.", 100, 150, 50, 50);
        Fae fae2 = new Fae("Nerion", "enemy", "Shadow Strike", "A cunning com.example.dreambound.Fae that attacks from the shadows.", 200, 250, 50, 50);
        // Add Faes
        faeDex.addFae(fae1);
        faeDex.addFae(fae2);
        // Add remaining Faes

        // Handle Bag button click
        faeDexScrollView = findViewById(R.id.faeDexScrollView);
        faeListTextView = findViewById(R.id.faeListTextView);
        Button bagButton = findViewById(R.id.bagButton);
        bagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faeDexScrollView.getVisibility() == View.GONE) {
                    StringBuilder faeNames = new StringBuilder();
                    for (Fae fae : faeDex.getFaeList()) {
                        faeNames.append(fae.getName()).append(" - ").append(fae.getType()).append("\n");
                        faeNames.append("Ability: ").append(fae.getAbility()).append("\n");
                        faeNames.append("Description: ").append(fae.getDescription()).append("\n\n");
                    }
                    faeListTextView.setText(faeNames.toString());
                    faeDexScrollView.setVisibility(View.VISIBLE);
                } else {
                    faeDexScrollView.setVisibility(View.GONE);
                }
            }
        });
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
