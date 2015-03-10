class Mover {
  
  PVector location;
  PVector velocity;
  final PVector gravity = new PVector(0, 0.1);
  
  Mover() {
    location = new PVector(width/2, height/2);
    velocity = new PVector(1, 1);
  }
  
  void update() {
    velocity.add(gravity);
    location.add(velocity);
  }
  
  void display() {
    stroke(0);
    strokeWeight(2);
    fill(127);
    ellipse(location.x, location.y, 48, 48);
  }
  
  void checkEdges() {
    if (location.x > width) {
      velocity.x = - velocity.x;
    } else if (location.x < 0) {
      velocity.x = - velocity.x;
    }
    
    if (location.y > height) {
      velocity.y = - velocity.y;
    } else if (location.y < 0) {
      velocity.y = - velocity.y;
    }
  }
}
