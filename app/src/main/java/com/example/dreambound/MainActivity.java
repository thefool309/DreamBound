package com.example.dreambound;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Player and Enemy stats
    private int playerHealth = 100;
    private int enemyHealth = 100;
    private int playerAttackPower = 20;
    private int enemyAttackPower = 15;
    private boolean playerDefending = false;

    private Random random = new Random();

    // UI Elements
    private TextView playerHealthText;
    private TextView enemyHealthText;
    private TextView battleLogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind UI Elements
        playerHealthText = findViewById(R.id.player_health);
        enemyHealthText = findViewById(R.id.enemy_health);
        battleLogText = findViewById(R.id.battle_log);

        Button attackButton = findViewById(R.id.attack_button);
        Button defendButton = findViewById(R.id.defend_button);

        // Attack Button Logic
        attackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerAttack();
                if (enemyHealth > 0) {
                    enemyTurn();
                }
            }
        });

        // Defend Button Logic
        defendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerDefending = true;
                battleLogText.setText("You brace for an attack!");
                enemyTurn();
            }
        });
    }

    // Player's Attack
    private void playerAttack() {
        int damage = random.nextInt(playerAttackPower) + 5; // Random damage
        enemyHealth -= damage;
        if (enemyHealth < 0) enemyHealth = 0;
        updateUI();
        battleLogText.setText("You attacked the enemy for " + damage + " damage!");
    }

    // Enemy's Turn
    private void enemyTurn() {
        int damage = random.nextInt(enemyAttackPower) + 5;
        if (playerDefending) {
            damage /= 2; // Half damage if defending
            playerDefending = false; // Reset defending after one turn
        }
        playerHealth -= damage;
        if (playerHealth < 0) playerHealth = 0;
        updateUI();
        battleLogText.setText("Enemy attacked you for " + damage + " damage!");
    }

    // Update UI
    private void updateUI() {
        playerHealthText.setText("Player Health: " + playerHealth);
        enemyHealthText.setText("Enemy Health: " + enemyHealth);
        checkGameOver();
    }

    // Check for Game Over
    private void checkGameOver() {
        if (playerHealth <= 0) {
            battleLogText.setText("You have been defeated!");
            disableButtons();
        } else if (enemyHealth <= 0) {
            battleLogText.setText("You defeated the enemy!");
            disableButtons();
        }
    }

    // Disable buttons after the game ends
    private void disableButtons() {
        findViewById(R.id.attack_button).setEnabled(false);
        findViewById(R.id.defend_button).setEnabled(false);
    }
}
