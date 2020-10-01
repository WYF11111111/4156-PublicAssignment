package controllers;

import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;

import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;

public class PlayGame {

  private static final int PORT_NUMBER = 8080;

  private static Javalin app;
  
  private static GameBoard gb = new GameBoard();
  
  private static Player p1 = new Player();
  
  private static Player p2 = new Player();
  
  private static Move p1_move = new Move();
  
  private static Move p2_move = new Move();
  
  private static Message report = new Message();

  static Gson gson = new Gson();
  
  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {

    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });

    //start a new game
    app.get("/newgame", ctx -> {
      ctx.redirect("/tictactoe.html");
    });
    
    //getter of the current gameboard
    app.get("/getgameboard", ctx -> {
    	ctx.result(gson.toJson(gb));
    });
    
    //setter of the current gameboard
    app.post("/setgameboard", ctx -> {
    	gb = new GameBoard();
    	ctx.result(gson.toJson(gb));
    	sendGameBoardToAllPlayers(gson.toJson(gb));
    });
    
    //add one turn to the current game board
    app.post("/addturn", ctx -> {
    	if (gb.getTurn() == 1)
    	{
    		gb.setTurn(2);
    	} else
    	{
    		gb.setTurn(1);
    	}
    	ctx.result(gson.toJson(gb));
    	sendGameBoardToAllPlayers(gson.toJson(gb));
    });
    
    //change the is draw state
    app.post("/changeisdraw", ctx -> {
    	if (gb.isDraw() == false)
    	{
    		gb.setDraw(true);
    	} else
    	{
    		gb.setDraw(false);
    	}
    	ctx.result(gson.toJson(gb));
    	sendGameBoardToAllPlayers(gson.toJson(gb));
    });
    
    //change the get winner state
    app.post("/win0", ctx -> {
    	gb.setWinner(0);
    	ctx.result(gson.toJson(gb));
    	sendGameBoardToAllPlayers(gson.toJson(gb));
    });
    
    //swap the player type
    app.post("/swaptype", ctx -> {
    	Player x = new Player();
    	x.setType(gb.getp1().getType());
    	gb.getp1().setType(gb.getp2().getType());
    	gb.getp2().setType(x.getType());
    	ctx.result(gson.toJson(gb));
    	sendGameBoardToAllPlayers(gson.toJson(gb));
    });
    
    //remove a 'O' from game board
    app.post("/removeO", ctx -> {
    	int x = 0;
    	int y = 0;
    	char[][] temp = gb.getBoardState();
    	
    	for (int i=0; i<temp.length; i++)
    	{
    		for (int j=0; j<temp[0].length; j++)
    		{
    			if (temp[i][j] == 'O')
    			{
    					x = i;
    					y = j;
    					i = 10;
    					j = 10;
    			}
    		}
    	}
    	
    	gb.setBoardState(x, y, '\u0000');
    	ctx.result(gson.toJson(gb));
    	sendGameBoardToAllPlayers(gson.toJson(gb));
    });

    //Initialize the game board and setup Player 1
    app.post("/startgame", ctx -> {
      String msg = ctx.body();
      char type = msg.split("=")[1].charAt(0);
      p1.setId(1);
      p1.setType(type);
      gb.setP1(p1);
      p1_move.setPlayer(p1);
      if (type == 'X') {
          p2.setId(2);
          p2.setType('O');
      } else {
        p2.setId(2);
        p2.setType('X');
      }
      gb.setP2(p2);
      p2_move.setPlayer(p2);
      gb.setTurn(1);
      //System.out.println(gson.toJson(gb));
      ctx.result(gson.toJson(gb));
    });
    
    //Update the game board after player2 joins in
    app.get("/joingame", ctx -> {
      if (gb.getp1().getId() == 0)
      {
    	  ctx.redirect("/tictactoe.html");
      } else
      {
    	  ctx.redirect("/tictactoe.html?p=2");
    	  gb.setGameStarted(true);
    	  //System.out.println(gson.toJson(gb));
    	  ctx.result(gson.toJson(gb));
    	  sendGameBoardToAllPlayers(gson.toJson(gb));
    	  
      }
    });
    
    //Update player1's move
    app.post("/move/1", ctx -> {
      
      if (gb.getTurn() == 1 && gb.getWinner() == 0 && gb.isDraw() == false && gb.getGameStarted()) 
      {
    	String msg = ctx.body();
        char[] coord = {msg.split("&")[0].charAt(2), msg.split("&")[1].charAt(2)};
        int is_valid_move = gb.checkGB(Character.getNumericValue(coord[0]), Character.getNumericValue(coord[1]));
    	if (is_valid_move == 1)
    	{
    		gb.setBoardState(Character.getNumericValue(coord[0]), Character.getNumericValue(coord[1]), gb.getp1().getType());
    		gb.setTurn(2);
    		p1_move.setMoveX(Character.getNumericValue(coord[0]));
    		p1_move.setMoveY(Character.getNumericValue(coord[1]));
    		
    		boolean game_result = gb.checkwin(1);
    		if (game_result)
    		{
    			//win message
    			report.setCode(400);
    			report.setMoveValidity(true);
    			report.setMessage("Player1 wins!");
    			gb.setGameStarted(false);
    			gb.setWinner(1);
    			sendGameBoardToAllPlayers(gson.toJson(gb));
    		} else {
    			boolean is_draw = gb.checkdraw();
    			if (is_draw)
    			{	
    				//draw message
    				report.setCode(300);
    				report.setMoveValidity(true);
    				report.setMessage("Game Draw!");
    				gb.setDraw(true);
    				sendGameBoardToAllPlayers(gson.toJson(gb));
    				
    			} else
    			{
    				//regular move message
    				report.setCode(200);
    				report.setMoveValidity(true);
    				report.setMessage("");
    				sendGameBoardToAllPlayers(gson.toJson(gb));
    				
    			}
    		}
    		
    	} else if(is_valid_move == -1)
    	{
    		//error message for out of range
    		report.setCode(700);
        	report.setMoveValidity(false);
    		report.setMessage("Out of Range!");
    		sendGameBoardToAllPlayers(gson.toJson(gb));
    	} else
    	{
    		//error message for already occupied places
    		report.setCode(600);
	    	report.setMoveValidity(false);
			report.setMessage("The place is already occupied!");
			sendGameBoardToAllPlayers(gson.toJson(gb));
    	}
		ctx.result(gson.toJson(report));
      } else {
    	if (gb.getTurn() == 2)
    	{
    		//error message
    		report.setCode(100);
    		report.setMoveValidity(false);
    		report.setMessage("This is player2\'s Turn!");
    	} else if (gb.getGameStarted() == false)
    	{
    		//error message
    		report.setCode(800);
    		report.setMoveValidity(false);
    		report.setMessage("Please wait player2 to join!");
    	} else
    	{
    		//error message
    		report.setCode(500);
    		report.setMoveValidity(false);
    		report.setMessage("The game is already end!");
    	}
    	
    	ctx.result(gson.toJson(report));
      }
      //System.out.println(gson.toJson(gb));
      //System.out.println(gson.toJson(report));
      
    });
    
    //Update player2's move
    app.post("/move/2", ctx -> {
      
      if (gb.getTurn() == 2 && gb.getWinner() == 0 && gb.isDraw() == false) 
      {
    	String msg = ctx.body();
        char[] coord = {msg.split("&")[0].charAt(2), msg.split("&")[1].charAt(2)};
        int is_valid_move = gb.checkGB(Character.getNumericValue(coord[0]), Character.getNumericValue(coord[1]));
        if (is_valid_move == 1)
    	{
    		gb.setBoardState(Character.getNumericValue(coord[0]), Character.getNumericValue(coord[1]), gb.getp2().getType());
    		gb.setTurn(1);
    		p2_move.setMoveX(Character.getNumericValue(coord[0]));
    		p2_move.setMoveY(Character.getNumericValue(coord[1]));
    		
    		boolean game_result = gb.checkwin(2);
    		if (game_result)
    		{
    			//win message
    			report.setCode(400);
    			report.setMoveValidity(true);
    			report.setMessage("Player2 wins!");
    			gb.setGameStarted(false);
    			gb.setWinner(2);
    			sendGameBoardToAllPlayers(gson.toJson(gb));
    		} else {
    			boolean is_draw = gb.checkdraw();
    			if (is_draw)
    			{	
    				//draw message
    				report.setCode(300);
    				report.setMoveValidity(true);
    				report.setMessage("Game Draw!");
    				gb.setDraw(true);
    				sendGameBoardToAllPlayers(gson.toJson(gb));
    			} else
    			{
    				//regular move message
    				report.setCode(200);
    				report.setMoveValidity(true);
    				report.setMessage("");
    				sendGameBoardToAllPlayers(gson.toJson(gb));
    			}
    		}
    		
    	} else if(is_valid_move == -1)
    	{
    		//error message for out of range
    		report.setCode(700);
        	report.setMoveValidity(false);
    		report.setMessage("Out of Range!");
    		sendGameBoardToAllPlayers(gson.toJson(gb));
    	} else
    	{
    		//error message for already occupied places
    		report.setCode(600);
	    	report.setMoveValidity(false);
			report.setMessage("The place is already occupied!");
			sendGameBoardToAllPlayers(gson.toJson(gb));
    	}
        ctx.result(gson.toJson(report));
      } else {
    	  if (gb.getTurn() == 1)
      	  {
      		//error message
      		report.setCode(100);
      		report.setMoveValidity(false);
      		report.setMessage("This is player1\'s Turn!");
      	  } else
      	  {
      		//error message
      		report.setCode(500);
      		report.setMoveValidity(false);
      		report.setMessage("The game is already end!");
      	  }
    	ctx.result(gson.toJson(report));
      }
      //System.out.println(gson.toJson(gb));
      //System.out.println(gson.toJson(report));
      
    });
    
    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
    	  System.out.println("logger report");
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
