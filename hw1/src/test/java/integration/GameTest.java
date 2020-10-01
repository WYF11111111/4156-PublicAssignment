package integration;
 

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import controllers.PlayGame;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.GameBoard;
import models.Player;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.google.gson.Gson;

@TestMethodOrder(OrderAnnotation.class) 
public class GameTest {
	
    /**
    * Runs only once before the testing starts.
    */
    @BeforeAll
	public static void init() {
		// Start Server
    	PlayGame.main(null);
    	System.out.println("Before All");
    }
	
    /**
    * This method starts a new game before every test run. It will run every time before a test.
    */
    @BeforeEach
    public void startNewGame() {
    	// Test if server is running. You need to have an end point /
        // If you do not wish to have this end point, it is okay to not have anything in this method.
    	HttpResponse response = Unirest.get("http://localhost:8080/").asString();
        int restStatus = response.getStatus();
        
    	System.out.println("Before Each");
    }
	
    /**
    * This is a test case to evaluate the newgame endpoint.
    */
    @Test
    @Order(1)
    public void newGameTest() {
    	
    	// Create HTTP request and get response
        HttpResponse response = Unirest.get("http://localhost:8080/newgame").asString();
        int restStatus = response.getStatus();
        
        // Check assert statement (New Game has started)
        assertEquals(restStatus, 200);
        System.out.println("Test New Game");
    }
    
    /**
    * This is a test case of type = X to evaluate the startgame endpoint.
    */
    @Test
    @Order(2)
    public void startGameTestX() {
    	
    	// Create a POST request to startgame endpoint and get the body
        // Remember to use asString() only once for an endpoint call. Every time you call asString(), a new request will be sent to the endpoint. Call it once and then use the data in the object.
        HttpResponse response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
        String responseBody = response.getBody().toString();
        
        // --------------------------- JSONObject Parsing ----------------------------------
        
        System.out.println("Start Game Response: " + responseBody);
        
        // Parse the response to JSON object
        JSONObject jsonObject = new JSONObject(responseBody);

        // Check if game started after player 1 joins: Game should not start at this point
        assertEquals(false, jsonObject.get("gameStarted"));
        
        // ---------------------------- GSON Parsing -------------------------
        
        // GSON use to parse data to object
        Gson gson = new Gson();
        GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
        Player player1 = gameBoard.getp1();
        Player player2 = gameBoard.getp2();
        
        // Check if player type is correct
        assertEquals('X', player1.getType());
        assertEquals('O', player2.getType());
        
        System.out.println("Test Start Game");
    }
    
    /**
    * This is a test case of type = O to evaluate the startgame endpoint.
    */
    @Test
    @Order(3)
    public void startGameTestO() {
    	
    	// Create a POST request to startgame endpoint and get the body
        // Remember to use asString() only once for an endpoint call. Every time you call asString(), a new request will be sent to the endpoint. Call it once and then use the data in the object.
        HttpResponse response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
        String responseBody = response.getBody().toString();
        
        // --------------------------- JSONObject Parsing ----------------------------------
        
        System.out.println("Start Game Response: " + responseBody);
        
        // Parse the response to JSON object
        JSONObject jsonObject = new JSONObject(responseBody);

        // Check if game started after player 1 joins: Game should not start at this point
        assertEquals(false, jsonObject.get("gameStarted"));
        
        // ---------------------------- GSON Parsing -------------------------
        
        // GSON use to parse data to object
        Gson gson = new Gson();
        GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
        Player player1 = gameBoard.getp1();
        Player player2 = gameBoard.getp2();
        
        // Check if player type is correct
        assertEquals('O', player1.getType());
        assertEquals('X', player2.getType());
        
        System.out.println("Test Start Game");
    }
    
    /**
    * This is a test case of joingame endpoint.
    */
    @Test
    @Order(4)
    public void joingameTest() {
    	
    	//First assign the value for player 1
    	Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    	
    	// Create a GET request to joingame endpoint and get the body
        // Remember to use asString() only once for an endpoint call. Every time you call asString(), a new request will be sent to the endpoint. Call it once and then use the data in the object.
        HttpResponse response = Unirest.get("http://localhost:8080/joingame").asString();
        String responseBody = response.getBody().toString();
        
        HttpResponse response1 = Unirest.get("http://localhost:8080/getgameboard").asString();
        String result = response1.getBody().toString();
        
        // Parse the response to JSON object
        JSONObject jsonObject = new JSONObject(result);

        // Check if game started after player 1 joins: Game should not start at this point
        assertEquals(true, jsonObject.get("gameStarted"));

        //System.out.print("YIFEILOLOLOL" + result);
        //System.out.print(jsonObject.get("p1").toString());
        assertEquals("{\"type\":\"O\",\"id\":1}", jsonObject.get("p1").toString());
        assertEquals("{\"type\":\"X\",\"id\":2}", jsonObject.get("p2").toString());
        
        System.out.println("Test Join Game");
    }
    
    /**
    * This is a test case of joingame endpoint.
    */
    @Test
    @Order(5)
    public void joingameTestNaN() {
    	
    	//join game before start game
    	Unirest.post("http://localhost:8080/setgameboard").asString();

    	// Create a GET request to joingame endpoint and get the body
        // Remember to use asString() only once for an endpoint call. Every time you call asString(), a new request will be sent to the endpoint. Call it once and then use the data in the object.
        HttpResponse response = Unirest.get("http://localhost:8080/joingame").asString();
        String responseBody = response.getBody().toString();
        
        HttpResponse response1 = Unirest.get("http://localhost:8080/getgameboard").asString();
        String result = response1.getBody().toString();
        
        // Parse the response to JSON object
        JSONObject jsonObject = new JSONObject(result);
        
        // Check if game started after player 1 joins: Game should not start at this point
        assertEquals(false, jsonObject.get("gameStarted"));

        
        //System.out.print(jsonObject.get("p1").toString());
        assertEquals("{\"type\":\" \",\"id\":0}", jsonObject.get("p1").toString());
        assertEquals("{\"type\":\" \",\"id\":0}", jsonObject.get("p2").toString());
        
        System.out.println("Test Join Game");
    }
    
    /**
    * This is a test case of player1 move endpoint.
    */
    @Test
    @Order(6)
    public void move1Test() {
    	
    	//reset game board
    	Unirest.post("http://localhost:8080/setgameboard").asString();
    	
    	//player1 enters the game
    	Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    	
    	/*
    	HttpResponse responseq = Unirest.get("http://localhost:8080/getgameboard").asString();
        String result = responseq.getBody().toString();
        System.out.print("YIFEIfffffffff" + result);
    	*/
    	
    	//player1 tries to make a move
    	HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    	String responseBody = response.getBody().toString();
    	
    	JSONObject jsonObject = new JSONObject(responseBody);
    	
    	//check whether has detected the invalid move
    	assertEquals(false, jsonObject.get("moveValidity"));
    	assertEquals(800, jsonObject.get("code"));
    	assertEquals("Please wait player2 to join!", jsonObject.get("message"));
    	
    	//player2 joins the game
    	Unirest.get("http://localhost:8080/joingame").asString();
    	
    	//player1 tries to make a move
    	HttpResponse response1 = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    	String responseBody1 = response1.getBody().toString();
    	JSONObject jsonObject1 = new JSONObject(responseBody1);

    	//check whether has detected the valid move
    	assertEquals(true, jsonObject1.get("moveValidity"));
    	assertEquals(200, jsonObject1.get("code"));
    	assertEquals("", jsonObject1.get("message"));
    	
    	//player1 tries to make multiple moves
    	HttpResponse response2 = Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    	String responseBody2 = response2.getBody().toString();
    	JSONObject jsonObject2 = new JSONObject(responseBody2);
    	
    	//System.out.print("YIFEIkkkkkkkkkkkk" + responseBody2);
    	
    	//check whether has detected the invalid move
    	assertEquals(false, jsonObject2.get("moveValidity"));
    	assertEquals(100, jsonObject2.get("code"));
    	assertEquals("This is player2\'s Turn!", jsonObject2.get("message"));
    	
    	//player2 makes an invalid move
    	HttpResponse response3 = Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    	String responseBody3 = response3.getBody().toString();
    	
    	JSONObject jsonObject3 = new JSONObject(responseBody3);
    	
    	//check whether has detected the invalid move
    	assertEquals(false, jsonObject3.get("moveValidity"));
    	assertEquals(600, jsonObject3.get("code"));
    	assertEquals("The place is already occupied!", jsonObject3.get("message"));
    	
    	//player2 makes a move
    	HttpResponse response4 = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    	String responseBody4 = response4.getBody().toString();
    	
    	JSONObject jsonObject4 = new JSONObject(responseBody4);
    	
    	//check whether has detected the valid move
    	assertEquals(true, jsonObject4.get("moveValidity"));
    	assertEquals(200, jsonObject4.get("code"));
    	assertEquals("", jsonObject4.get("message"));
    	
    	//player2 makes another invalid move
    	HttpResponse response5 = Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    	String responseBody5 = response5.getBody().toString();
    	
    	JSONObject jsonObject5 = new JSONObject(responseBody5);
    	
    	//check whether has detected the invalid move
    	assertEquals(false, jsonObject5.get("moveValidity"));
    	assertEquals(100, jsonObject5.get("code"));
    	assertEquals("This is player1\'s Turn!", jsonObject5.get("message"));
    	
    	//player1 makes a invalid move
    	HttpResponse response6 = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    	String responseBody6 = response6.getBody().toString();
    	
    	JSONObject jsonObject6 = new JSONObject(responseBody6);
    	
    	//check whether has detected the invalid move
    	assertEquals(false, jsonObject6.get("moveValidity"));
    	assertEquals(600, jsonObject6.get("code"));
    	assertEquals("The place is already occupied!", jsonObject6.get("message"));
    	
    	//player1 makes a out of range move
    	HttpResponse response7 = Unirest.post("http://localhost:8080/move/1").body("x=5&y=5").asString();
    	String responseBody7 = response7.getBody().toString();
    	
    	JSONObject jsonObject7 = new JSONObject(responseBody7);
    	
    	//check whether has detected the invalid move
    	assertEquals(false, jsonObject7.get("moveValidity"));
    	assertEquals(700, jsonObject7.get("code"));
    	assertEquals("Out of Range!", jsonObject7.get("message"));
    	
    	//player1 makes a valid move
    	Unirest.post("http://localhost:8080/move/1").body("x=2&y=1").asString();
    	
    	//player2 makes a out of range move
    	HttpResponse response8 = Unirest.post("http://localhost:8080/move/2").body("x=5&y=5").asString();
    	String responseBody8 = response8.getBody().toString();
    	
    	JSONObject jsonObject8 = new JSONObject(responseBody8);
    	
    	//check whether has detected the invalid move
    	assertEquals(false, jsonObject8.get("moveValidity"));
    	assertEquals(700, jsonObject8.get("code"));
    	assertEquals("Out of Range!", jsonObject8.get("message"));
    	
    	//player2 makes a valid move
    	Unirest.post("http://localhost:8080/move/2").body("x=0&y=1").asString();
    	
    	//player1 makes a valid move
    	Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    	
    	//player2 makes a valid move
    	Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    	
    	//player1 makes a valid move
    	Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    	
    	//player2 makes a valid move
    	Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    	
    	//player1 makes a valid move
    	HttpResponse response9 = Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    	String responseBody9 = response9.getBody().toString();
    	
    	JSONObject jsonObject9 = new JSONObject(responseBody9);
    	
    	//check whether there is a draw
    	assertEquals(true, jsonObject9.get("moveValidity"));
    	assertEquals(300, jsonObject9.get("code"));
    	assertEquals("Game Draw!", jsonObject9.get("message"));
    	
    	//player2 keeps playing
    	HttpResponse response10 = Unirest.post("http://localhost:8080/move/2").body("x=2&y=2").asString();
    	String responseBody10 = response10.getBody().toString();
    	
    	JSONObject jsonObject10 = new JSONObject(responseBody10);
    	
    	//check whether there is a draw
    	assertEquals(false, jsonObject10.get("moveValidity"));
    	assertEquals(500, jsonObject10.get("code"));
    	assertEquals("The game is already end!", jsonObject10.get("message"));
    	
    	//add one turn to the game board -- turn = 1
    	Unirest.post("http://localhost:8080/addturn").asString();
    	
    	//player1 keeps playing
    	HttpResponse response11 = Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    	String responseBody11 = response11.getBody().toString();
    	
    	JSONObject jsonObject11 = new JSONObject(responseBody11);
    	
    	//check whether there is a draw
    	assertEquals(false, jsonObject11.get("moveValidity"));
    	assertEquals(500, jsonObject11.get("code"));
    	assertEquals("The game is already end!", jsonObject11.get("message"));
    	
    	//add one turn to the game board -- turn = 2
    	Unirest.post("http://localhost:8080/addturn").asString();
    	
    	//change is draw state -- isdraw() = false
    	Unirest.post("http://localhost:8080/changeisdraw").asString();
    	
    	//swap the type for player 1 and 2 -- 1=X 2=O
    	Unirest.post("http://localhost:8080/swaptype").asString();
    	
    	//remove a 'O' from game board
    	Unirest.post("http://localhost:8080/removeO").asString();
    	
    	//player2 makes a valid move
    	HttpResponse response12 = Unirest.post("http://localhost:8080/move/2").body("x=0&y=2").asString();
    	String responseBody12 = response12.getBody().toString();
    	
    	JSONObject jsonObject12 = new JSONObject(responseBody12);
    	
    	//check whether there is a draw
    	assertEquals(true, jsonObject12.get("moveValidity"));
    	assertEquals(300, jsonObject12.get("code"));
    	assertEquals("Game Draw!", jsonObject12.get("message"));
    	
    	//flip the draw status -- isdraw = false
    	Unirest.post("http://localhost:8080/changeisdraw").asString();
    	
    	//remove a 'O' from game board
    	Unirest.post("http://localhost:8080/removeO").asString();
    	
    	//player1 makes a valid move
    	HttpResponse response13 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    	String responseBody13 = response13.getBody().toString();
    	
    	JSONObject jsonObject13 = new JSONObject(responseBody13);
    	
    	//check whether there is a win
    	assertEquals(true, jsonObject13.get("moveValidity"));
    	assertEquals(400, jsonObject13.get("code"));
    	assertEquals("Player1 wins!", jsonObject13.get("message"));
    	
    	//swap the type for player 1 and 2 -- 1=O 2=X
    	Unirest.post("http://localhost:8080/swaptype").asString();
    	
    	//remove a 'O' from game board
    	Unirest.post("http://localhost:8080/removeO").asString();
    	
    	//flip the win status -- getwinner = 0
    	Unirest.post("http://localhost:8080/win0").asString();
    	
    	//player2 makes a valid move
    	HttpResponse response14 = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    	String responseBody14 = response14.getBody().toString();
    	
    	JSONObject jsonObject14 = new JSONObject(responseBody14);
    	
    	//check whether there is a win
    	assertEquals(true, jsonObject14.get("moveValidity"));
    	assertEquals(400, jsonObject14.get("code"));
    	assertEquals("Player2 wins!", jsonObject14.get("message"));
    	
    	//player1 makes an invalid move
    	Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    	
    	//add one turn to the game board -- turn = 2
    	Unirest.post("http://localhost:8080/addturn").asString();
    	
    	//player2 makes an invalid move
    	Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    	
    	//flip the draw status -- isdraw = false
    	Unirest.post("http://localhost:8080/changeisdraw").asString();
    	
        System.out.println("Test moves");
    }
    
    /**
    * This will run every time after a test has finished.
    */
    @AfterEach
    public void finishGame() {
    	System.out.println("After Each");
    }
    
    /**
     * This method runs only once after all the test cases have been executed.
     */
    @AfterAll
    public static void close() {
	// Stop Server
    	PlayGame.stop();
    	System.out.println("After All");
    }
}