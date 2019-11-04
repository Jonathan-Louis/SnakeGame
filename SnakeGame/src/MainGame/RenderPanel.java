package MainGame;

import java.awt.*;

import javax.swing.JPanel;

public class RenderPanel  extends JPanel{

	protected void paintComponent(Graphics g) {
		
		//intialize render panel
		super.paintComponent(g);
		
		//set background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 760, 510);
		
		Snake snake = Snake.snake;
		
		//catching null pointer exception when first frame rendered and snakeBody = null
		try {
			//draw snake body
			g.setColor(Color.GREEN);
			for(Point point : snake.snakeBody) {
				g.fillRect(point.x * snake.SCALE, point.y * snake.SCALE, snake.SCALE, snake.SCALE);
			}
		
		
			//draw snake head
			g.setColor(Color.ORANGE);
			g.fillRect(snake.head.x * snake.SCALE, snake.head.y * snake.SCALE, snake.SCALE, snake.SCALE);
		
			//draw apple
			g.setColor(Color.RED);
			g.fillRect(snake.apple.x * snake.SCALE, snake.apple.y * snake.SCALE, snake.SCALE, snake.SCALE);
			
			//draw score line
			String scoreLine = "Score: " + snake.score;
			g.setColor(Color.WHITE);
			g.drawString(scoreLine, (int)(getWidth() / 2 - scoreLine.length() * 2.5f), 10);
			
			//draw high score line
			String highScoreLine = "High Score: " + snake.highScoreString;
			g.setColor(Color.WHITE);
			g.drawString(highScoreLine, (int)(getWidth() - highScoreLine.length() * 7.5f), 10);
			
			//draw game over when game ends
			if(snake.paused) {
				String pausedString = "Paused -- press space to continue";
				g.drawString(pausedString, (int) (snake.WIDTH / 2 - pausedString.length() * 2.5f), (int) snake.HEIGHT / 2);
			}
			
			//draw game over when game ends
			if(snake.gameOver) {
				String gameOver = "Game Over!";
				g.drawString(gameOver, (int) getWidth() / 2 - gameOver.length(), (int) getHeight() / 2);
			}
		}
		catch(NullPointerException e) {
			
		}
	}
}
