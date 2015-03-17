Ball ball;
PShape boxe;

ArrayList<Cylinder> cylinders = new ArrayList();
ArrayList<PVector> cylindersPosition = new ArrayList();

float rotationX = 0.0;
float rotationY = 0;
float rotationZ = 0.0;

float speedMovement = 1.0; // The sepped of board movements

boolean shiftKey = false;

void setup(){
   size(800, 800, P3D); 
   noStroke();
   
   boxe = createShape(BOX,400, 20, 400);
   
   ball = new Ball();
}

void draw(){
  background(255);

   
  if(!shiftKey){
     
    directionalLight(50, 100, 125, 1, 1, 0);
    ambientLight(102, 102, 102);
    
    text("Rotation x :"+degrees(rotationX) + "    Rotation y :"+degrees(rotationY) + "   Rotation z :"+ degrees(rotationZ)+"    Speed : "+speedMovement, 10,10);
     
    translate(width/2, height/2, 0);
     
    rotateY(rotationY);
    rotateX(rotationX);
    rotateZ(rotationZ); 
                   
                                                            
    pushMatrix();
    rotateX(PI/2); 
    Cylinder c;
    for(int i = 0; i < cylinders.size(); i++){
      pushMatrix();
      
      c = cylinders.get(i);
      translate(-210, -210, 10);  // 210 = 0.5*(box.width+cylinder.base)  10 = box.height/2
      c.initialize();
      c.display();   
      
      popMatrix();
    }
    rotateX(-PI/2);
    popMatrix();
    
                              
    shape(boxe,0,0);
  
    ball.update();
    ball.checkEdges();
    ball.checkCylinderCollision();
    ball.display();
    
  }
  
  else{ // SHIFT MODE
    text("SHIFT",650,600);
    
    translate((width-400)/2, (height-400)/2, 0);
    
    Cylinder c;
    for(int i = 0; i < cylinders.size(); i++){
       c = cylinders.get(i); 
       c.initialize();
       c.display();
    }

    
    pushMatrix();
    translate(ball.location.x, ball.location.z);
  
    
    PShape circle = createShape(ELLIPSE, 200, 200, 20,20);
    fill(0);
    shape(circle);
    
    popMatrix();
    
    fill(127);
    rect(0,0,400,400);
  }
  
}

void keyPressed(){
  if (key == CODED) {
     if (keyCode == LEFT){
         rotationY = rotationY - speedMovement*0.1; //check the -0.1 ??
     } else if (keyCode == RIGHT){
         rotationY = rotationY + speedMovement*0.1;
     }
     if (keyCode == SHIFT){
        if(!shiftKey){
          shiftKey = true;
        }
     }
  } 
}


void keyReleased(){
 if (key == CODED){
    if (keyCode == SHIFT){
       shiftKey = false;
    }
 } 
}


void mouseClicked(){
  if(shiftKey){
     //ADD Cylinder 
     if (mouseX > 200 && mouseX < 600  &&  mouseY > 200  && mouseY < 600){ 
        float x = mouseX - 200;
        float y = mouseY - 200;
        Cylinder c = new Cylinder(x,y);
        cylinders.add(c);
        cylindersPosition.add(new PVector(x, y));
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

  rotationX -= speedMovement * (mouseY-(pmouseY))/100;
  if ( rotationX < -PI/3){
     rotationX = - PI/3;
  } else if(rotationX > PI/3){
     rotationX = PI/3; 
  } 
}

void mouseWheel(MouseEvent event) {
  
    if(event.getCount() < 0) {
        speedMovement += 0.1;
    } else{
        speedMovement -= 0.1;
    }
       
          
    if(speedMovement > 1.5){
       speedMovement = 1.5;
     }else if (speedMovement < 0.20) {
       speedMovement = 0.20;
     }
    
}


       
