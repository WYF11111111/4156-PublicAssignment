package unit;

import static org.junit.jupiter.api.Assertions.*;

import models.GameBoard;
import models.Player;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class)
public class GameboardTest {

	GameBoard gb = new GameBoard();
	Player p = new Player();
	
	@Test
	@Order(1)
	public void testcheckGBoutofrange() {
		int x = 6;
		int y = 0;
		
		int result = gb.checkGB(x,y);
		assertEquals(-1, result);
		
		x = 0;
		y = 6;
		result = gb.checkGB(x,y);
		assertEquals(-1, result);
	}
	
	@Test
	@Order(2)
	public void testcheckGBcorrectcase() {
		int x = 2;
		int y = 1;
		char type = '\u0000';
		
		gb.setBoardState(x, y, type);
		int result = gb.checkGB(x,y);
		assertEquals(1, result);
	}
	
	@Test
	@Order(3)
	public void testcheckGBoccupiedcase() {
		int x = 0;
		int y = 1;
		char type = 'X';

		gb.setBoardState(x, y, type);
		int result = gb.checkGB(x,y);
		assertEquals(0, result);
		
		type = 'O';

		gb.setBoardState(x, y, type);
		result = gb.checkGB(x,y);
		assertEquals(0, result);
	}

	@Test
	@Order(4)
	public void testcheckwinrow1() {
		char[][] rowspace = {{'X','X','X'}, {'O','X','X'}, {'X','O','X'}, {'X','X','O'}, {'O','O','X'}, {'O','X','O'}, {'X','O','O'}, {'O','O','O'}};
		int count = 0;
		int player = 1;
		char type = 'X';
		gb = new GameBoard();
		p.setId(player);
		p.setType(type);
		gb.setP1(p);
		char[][][] gamespace = new char[512][3][3];
		boolean[] gameresult = new boolean[512];
		
		for (int i=0; i<rowspace.length;i++) {
			for (int j=0; j<rowspace.length;j++) {
				for (int k=0; k<rowspace.length;k++) {
					gamespace[count][0] = rowspace[i];
					gamespace[count][1] = rowspace[j];
					gamespace[count][2] = rowspace[k];
					count += 1;
				}
			}
		}
		
		for (int i=0; i<gamespace.length; i++) {
			for (int j=0; j<gamespace[i].length; j++) {
				for (int k=0; k<gamespace[i][j].length; k++) {
					gb.setBoardState(j, k, gamespace[i][j][k]);
				}
			}
			gameresult[i] = gb.checkwin(player);
		}
		
		for (int i=0; i<gamespace.length; i++) {
			for (int j=0; j<gamespace[i].length; j++) {
				for (int k=0; k<gamespace[i][j].length; k++) {
					gb.setBoardState(j, k, gamespace[i][j][k]);
				}
			}
			boolean result = gb.checkwin(player);
			assertEquals(gameresult[i], result);
		}
	}
	
	@Test
	@Order(5)
	public void testcheckwincol1() {
		int player = 1;
		char type = 'O';
		
		gb = new GameBoard();
		p.setId(player);
		p.setType(type);
		gb.setP1(p);
		
		for (int i=0; i<gb.getBoardState().length; i++) {
			gb.setBoardState(i, 0, type);
		}
		boolean result = gb.checkwin(player);
		assertEquals(true, result);
		for (int i=0; i<gb.getBoardState().length; i++) {
			gb.setBoardState(i, 0, 'X');
			gb.setBoardState(i, 1, type);
		}
		result = gb.checkwin(player);
		assertEquals(true, result);
		for (int i=0; i<gb.getBoardState().length; i++) {
			gb.setBoardState(i, 1, 'X');
			gb.setBoardState(i, 2, type);
		}
		result = gb.checkwin(player);
		assertEquals(true, result);
	}

	@Test
	@Order(6)
	public void testcheckwindiag1() {
		int player = 1;
		char type = 'O';
		
		gb = new GameBoard();
		p.setId(player);
		p.setType(type);
		gb.setP1(p);
		
		gb.setBoardState(0, 0, type);
		gb.setBoardState(1, 1, type);
		gb.setBoardState(2, 2, type);
		
		boolean result = gb.checkwin(player);
		assertEquals(true, result);
		
		gb.setBoardState(0, 0, 'X');
		gb.setBoardState(1, 1, 'X');
		gb.setBoardState(2, 2, 'X');
		gb.setBoardState(0, 2, type);
		gb.setBoardState(1, 1, type);
		gb.setBoardState(2, 0, type);
		
		result = gb.checkwin(player);
		assertEquals(true, result);
	}
	
	@Test
	@Order(7)
	public void testcheckwinrow2() {
		int player = 2;
		char type = 'O';
		
		gb = new GameBoard();
		p.setId(player);
		p.setType(type);
		gb.setP2(p);
		
		for (int i=0; i<gb.getBoardState()[0].length; i++) {
			gb.setBoardState(0, i, type);
		}
		boolean result = gb.checkwin(player);
		assertEquals(true, result);
		for (int i=0; i<gb.getBoardState()[0].length; i++) {
			gb.setBoardState(0, i, 'X');
			gb.setBoardState(1, i, type);
		}
		result = gb.checkwin(player);
		assertEquals(true, result);
		for (int i=0; i<gb.getBoardState()[0].length; i++) {
			gb.setBoardState(1, i, 'X');
			gb.setBoardState(2, i, type);
		}
		result = gb.checkwin(player);
		assertEquals(true, result);
		
		gb.setBoardState(0, 0, 'O');
		gb.setBoardState(0, 1, 'X');
		gb.setBoardState(0, 2, 'O');
		gb.setBoardState(1, 0, 'O');
		gb.setBoardState(1, 1, 'O');
		gb.setBoardState(1, 2, 'X');
		gb.setBoardState(2, 0, 'X');
		gb.setBoardState(2, 1, 'O');
		gb.setBoardState(2, 2, 'X');
		
		result = gb.checkwin(player);
		assertEquals(false, result);
	}
	
	@Test
	@Order(8)
	public void testcheckwincol2() {
		int player = 2;
		char type = 'O';
		
		gb = new GameBoard();
		p.setId(player);
		p.setType(type);
		gb.setP2(p);
		
		for (int i=0; i<gb.getBoardState().length; i++) {
			gb.setBoardState(i, 0, type);
		}
		boolean result = gb.checkwin(player);
		assertEquals(true, result);
		for (int i=0; i<gb.getBoardState().length; i++) {
			gb.setBoardState(i, 0, 'X');
			gb.setBoardState(i, 1, type);
		}
		result = gb.checkwin(player);
		assertEquals(true, result);
		for (int i=0; i<gb.getBoardState().length; i++) {
			gb.setBoardState(i, 1, 'X');
			gb.setBoardState(i, 2, type);
		}
		result = gb.checkwin(player);
		assertEquals(true, result);
	}

	@Test
	@Order(9)
	public void testcheckwindiag2() {
		int player = 2;
		char type = 'O';
		
		gb = new GameBoard();
		p.setId(player);
		p.setType(type);
		gb.setP2(p);
		
		gb.setBoardState(0, 0, type);
		gb.setBoardState(1, 1, type);
		gb.setBoardState(2, 2, type);
		
		boolean result = gb.checkwin(player);
		assertEquals(true, result);
		
		gb.setBoardState(0, 0, 'X');
		gb.setBoardState(0, 1, 'O');
		gb.setBoardState(1, 1, 'X');
		gb.setBoardState(2, 2, 'X');
		gb.setBoardState(0, 2, type);
		gb.setBoardState(1, 1, type);
		gb.setBoardState(2, 0, type);
		
		result = gb.checkwin(player);
		assertEquals(true, result);
	}
	
	@Test
	@Order(10)
	public void testcheckwinfail() {
		int player1 = 1;
		char p1type = 'X';
		int player2 = 2;
		char p2type = 'O';
		
		gb = new GameBoard();
		p.setId(player1);
		p.setType(p1type);
		gb.setP1(p);
		p.setId(player2);
		p.setType(p2type);
		gb.setP2(p);
		
		gb.setBoardState(0, 0, 'O');
		gb.setBoardState(0, 1, 'X');
		gb.setBoardState(0, 2, 'O');
		gb.setBoardState(1, 0, 'O');
		gb.setBoardState(1, 1, 'X');
		gb.setBoardState(1, 2, 'X');
		gb.setBoardState(2, 0, 'X');
		gb.setBoardState(2, 1, 'O');
		gb.setBoardState(2, 2, 'O');
		
		boolean result = gb.checkwin(player2);
		assertEquals(false, result);
		result = gb.checkwin(player1);
		assertEquals(false, result);
		
		gb.setBoardState(0, 0, 'O');
		gb.setBoardState(0, 1, 'X');
		gb.setBoardState(0, 2, 'O');
		gb.setBoardState(1, 0, 'X');
		gb.setBoardState(1, 1, 'O');
		gb.setBoardState(1, 2, 'O');
		gb.setBoardState(2, 0, 'X');
		gb.setBoardState(2, 1, 'O');
		gb.setBoardState(2, 2, 'X');
		
		result = gb.checkwin(player1);
		assertEquals(false, result);
		result = gb.checkwin(player2);
		assertEquals(false, result);
		result = gb.checkwin(3);
		//System.out.print(result);
		assertEquals(false,result);

	}
	
	@Test
	@Order(11)
	public void testcheckdrawstart() {
		int player1 = 1;
		char p1type = 'O';
		int player2 = 2;
		char p2type = 'X';
		
		gb = new GameBoard();
		p.setId(player1);
		p.setType(p1type);
		gb.setP1(p);
		p.setId(player2);
		p.setType(p2type);
		gb.setP2(p);
		
		//check status when before the game started
		boolean result = gb.checkdraw();
		assertEquals(false, result);
		
	}
	
	@Test
	@Order(12)
	public void testcheckdrawdraw() {
		int player1 = 1;
		char p1type = 'X';
		int player2 = 2;
		char p2type = 'O';
		
		gb = new GameBoard();
		p.setId(player1);
		p.setType(p1type);
		gb.setP1(p);
		p.setId(player2);
		p.setType(p2type);
		gb.setP2(p);
		
		//check status that is really a draw
		gb.setBoardState(0, 0, 'X');
		gb.setBoardState(0, 1, 'X');
		gb.setBoardState(0, 2, 'O');
		gb.setBoardState(1, 0, 'O');
		gb.setBoardState(1, 1, 'X');
		gb.setBoardState(1, 2, 'X');
		gb.setBoardState(2, 0, 'X');
		gb.setBoardState(2, 1, 'O');
		gb.setBoardState(2, 2, 'O');
		
		boolean result = gb.checkdraw();
		assertEquals(true, result);
	}

	@Test
	@Order(13)
	public void testcheckdrawwin() {
		int player1 = 1;
		char p1type = 'X';
		int player2 = 2;
		char p2type = 'O';
		
		gb = new GameBoard();
		p.setId(player1);
		p.setType(p1type);
		gb.setP1(p);
		p.setId(player2);
		p.setType(p2type);
		gb.setP2(p);
		
		//check whether the status is really a draw
		gb.setBoardState(0, 0, 'X');
		gb.setBoardState(0, 1, 'O');
		gb.setBoardState(0, 2, 'O');
		gb.setBoardState(1, 0, 'O');
		gb.setBoardState(1, 1, 'X');
		gb.setBoardState(1, 2, 'X');
		gb.setBoardState(2, 0, 'X');
		gb.setBoardState(2, 1, 'O');
		gb.setBoardState(2, 2, 'X');
		
		boolean result = gb.checkdraw();
		assertEquals(false, result);
		
		//check whether the status is really a draw
		gb.setBoardState(0, 0, 'O');
		gb.setBoardState(0, 1, 'X');
		gb.setBoardState(0, 2, 'X');
		gb.setBoardState(1, 0, 'X');
		gb.setBoardState(1, 1, 'O');
		gb.setBoardState(1, 2, 'O');
		gb.setBoardState(2, 0, 'O');
		gb.setBoardState(2, 1, 'X');
		gb.setBoardState(2, 2, 'O');
		
		result = gb.checkdraw();
		assertEquals(false, result);
	}
}
