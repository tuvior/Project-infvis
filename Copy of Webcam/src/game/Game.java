package game;

import data.Data;
import object.Arch;
import object.MovingBall;
import processing.core.*;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;



import ImageProc.ImageProcessing;



public class Game extends PApplet {

    public MovingBall ball;
    public ArrayList<Arch> archs;
    private float rotateY = 0;
    private float rotateX = 0;
    private float rotateZ = 0;
    private float prevMouseX;
    private float prevMouseY;
    public boolean birdView = false;
    private float boundX1;
    private float boundX2;
    private float boundZ1;
    private float boundZ2;
    public final float boardSize = 400;
    private final float boardHeight = 5;
    private float movMagnitude = 0.02f;
    private final float speedCoeff = 1.5f;
    private final float speedLowLimit = 0.01f;
    private final float speedUpperLimit = 0.1f;
    public Data dataPanel;
    public int successfulPasses = 0;

    public float currentRot = 0;
    public int archNbr = 0;
    public int lastArchId = 0;

    private Arch preview;
    
    private ImageProcessing ip;
    
    

    public void setup() {
        size(800, 1000, P3D);
        ball = new MovingBall(this);
        boundX1 = width / 2 - boardSize / 2;
        boundX2 = width / 2 + boardSize / 2;
        boundZ1 = height / 2 - boardSize / 2;
        boundZ2 = height / 2 + boardSize / 2;
        noStroke();
        archs = new ArrayList<>();
        dataPanel = new Data(width, 120, this);
        //statically load arch shape for performance
        Arch.archPreview = loadShape("img_data/arco_texture_buone.obj");
        Arch.archPreview.setFill(color(255,0,0));
        Arch.arch = loadShape("img_data/arco_texture_buone.obj");
        preview = new Arch(0, 0, 0, 0, this);       
        
        ip = new ImageProcessing(this);
        ip.setupBis();
    }
    
    public void draw() {
        background(9, 162, 155);
        lights();
        dataPanel.draw(0, height - 120);
        fill(255);
        textSize(15);
        text("rotationX: " + degrees(rotateX) + " rotationZ: " + degrees(rotateZ) + " rotation speed: " + movMagnitude + "   Passes: " + successfulPasses, 10, 20, 0);
        translate(width / 2, height / 2, 0);      
        
        if (birdView) {
            //view the board from the top to place cylinders
            textSize(25);
            text("SHIFT", boardSize / 2 + 20, boardSize / 2 - 20, 0);
            rotateX(-PI / 2);
            // arch preview
            preview.preview(archCheckBoard() && archCheckBall(), currentRot);	
        } else {

    	PVector temp = ip.drawBis();

    	
    	if(temp.x != 0 && temp.y!= 0 && temp.z != 0){
    		rotateX = temp.x;
    	//	rotateY = temp.y;
        	rotateZ = temp.y;
    	}
   	
    	System.out.print("X = "+rotateX);
    	System.out.print("   Y = "+rotateY);
    	System.out.println("  Z = "+rotateZ);

            //normal view
            rotateX((rotateX));
        //    rotateY((rotateY));
            rotateZ((rotateZ));
            
            ball.checkEdges(boardSize / 2, boardSize / 2);
            ball.update(rotateX, rotateZ);
            ball.checkCylinderCollision();
        }
        fill(131, 191, 17);
        box(boardSize, boardHeight, boardSize);
        ball.display();
        fill(80, 98, 112);
        for (Arch arch : archs) {
            arch.display();
        }
    }

    @Override
    public void mouseClicked() {
        if (birdView) {
            //don't place arch outside of the board
            if (archCheckBoard()) {
                //don't place arch on the ball
                if (archCheckBall()) {
                    archNbr ++;
                    archs.add(new Arch(mouseX - width / 2, mouseY - width / 2, archNbr, currentRot, this));
                }
            }
        }
    }

    public void mouseDragged() {
        //don't rotate if in birdview
        if (birdView) return;

        if(dataPanel.bar.mouseOver || dataPanel.bar.locked) return;

        //rotation
        if (mouseY > prevMouseY) {
            rotateX -= movMagnitude;
        } else if (mouseY < prevMouseY) {
            rotateX += movMagnitude;
        }
        if (mouseX > prevMouseX) {
            rotateZ += movMagnitude;
        } else if (mouseX < prevMouseX) {
            rotateZ -= movMagnitude;
        }
        if (rotateX > PI / 3) {
            rotateX = PI / 3;
        } else if (rotateX < -PI / 3) {
            rotateX = -PI / 3;
        }
        if (rotateZ > PI / 3) {
            rotateZ = PI / 3;
        } else if (rotateZ < -PI / 3) {
            rotateZ = -PI / 3;
        }
        prevMouseY = mouseY;
        prevMouseX = mouseX;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        float w = e.getWheelRotation();
        if (birdView) {
            if (w < 0) {
                currentRot = currentRot + (float)(Math.PI / 16);
            } else if (w > 0) {
                currentRot = currentRot - (float)(Math.PI / 16);
            }
            if (currentRot < 0) {
                currentRot = (float)Math.PI*2;
            } else if (currentRot > Math.PI*2) {
                currentRot = 0;
            }
        } else {
            if (w < 0) {
                movMagnitude = movMagnitude * speedCoeff;
            } else if (w > 0) {
                movMagnitude = movMagnitude / speedCoeff;
            }
            if (movMagnitude < speedLowLimit) {
                movMagnitude = speedLowLimit;
            } else if (movMagnitude > speedUpperLimit) {
                movMagnitude = speedUpperLimit;
            }
        }
    }

    @Override
    public void keyReleased() {
        if (key == CODED) {
            if (keyCode == SHIFT) {
                birdView = false;
            }
        }
    }

    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == LEFT) {
                rotateY -= movMagnitude;
            } else if (keyCode == RIGHT) {
                rotateY += movMagnitude;
            } else if (keyCode == SHIFT) {
                birdView = true;
            }
        }
    }

    public boolean archCheckBoard() {
        return (mouseX - Arch.archWidth >= boundX1  && mouseX + Arch.archWidth <= boundX2 && mouseY - Arch.columnHeight/2 >= boundZ1  && mouseY + Arch.columnHeight/2 <= boundZ2);
    }

    public boolean archCheckBall() {
        PVector mouse = new PVector(mouseX - width / 2, -22.5f, mouseY - width / 2);

        PVector location1 = new PVector(mouse.x + Arch.columnBoard, -22.5f, mouse.z + Arch.columnHeight);
        PVector location2 = new PVector(mouse.x - Arch.columnBoard, -22.5f, mouse.z - Arch.columnHeight);

        boolean un = location1.dist(ball.location) > Arch.archWidth / 2;
        boolean deux = location2.dist(ball.location) > Arch.archWidth / 2;

        return (un && deux);
    }

    static public void main(String[] args) {
        String[] appletArgs = new String[]{"game.Game"};
        PApplet.main(appletArgs);
    }



}