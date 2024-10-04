package com.example.dreambound;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class BattleGameView extends View {
    private Player player1;
    private Player player2;
    private Player player3;
    private CreatureEntity enemy1;
    private CreatureEntity enemy2;
    private CreatureEntity enemy3;

    public BattleGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        player1 = new Player(100, 600, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        player2 = new Player(100, 700, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        player3 = new Player(100, 800, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        enemy1 = new CreatureEntity(2200, 600, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        enemy2 = new CreatureEntity(2200, 700, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        enemy3 = new CreatureEntity(2200, 800, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        player1.draw(canvas);
        player2.draw(canvas);
        player3.draw(canvas);
        enemy1.draw(canvas);
        enemy2.draw(canvas);
        enemy3.draw(canvas);
    }
}
