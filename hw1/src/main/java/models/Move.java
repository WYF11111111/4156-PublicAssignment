package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;

  //getter of the player of a specific move
  public Player getPlayer() {
	return player;
  }

  //setter of the player of a specific move
  public void setPlayer(Player player) {
	this.player = player;
  }

  //getter of the move in x coordinate
  public int getMoveX() {
	return moveX;
  }

  //setter of the move in x coordinate
  public void setMoveX(int moveX) {
	this.moveX = moveX;
  }

  //getter of the move in y coordinate
  public int getMoveY() {
	return moveY;
  }

  //setter of the move in y coordinate
  public void setMoveY(int moveY) {
	this.moveY = moveY;
  }

}


