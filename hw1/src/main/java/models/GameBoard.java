package models;

public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;
  
  //Default Constructor
  public GameBoard(){
	this.p1 = new Player();
	this.p2 = new Player();
	this.gameStarted = false;
	this.setTurn(0);
	this.boardState = new char[][]{ {'\u0000','\u0000','\u0000'},{'\u0000','\u0000','\u0000'},{'\u0000','\u0000','\u0000'} };
	this.setWinner(0);
	this.isDraw = false;
  }

  
  //getter of Board State
  public char[][] getBoardState() {
	  return boardState;
  }
  
  //setter of Board State
  public void setBoardState(int x, int y, char type) {
	this.boardState[x][y] = type;
  }

  //setter of gameStarted
  public void setGameStarted(boolean gameStarted) {
	this.gameStarted = gameStarted;
  }
  
  //setter of player1
  public void setP1(Player p1) {
	this.p1 = p1;
  }
  
  //getter of player1
  public Player getp1() {
    return p1;
  }
  
  //setter of player2
  public void setP2(Player p2) {
	this.p2 = p2;
  }
  
  //getter of player1
  public Player getp2() {
    return p2;
  }

  //setter of turn
  public void setTurn(int turn) {
	this.turn = turn;
  }
  
  //getter of turn
  public int getTurn() {
	  return turn;
  }
  
  //check game board state
  public int checkGB(int x, int y)
  {
	int x_coord = this.boardState.length;
	int y_coord = this.boardState[0].length; 
	
	if (x>=x_coord || y>=y_coord)
	{
		return -1;
	} else
	{
		if (this.boardState[x][y] == 'O' || this.boardState[x][y] == 'X')
		{
			return 0;
		} else
		{
			return 1;
		}
	}
  }
  
  //getter of win status
  public int getWinner() {
	return winner;
  }

  //setter of win status
  public void setWinner(int winner) {
	this.winner = winner;
  }

  public boolean isDraw() {
	return isDraw;
  }

  //setter of Draw
  public void setDraw(boolean isDraw) {
	this.isDraw = isDraw;
  }
  
  //check whether a specific player is win
  public boolean checkwin(int player) {
	  boolean check_gameboard = false;
	  if (player == 1)
	  {
		  char type = getp1().getType();
		  
	      //check whether the row indicates player1's victory
		  for (int i=0; i<getBoardState().length; i++)
		  {
		    if (getBoardState()[i][0] == type)
			{
			  for (int j=1; j<getBoardState()[i].length; j++)
			  {
			    if (getBoardState()[i][j] != type)
				{
				  break;
				} else
				{
				  if (j==getBoardState()[i].length-1)
				  {
				    check_gameboard = true;
					return check_gameboard;
				  }
				}
	  	  	  }
		    }
		   }
			  
		   //check whether the column indicates player1's victory
		   for (int i=0; i<getBoardState()[0].length; i++)
		   {
		     if (getBoardState()[0][i] == type)
			 {
			   for (int j=1; j<getBoardState().length; j++)
			   {
			     if (getBoardState()[j][i] != type)
				 {
				   break;
				 } else
				 {
				   if (j == getBoardState().length-1)
				   {
				     check_gameboard = true;
					 return check_gameboard;
				   }
			     }
		       }
		     }
	       }
			  
		   //check whether the diagonal indicates player1's victory
		   for (int i=0; i<getBoardState()[0].length; i++)
		   {
		     if (getBoardState()[0][i] == type)
			 {
			   if (i == 0)
			   {
				   int k = i+1;
				   while(k < getBoardState().length)
				   {
					   if (getBoardState()[k][k] != type)
					   {
						   break;
					   }
					   k += 1;
				   }
				   if (k == getBoardState().length)
				   {
					   check_gameboard = true;
					   return check_gameboard;
				   }
			   } else if (i == getBoardState()[0].length-1)
			   {
				   int k = i-1;
				   int j = 1;
				   while(k>=0 && j<getBoardState().length)
				   {
					   if (getBoardState()[j][k] != type)
					   {
						   break;
					   }
					   k -= 1;
					   j += 1;
				   }
				   if (k < 0 && j == getBoardState().length)
				   {
					   check_gameboard = true;
					   return check_gameboard;
				   }
			   }
		     }
	       }

	  } else if (player == 2)
	  {
		  char type = getp2().getType();
		  
	      //check whether the row indicates player2's victory
		  for (int i=0; i<getBoardState().length; i++)
		  {
		    if (getBoardState()[i][0] == type)
			{
			  for (int j=1; j<getBoardState()[i].length; j++)
			  {
			    if (getBoardState()[i][j] != type)
				{
				  break;
				} else
				{
				  if (j==getBoardState()[i].length-1)
				  {
				    check_gameboard = true;
					return check_gameboard;
				  }
				}
	  	  	  }
		    }
		   }
			  
		   //check whether the column indicates player2's victory
		   for (int i=0; i<getBoardState()[0].length; i++)
		   {
		     if (getBoardState()[0][i] == type)
			 {
			   for (int j=1; j<getBoardState().length; j++)
			   {
			     if (getBoardState()[j][i] != type)
				 {
				   break;
				 } else
				 {
				   if (j == getBoardState().length-1)
				   {
				     check_gameboard = true;
					 return check_gameboard;
				   }
			     }
		       }
		     }
	       }
			  
		   //check whether the diagonal indicates player2's victory
		   for (int i=0; i<getBoardState()[0].length; i++)
		   {
		     if (getBoardState()[0][i] == type)
			 {
			   if (i == 0)
			   {
				   int k = i+1;
				   while(k < getBoardState().length)
				   {
					   if (getBoardState()[k][k] != type)
					   {
						   break;
					   }
					   k += 1;
				   }
				   if (k == getBoardState().length)
				   {
					   check_gameboard = true;
					   return check_gameboard;
				   }
			   } else if (i == getBoardState()[0].length-1)
			   {
				   int k = i-1;
				   int j = 1;
				   while(k>=0 && j<getBoardState().length)
				   {
					   if (getBoardState()[j][k] != type)
					   {
						   break;
					   }
					   k -= 1;
					   j += 1;
				   }
				   if (k < 0 && j == getBoardState().length)
				   {
					   check_gameboard = true;
					   return check_gameboard;
				   }
			   }
		     }
	       }

	  }
	return check_gameboard;
  }
  
  //check whether the game is draw
  public boolean checkdraw()
  {
	  if (checkwin(1) || checkwin(2))
	  {
		  return false;
	  }else
	  {
		  for (int i=0; i<getBoardState().length; i++)
		  {
			  for (int j=0; j<getBoardState()[i].length; j++)
			  {
				  if (getBoardState()[i][j] == '\u0000')
				  {
					  return false;
				  }
			  }
		  }
	  }
	  return true;
  }
}
