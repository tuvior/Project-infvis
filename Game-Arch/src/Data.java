import processing.core.*;

import java.awt.*;


public class Data{

    private Game parent;
    private PGraphics graphics;
    private int height;
    private Chart chart;
    public HScrollbar bar;
    private int timer;

    public float score = 0;
    private float lastScore = 0;

    public Data(int w, int h, Game parent){
        graphics = parent.createGraphics(w,h,PApplet.P2D);
        this.parent = parent;
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
        graphics.fill(131, 191, 17);
        int previewSize = height - 10;
        graphics.rect(5, 5, previewSize, previewSize);
        graphics.fill(12,119,136);
        new Color(9, 162, 155);
        float ballx = parent.ball.location.x / (parent.boardSize / previewSize);
        float bally = parent.ball.location.z / (parent.boardSize / previewSize);
        graphics.ellipse(ballx + previewSize/2 + 5, bally + previewSize/2 + 5, previewSize/10, previewSize/10);
        graphics.fill(14, 101, 7);
        for (Arch arch : parent.archs) {
            float archx = arch.location.x / (parent.boardSize / previewSize);
            float archy = arch.location.y / (parent.boardSize / previewSize);
            graphics.ellipse(archx + previewSize / 2 + 15, archy + previewSize / 2 + 5, previewSize * Arch.sizeForPrev / parent.boardSize, previewSize * Arch.sizeForPrev / parent.boardSize);
            graphics.ellipse(archx + previewSize / 2 - 5, archy + previewSize / 2 + 5, previewSize * Arch.sizeForPrev / parent.boardSize, previewSize * Arch.sizeForPrev / parent.boardSize);
            
            //LINE BETWEEN THE 2 COOLUMS TO WORK...
            graphics.line(archx + previewSize / 2 + 15, archy + previewSize / 2 + 5, archx + previewSize / 2 - 5, archy + previewSize / 2 + 5); 
           
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

    public void archHit() {
        score += parent.ball.velocity.mag();
        if (score < 0) score = 0;
        lastScore = parent.ball.velocity.mag();
    }

    public void boundHit(){
        score -= parent.ball.velocity.mag();
        if (score < 0) score = 0;
        lastScore = -parent.ball.velocity.mag();
    }

}