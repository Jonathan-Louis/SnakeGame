package MainGame;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


import javax.swing.JFrame;
import javax.swing.Timer;




public class Snake implements ActionListener, KeyListener{
	
	public static Snake snake;
	
	public static final int WIDTH = 750, HEIGHT = 500;
	
	public static final int RIGHT = 0, LEFT = 1, UP = 2, DOWN = 3, SCALE = 10;
	
	public int direction, score, tail, highScore = 0;
	
	public int time, ticks = 0, delay, initialDelay = 50;
	
	public boolean overlapping;
	
	public String highScoreString;
	
	public JFrame frame;
	
	public Timer timer = new Timer(initialDelay, this);
	
	public RenderPanel renderPanel;
	
	public ArrayList<Point> snakeBody = new ArrayList();
	
	public Point apple, head;
	
	public Random random;
	
	public boolean gameOver = false, paused = false;
	
	public Dimension dimension;
	
	
	public Snake() {
		//get screen info
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		frame = new JFrame("Snake Game");
		frame.setTitle("Snake Game");
		
		//set frame size to show all game
		frame.setSize(WIDTH + 26, HEIGHT + 49);
		frame.setVisible(true);
		frame.setResizable(false);
		
		//center frame on screen
		frame.setLocation(dimension.width / 2 - WIDTH / 2, dimension.height / 2 - HEIGHT / 2);
		
		//add render panel to draw images to frame
		frame.add(renderPanel = new RenderPanel());
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		//add key listener for controls
		frame.addKeyListener((KeyListener) this);
		
		//start the game
		startGame();
	}
	
	//initialize the game
	public void startGame() {
		score = 0;
		tail = 1;
		time = 0;
		head = new Point(10, 10);
		random = new Random();
		snakeBody.clear();
		direction = DOWN;
		timer.setDelay(initialDelay);
		delay = initialDelay;
		ticks = 0;
		
		//get high score string
		getHighScore();
		Scanner sc = new Scanner(highScoreString);
			highScore = sc.nextInt();
		sc.close();
		
		//set first apple
		apple = new Point(random.nextInt(WIDTH / SCALE), random.nextInt(HEIGHT / SCALE));
		
		//start timer for JPanel
		timer.start();
	}
	
	//snake movement control
	@Override
	public void actionPerformed(ActionEvent arg0) {
		renderPanel.repaint();
		ticks++;
		if(!gameOver && !paused && ticks % 2 == 0) {
			time++;
			snakeBody.add(new Point(head.x, head.y));
			
			if(direction == UP) {
				if(tailCheck(head.x, head.y - 1) && head.y - 1 >=0) {
					head = new Point(head.x, head.y - 1);
				}
				else {
					gameOver = true;
				}
			}
			
			if(direction == DOWN) {
				if(tailCheck(head.x, head.y + 1) && head.y + 1 <= (HEIGHT / SCALE)) {
					head = new Point(head.x, head.y + 1);
				}
				else {
					gameOver = true;
				}
			}
			
			if(direction == RIGHT) {
				if(tailCheck(head.x + 1, head.y) && head.x + 1 <= (WIDTH / SCALE)) {
					head = new Point(head.x + 1, head.y);
				}
				else {
					gameOver = true;
				}
			}
			
			if(direction == LEFT) {
				if(tailCheck(head.x - 1, head.y) && head.x - 1 >= 0) {
					head = new Point(head.x - 1, head.y);
				}
				else {
					gameOver = true;
				}
			}
			
			//remove last snake body with movement
			if(snakeBody.size() > tail) {
				snakeBody.remove(0);
			}
			
			//check if eatting apple
			if(head.equals(apple)) {
				tail++;
				score++;
				//relocate apple after eatting
				do{
					apple.setLocation(random.nextInt(WIDTH / SCALE), random.nextInt(HEIGHT / SCALE));
					overlapping = false;
					for(Point point : snakeBody) {
						if(apple.equals(point)) {
							overlapping = true;
						}
					}
				}while(overlapping); 
			}
		}
		
		//check for new high score
		if(gameOver) {
			if(score > highScore) {
				String temp = "";
				temp = Integer.toString(score);
				//format string to match high score format
				for(int i = temp.length(); i < 6; i++) {
					temp = "0" + temp;
				}
				
				//set new high score
				setHighScore(temp);
			}
		}
		
		
	}
		
	//check if head runs into tail
	public boolean tailCheck(int x, int y) {
		for(Point point : snakeBody) {
			if(point.equals(new Point(x, y))){
				return false;
			}
		}
		
		return true;
	}
	
	
	//MAIN CLASS
	public static void main(String[] args) {
		
		snake = new Snake();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	//controls snake direction with WSAD
	@Override
	public void keyPressed(KeyEvent e) {
		int i = e.getKeyCode();
		if((i == KeyEvent.VK_A ) && direction != RIGHT) {
			direction = LEFT;
		}
		if((i == KeyEvent.VK_D ) && direction != LEFT) {
			direction = RIGHT;
		}
		if((i == KeyEvent.VK_S ) && direction != UP) {
			direction = DOWN;
		}
		if((i == KeyEvent.VK_W ) && direction != DOWN) {
			direction = UP;
		}
		
		
		//space to pause or restart the game
		if(i == KeyEvent.VK_SPACE) {
			if(gameOver) {
				gameOver = false;
				startGame();
			}
			else {
				paused = !paused;
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//get current high score from text file
	public void getHighScore() {
		Path path = Paths.get("HighScore.txt").toAbsolutePath();
		String currentPath = path.toString();
		System.out.println(currentPath);
		File file = new File(currentPath);
				
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			//read file
			String temp;
			while((temp = br.readLine()) != null) {
				highScoreString = temp;
			}
			
			
			br.close();
		}
		catch(IOException e) {
			System.out.println(e + "\nFailed to open HighScore.txt");
		}

	}
	
	//write new high score to text file
	public void setHighScore(String scoreString) {
		Path path = Paths.get("HighScore.txt").toAbsolutePath();
		String currentPath = path.toString();
		File file = new File(currentPath);
	
		try {
			FileWriter fout = new FileWriter(file);
			
			fout.write(scoreString);
			
			fout.close();
		}
		catch(IOException e) {
			System.out.println(e + "\nFailed to write to HighScore.txt\n");
		}
	}
	
}
