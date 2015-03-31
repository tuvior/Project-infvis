package game;

import processing.core.*;

import java.util.LinkedList;

public class Chart {

    private PGraphics graphics;
    private int height;
    private int width;
    private Game parent;
    private LinkedList<Float> scores;

    public Chart (int w, int h, Game parent) {
        graphics = parent.createGraphics(w,h, PApplet.P2D);
        this.parent = parent;
        width = w;
        height = h;
        scores = new LinkedList<>();
    }

    public void draw(int x, int y, float compression){
        graphics.beginDraw();
        graphics.noStroke();
        graphics.background(200);
        graphics.fill(34, 107, 171);
        int i = 0;
        for (float score : scores){
            drawColumn(score, i*(5*compression +1), compression);
            i++;
        }
        graphics.endDraw();
        parent.image(graphics, x, y);
    }

    public void drawColumn(float score, float x, float compression){
        int numRectangles = (int)score/ 3;
        for (int i = 0; i < numRectangles; i++) {
            graphics.rect(x, height - i*6 - 5, 5*compression, 5);
        }

    }



    public void updateScore(float score){
        scores.add(score);
    }
}
