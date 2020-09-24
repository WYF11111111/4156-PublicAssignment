package models;

public class Player {

  private char type;

  private int id;

  //Constructor
  public Player(){
    this.type = ' ';
    this.id = 0;
  }
  
  //getter and setter of type and id
  public char getType() {
	return type;
  }

  public void setType(char type) {
	this.type = type;
  }

  public int getId() {
	return id;
  }

  public void setId(int id) {
	this.id = id;
  }
  
}

