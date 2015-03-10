//import net.silentlycrashing.gestures.*;
//import net.silentlycrashing.gestures.preset.*;

float rotateY = 0;
float rotateX = 0;
float rotateZ = 0;
float movMagnitude = 0.02;
float prevMouseX;
float prevMouseY;


void setup() {
  size(1000, 1000, OPENGL);
  noStroke();
}

void draw() {
  background(0,0,0);
  lights();
  translate(width/2, height/2, 0);
  rotateX(rotateX);
  rotateY(rotateY);
  //rotateZ(rotateZ);
  box(400, 5, 400);
}

void mouseDragged(){
  if (mouseY > prevMouseY) {
     rotateX -= movMagnitude; 
  } else if (mouseY < prevMouseY) {
    rotateX += movMagnitude;
  } 
  if (mouseX > prevMouseX){
     rotateZ += movMagnitude; 
  } else if (mouseX < prevMouseX){
     rotateZ -= movMagnitude; 
  }

  if (rotateX > PI/3){
     rotateX = PI/3; 
  } else if (rotateX < -PI/3){
     rotateX = -PI/3; 
  }
  
  if (rotateZ > PI/3){
     rotateZ = PI/3; 
  } else if (rotateY < -PI/3){
     rotateZ = -PI/3; 
  }
  prevMouseY = mouseY;
  prevMouseX = mouseX;
}

void mouseWheel(MouseEvent event) {
  float e = event.getCount();
  if (e < 0) {
     movMagnitude = movMagnitude * 1.2; 
  } else if (e > 0) {
     movMagnitude = movMagnitude / 1.2; 
  }
}

void keyPressed() {
  if (key == CODED) {
    if (keyCode == LEFT) {
      rotateY -= movMagnitude;
    }
    else if (keyCode == RIGHT) {
      rotateY += movMagnitude;
    }
  }
}
