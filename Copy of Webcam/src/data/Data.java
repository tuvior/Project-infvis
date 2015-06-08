package data;

import object.Arch;
import game.Game;
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
    private float lastTopScore = 0;

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
            float arch1x = arch.lColumn.x / (parent.boardSize / previewSize);
            float arch1y = arch.lColumn.y / (parent.boardSize / previewSize);
            float arch2x = arch.rColumn.x / (parent.boardSize / previewSize);
            float arch2y = arch.rColumn.y / (parent.boardSize / previewSize);
            graphics.ellipse(arch1x + previewSize / 2 + 5, arch1y + previewSize / 2 + 5, previewSize * Arch.sizeForPrev / parent.boardSize, previewSize * Arch.sizeForPrev / parent.boardSize);
            graphics.ellipse(arch2x + previewSize / 2 + 5, arch2y + previewSize / 2 + 5, previewSize * Arch.sizeForPrev / parent.boardSize, previewSize * Arch.sizeForPrev / parent.boardSize);

            graphics.pushMatrix();
            graphics.translate(arch1x + previewSize / 2 + 5, arch1y + previewSize / 2 + 5);
            graphics.rotate(arch.rotation);
            graphics.rect(0, -1, 20, 2);
            graphics.popMatrix();
        }
        graphics.fill(210);
        graphics.rect(10 + previewSize, 5, previewSize - 10, previewSize);
        graphics.fill(0);
        graphics.text("Total Score:", 15 + previewSize, 20);
        graphics.text(score, 15 + previewSize, 35);
        graphics.text("Velocity:", 15 + previewSize, 55);
        graphics.text(parent.ball.velocity.mag(), 15 + previewSize, 70);
        graphics.text("Last Top Score:", 15 + previewSize, 90);
        graphics.text(lastTopScore, 15 + previewSize, 105);
    }


    public void archSuccess(){
        score += 10;
    }

    public void boundHit(){
        if(score > lastTopScore) lastTopScore = score;
        score -= 1;
        if (score < 0) score = 0;
    }

}