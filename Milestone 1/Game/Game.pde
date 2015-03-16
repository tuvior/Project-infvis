Ball ball;
PShape boxe;

float rotationX = 0.0;
float rotationY = 0;
float rotationZ = 0.0;

float speedMovement = 1.0; // The sepped of board movements



void setup(){
   size(800, 800, P3D); 
   noStroke();
   
   boxe = createShape(BOX,400, 20, 400);
   
   ball = new Ball();
}

void draw(){
  background(255);
 
  text("Rotation x :"+degrees(rotationX) + "Rotation y :"+degrees(rotationY) + "   Rotation z :"+ degrees(rotationZ)+"    Speed : "+speedMovement, 10,10);
  
  directionalLight(50, 100, 125, 1, 1, 0);
  ambientLight(102, 102, 102);
  
  translate(width/2, height/2, 0);
  
  rotateY(rotationY);
  rotateX(rotationX);
  rotateZ(rotationZ); 
  
  shape(boxe,0,0);

  ball.update();
  ball.checkEdges();
  ball.display();
  
}

void keyPressed(){
  if (key == CODED) {
     if (keyCode == LEFT){
         rotationY = rotationY - speedMovement*0.1; //check the -0.1 ??
     } else if (keyCode == RIGHT){
         rotationY = rotationY + speedMovement*0.1;
     }
  } 
}



void mouseDragged(){
  
  rotationZ += speedMovement * (mouseX-(pmouseX))/100; 
  
  if ( rotationZ < -PI/3){
     rotationZ = - PI/3;
  } else if(rotationZ > PI/3){
     rotationZ = PI/3; 
  } 

  rotationX += speedMovement * (mouseY-(pmouseY))/100;
  if ( rotationX < -PI/3){
     rotationX = - PI/3;
  } else if(rotationX > PI/3){
     rotationX = PI/3; 
  } 
}

void mouseWheel(MouseEvent event) {
  
    if(event.getCount() < 0) {
        speedMovement += 0.02;
    } else{
        speedMovement -= 0.02;
    }
       
          
    if(speedMovement > 2){
       speedMovement = 2;
     }else if (speedMovement < 0.2) {
       speedMovement = 0.2;
     }
    
}
       
