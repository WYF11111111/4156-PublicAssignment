package models;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;

  //getter of the move validity message
  public boolean isMoveValidity() {
	return moveValidity;
  }

  //setter of the move validity message
  public void setMoveValidity(boolean moveValidity) {
	this.moveValidity = moveValidity;
  }

  //getter of the code
  public int getCode() {
	return code;
  }

  //setter of the code
  public void setCode(int code) {
	this.code = code;
  }

  //getter of the message
  public String getMessage() {
	return message;
  }

  //setter of the message
  public void setMessage(String message) {
	this.message = message;
  }

}
