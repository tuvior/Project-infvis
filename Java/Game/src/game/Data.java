package game;

import processing.core.*;

import java.awt.*;

public class Data{

    private Game parent;
    private PGraphics graphics;
    private int width;
    private int height;
    private Chart chart;
    public HScrollbar bar;
    private boolean started = false;

    private int timer;

    public float score = 0;
    private float lastScore = 0;

    public Data(int w, int h, Game parent){
        graphics = parent.createGraphics(w,h,PApplet.P2D);
        this.parent = parent;
        width = w;
        height = h;
        chart = new Chart(w - 2*h + 10, h - 25, parent);
        bar = new HScrollbar(2 * height - 15, parent.height - 15,  w - 2*h + 10, 10, parent);
    }

    public void draw(int x, int y){
        if (!parent.birdView && parent.millis() - timer >= 1000) {
            chart.updateScore(score);
            timer = parent.millis();
        }
        graphics.beginDraw();
        graphics.background(125);
        boardTopView();
        graphics.endDraw();
        parent.image(graphics, x, y);
        chart.draw(2 * height - 15, parent.height - height + 5, bar.getClampedPos());
        bar.update();
        bar.display();
    }

    private void boardTopView(){
        graphics.noStroke();
        graphics.fill(133, 158, 178);
        int previewSize = height - 10;
        graphics.rect(5, 5, previewSize, previewSize);
        graphics.fill(12,119,136);
        new Color(80, 98, 112);
        float ballx = parent.ball.location.x / (parent.boardSize / previewSize);
        float bally = parent.ball.location.z / (parent.boardSize / previewSize);
        graphics.ellipse(ballx + previewSize/2 + 5, bally + previewSize/2 + 5, previewSize/10, previewSize/10);
        graphics.fill(80, 98, 112);
        for (Cylinder cy : parent.cylinders) {
            float cyx = cy.location.x / (parent.boardSize / previewSize);
            float cyy = cy.location.y / (parent.boardSize / previewSize);
            graphics.ellipse(cyx + previewSize / 2 + 5, cyy + previewSize / 2 + 5, previewSize * Cylinder.radius*2 / parent.boardSize, previewSize * Cylinder.radius*2 / parent.boardSize);
        }
        graphics.fill(210);
        graphics.rect(10 + previewSize, 5, previewSize - 10, previewSize);
        graphics.fill(0);
        graphics.text("Total Score:", 15 + previewSize, 20);
        graphics.text(score, 15 + previewSize, 35);
        graphics.text("Velocity:", 15 + previewSize, 55);
        graphics.text(parent.ball.velocity.mag(), 15 + previewSize, 70);
        graphics.text("Last Score:", 15 + previewSize, 90);
        graphics.text(lastScore, 15 + previewSize, 105);
    }

    public void cylinderHit() {
        score += parent.ball.velocity.mag();
        if (score < 0) score = 0;
        lastScore = parent.ball.velocity.mag();
        started = true;
    }

    public void boundHit(){
        score -= parent.ball.velocity.mag();
        if (score < 0) score = 0;
        lastScore = -parent.ball.velocity.mag();
        started = true;
    }

}
