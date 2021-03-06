package game;

import java.awt.Graphics;
import java.awt.image.BufferStrategy; // (the way of the computer to draw things)

/*
 * buffers are "hidden" computer screen which you can draw to.
 * for example, you draw to the first buffer, then to the second, then to the actual screen.
 * BUFFER -> BUFFER -> SCREEN
 * this way you prevent flickering
 */
import game.display.Display;
import game.gfx.Assets;
import game.gfx.GameCamera;
import game.input.KeyManager;
import game.input.MouseManager;
import game.states.GameState;
import game.states.MenuState;
import game.states.State;



public class Game implements Runnable {

	private Display display;
	
	public String title;
	private int width, height;
	
	boolean running = false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics g; 
	
	//states
	public State gameState; //creating the states (=screen)
	public State menuState;
	
	//input
	private KeyManager keyManager;
	private MouseManager mouseManager;
	
	//camera
	private GameCamera gameCamera;
	
	//handler
	private Handler handler;
	
	
	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		keyManager = new KeyManager();
		mouseManager = new MouseManager();
	}
	
	
	private void init() {
		//creating a window
		display = new Display(title, width, height);
		display.getJframe().addKeyListener(keyManager); //adding the key-listener to our window
		
		display.getJframe().addMouseListener(mouseManager); //getting the mouse-listener and motion-listener to our window and canvas
		display.getJframe().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager); 
		display.getCanvas().addMouseMotionListener(mouseManager);
		
		Assets.init(); //initializing all of the assets
		
		handler = new Handler(this);
		gameCamera = new GameCamera(handler , 0, 0); //initializing the game camera
		
		gameState = new GameState(handler); //initializing the states 
		menuState = new MenuState(handler);
		State.setState(menuState);
	}
	
	private void tick() { 
		keyManager.tick();
		
		if (State.getState() != null) // if we have an actual state on, do the tick method
			State.getState().tick();
	}

	private void render() {
		
		bs = display.getCanvas().getBufferStrategy(); //setting the BufferedStrategy to the currently bufferStrategy of this canvas
		
		if(bs == null) { //if the canvas has no bufferStrategy
			display.getCanvas().createBufferStrategy(3); //creating 3 buffers
			return;
		}
		
		g = bs.getDrawGraphics(); // allow the Graphics to draw on the canvas
		//clear screen
		g.clearRect(0, 0, width, height);
		//Draw here!
		
		if (State.getState() != null) // if we have an actual state on, do the render method
			State.getState().render(g);
		
		
		
		//End drawing!
		bs.show();
		g.dispose();
	}
	
	
	public void run() { //activating when the thread initialize
		
		init();
		
		int fps = 60; //how many time we want to run the update and render method every second
		double timePerTick = 1000000000 / fps; //measures in nanoseconds (maximum amount of time we allowed to run these methods)
		double deltaTime = 0; 
		long now; 
		long lastTime = System.nanoTime(); 
		long timer = 0;
		int ticks = 0; //how many times the tick and render method were called
		
		
				
		while(running) {
			now  = System.nanoTime(); //the current time of the computer but in nanoseconds
			deltaTime += now - lastTime; //the elapsed time since we have called the update and render methods again
			timer += now - lastTime;
			lastTime = now;
			
			
			if(deltaTime >= timePerTick) {
				tick();
				render();
				ticks++;
				deltaTime -= timePerTick;
			}
				
			/*
			if(timer >= 1000000000) {
				System.out.println("the FPS: " + ticks);
				ticks = 0;
				timer = 0;
			}
			*/
		}
		
		stop();
	}
	
	
	//getters
	public KeyManager getKeyManager() {
		return keyManager;
	}
	
	public MouseManager getMouseManager() {
		return mouseManager;
	}
	
	
	public GameCamera getGameCamera() {
		return gameCamera;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	
	
	
	//threads
	public synchronized void start() {
		if(running) //safety check
			return;
		//initializing the thread
		running = true;
		thread = new Thread(this); //giving the thread this class and allowing by implementing Runnable
		thread.start(); // running the "run" method
	}
	
public synchronized void stop() {
	if(!running) //safety check
		return;
	running = false;
	
	//terminating this thread
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	


}	