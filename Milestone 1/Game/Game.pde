import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

MovingBall ball;

public ArrayList < Cylinder > cylinders;
float rotateY = 0;
float rotateX = 0;
float rotateZ = 0;
float movMagnitude = 0.02f;
float prevMouseX;
float prevMouseY;

boolean birdView = false;
float boundX1;
float boundX2;
float boundZ1;
float boundZ2;


float boardSize = 400;


public void setup() {
  size(800, 800, P3D);
  ball = new MovingBall(this);
  boundX1 = width / 2 - boardSize / 2;
  boundX2 = width / 2 + boardSize / 2;
  boundZ1 = height / 2 - boardSize / 2;
  boundZ2 = height / 2 + boardSize / 2;
  noStroke();
  noSmooth();
  cylinders = new ArrayList < Cylinder > ();
}

public void draw() {
  fill(255);
  background(35);
  lights();
  fill(255, 255, 255);
  textSize(15);
  text("rotationX: " + degrees(rotateX) + " rotationZ: " + degrees(rotateZ) + " rotation speed: " + movMagnitude + " cylinders: " + cylinders.size(), 10, 20, 0);
  translate(width / 2, height / 2, 0);

  if (birdView) {
    //view the board from the top to place cylinders
    textSize(25);
    text("SHIFT", boardSize / 2 + 20, boardSize / 2 - 20, 0);
    rotateX(-PI / 2);
  } else {
    //normal view
    rotateY(rotateY);
    rotateZ(rotateZ);
    rotateX(rotateX);
    ball.checkEdges(boardSize / 2, boardSize / 2);
    ball.update(rotateX, rotateZ);
    ball.checkCylinderCollision();
  }
  fill(0, 255, 0);
  box(boardSize, 5, boardSize);
  ball.display();

  fill(0, 0, 255);
  for (Cylinder cy: cylinders) {
    cy.display();
  }
}

@Override
public void mouseClicked() {
  if (birdView) {
    //don't place cylinders outside of the board
    if (mouseX >= boundX1 + Cylinder.radius && mouseX <= boundX2 - Cylinder.radius && mouseY >= boundZ1 + Cylinder.radius && mouseY <= boundZ2 - Cylinder.radius) {
      PVector mouse = new PVector(mouseX - 400, -22.5f, mouseY - 400);

      //don't place cylinders on the ball
      if (mouse.dist(ball.location) >= 35) {
        cylinders.add(new Cylinder(mouse.x, mouse.z, this));
      }
    }
  }
}

public void mouseDragged() {
  //don't rotate if in birdview
  if (birdView) return;

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
  if (w < 0) {
    movMagnitude = movMagnitude * 1.5f;
  } else if (w > 0) {
    movMagnitude = movMagnitude / 1.5f;
  }

  if (movMagnitude < 0.01f) {
    movMagnitude = 0.01f;
  } else if (movMagnitude > 0.1f) {
    movMagnitude = 0.1f;
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
