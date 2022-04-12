

PImage blmap;

void setup() {
  size(1200, 700);
  textSize(26);
  
  blmap = loadImage("BanjaLukaMap.png");
  blmap.resize(1200, 700);
}

public class Point {
  private float x;
  private float y;
  
  public Point(float x, float y) {
    this.x = x;
    this.y = y;
  }
  
  void setX(float x) {
    this.x = x;
  }
  
  void setY(float y) {
    this.y = y;
  }
  
  float getX() {
    return x;
  }
  
  float getY() {
    return y;
  }
  
  float getDistanceTo(Point p) {
    return sqrt( (x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
  }
  
  String toString() {
   return "X: " + x + "  Y: " + y; 
  }
}

Point a = new Point(random(50, 1000), random(50, 700));
Point b = new Point(random(50, 1000), random(50, 700));
Point c = new Point(random(50, 1000), random(50, 700));

void draw() {
  //background(76);
  background(blmap);
  
  Point mouseP = new Point(mouseX, mouseY);
  
  float da = a.getDistanceTo(mouseP);//sqrt((mouseX - a_x) * (mouseX - a_x) + (mouseY - a_y) * (mouseY - a_y));
  float db = b.getDistanceTo(mouseP);//sqrt((mouseX - b_x) * (mouseX - b_x) + (mouseY - b_y) * (mouseY - b_y));
  float dc = c.getDistanceTo(mouseP);//sqrt((mouseX - c_x) * (mouseX - c_x) + (mouseY - c_y) * (mouseY - c_y));
  
  Point a_of = new Point(0, 0);
  Point b_of = new Point(b.getX() - a.getX(), b.getY() - a.getY());
  Point c_of = new Point(c.getX() - a.getX(), c.getY() - a.getY());
  
  println("a: " + a_of);
  println("b: " + b_of);
  println("c: " + c_of);
  
  float angle = atan2(b_of.getY(), b_of.getX());
  
  println("Ugao " + angle * 180 / 3.1415);
  
  Point b_rot = new Point(b_of.getX() * cos(angle) + b_of.getY() * sin(angle), - b_of.getX() * sin(angle) + b_of.getY() * cos(angle));
  Point c_rot = new Point(c_of.getX() * cos(angle) + c_of.getY() * sin(angle), - c_of.getX() * sin(angle) + c_of.getY() * cos(angle));
  
  println("Nakon rotacije a " + a_of);
  println("Nakon rotacije b " + b_rot);
  println("Nakon rotacije c " + c_rot);
  
  float mouse_X = (da * da - db * db + b_rot.getX() * b_rot.getX()) / (2 * b_rot.getX());
  float mouse_Y = (da * da - dc * dc + c_rot.getX() * c_rot.getX() + c_rot.getY() * c_rot.getY() - 2 * c_rot.getX() * mouse_X) / (2 * c_rot.getY());
  
  println("mouse X prije antirotacije " + mouse_X);
  println("mouse y prije antirotacije " + mouse_Y);
  
  float mouse_X_rot_back = mouse_X * cos(-angle) + mouse_Y * sin(-angle);
  float mouse_Y_rot_back = - mouse_X * sin(-angle) + mouse_Y * cos(-angle);
  
  Point calc_mouse = new Point(mouse_X_rot_back + a.getX(), mouse_Y_rot_back + a.getY());
  
  //translate(100, 100);
  
  line(a.getX(), a.getY(), mouseX, mouseY);
  line(b.getX(), b.getY(), mouseX, mouseY);
  line(c.getX(), c.getY(), mouseX, mouseY);
  
  text(da, a.getX() + 10, a.getY() + 10);
  text(db, b.getX() + 10, b.getY() + 10);
  text(dc, c.getX() + 10, c.getY() + 10);
  
  text(mouseX + " " + mouseY, mouseX + 10, mouseY + 10);
  
  fill(255, 0, 0);
  text(calc_mouse.getX() + " " + calc_mouse.getY(), mouseX + 10, mouseY - 10);
  
  fill(255, 0, 0);
  ellipse(a.getX(), a.getY(), 10, 10);
  ellipse(b.getX(), b.getY(), 10, 10);
  ellipse(c.getX(), c.getY(), 10, 10);
  
  if (mousePressed) {
    fill(0);
  } else {
    fill(255);
  }
  ellipse(mouseX, mouseY, 10, 10);
  
  
}
