package no.predikament;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;

import no.predikament.entity.Camera;
import no.predikament.entity.Character;
import no.predikament.level.Level;
import no.predikament.util.Stopwatch;
import no.predikament.util.Vector2;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	public static final String 	TITLE 	= "Kittens vs Marbles";
	public static final int 	FPS 	= 60;
	public static final int		HEIGHT 	= 320;
	public static final int 	WIDTH	= (int) (HEIGHT * 16.0 / 9.0);
	public static final int 	SCALE	= 2;
	
	private boolean running;
	
	private BufferedImage screenImage;
	private Bitmap screenBitmap;
	
	private InputHandler inputHandler;
	private Stopwatch frameTimer;
	
	private int currentFrameCount;
	private int updatesPerSecond;
	
	private boolean fpsPrintoutEnabled;
	
	private Character character;
	private Level level;
	private Camera camera;
	
	public Game()
	{
		// Window size
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setFocusable(true);
		
		inputHandler 		= new InputHandler(this);
		fpsPrintoutEnabled 	= true;
		frameTimer 			= new Stopwatch(false);
		
		currentFrameCount 	= 0;
		updatesPerSecond 	= 0;
		
		running = true;
	}
	
	private void init()
	{
		screenImage 	= new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		screenBitmap 	= new Bitmap(screenImage);
		
		character 		= new Character(this, Vector2.zero());
		camera 			= new Camera(this, Vector2.zero(), new Vector2(5, 5));
		level 			= new Level(this, character, camera);
		
		Art.init();
		level.init();
	}
	
	public void start()
	{
		new Thread(this, "Game Thread").start();
	}
	
	public void stop()
	{
		running = false;
	}
	
	private void update(double delta)
	{
		inputHandler.update(delta);
		level.update(delta);
		character.update(delta);
	}
	
	private void render(Bitmap screen)
	{
		screen.clear(0x9dba5e);
		
		level.render(screen);
		character.render(screen);
		
		++currentFrameCount;
	}
	
	private void swap()
	{
		BufferStrategy bs = getBufferStrategy();
		
		if (bs == null)
		{
			System.out.println("Creating buffer strategy (2).");
			
			createBufferStrategy(2);
			
			// bs = getBufferStrategy();
			
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		int sW = getWidth();
		int sH = getHeight();
		int w = WIDTH * SCALE;
		int h = HEIGHT * SCALE;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, sW, sH);
		g.drawImage(screenImage, (sW - w) / 2, (sH - h) / 2, w, h, null);
		g.dispose();
		
		bs.show();
	}
	
	public void run()
	{
		init();
		
		long 	lastTime 		= System.currentTimeMillis();
		double 	lastFrameNS 	= (double) System.nanoTime();
		
		frameTimer.start();
		
		while (running)
		{
			long 	thisTime 	= System.currentTimeMillis();
			double 	thisFrameNS = (double) System.nanoTime();
			double 	deltaTime 	= (thisFrameNS - lastFrameNS) / 1.0E9;
			
			// Update as often as possible
			update(deltaTime);
			
			// Increase UPS counter
			++updatesPerSecond;
			
			// Try to render (approximately) at desired frame rate
			if (frameTimer.getElapsedTime() >= 1000 / FPS)
			{
				render(screenBitmap);
				
				frameTimer.reset();
			}
			
			boolean timeToResetCounters = thisTime - lastTime >= 1000;
			
			if (timeToResetCounters)
			{
				if (fpsPrintoutEnabled)
				{
					String output = String.format("%d fps, %d ups", currentFrameCount, updatesPerSecond);
					
					System.out.println(output);
				}
				
				currentFrameCount 	= 0;
				updatesPerSecond 	= 0;
				
				lastTime = thisTime;
			}
			
			lastFrameNS = (double) System.nanoTime();
			
			swap();
			
			// Sleep 1 millisecond per update cycle to allow potential worker-threads to catch up
			try { Thread.sleep(1); } catch (InterruptedException ie) { ie.printStackTrace(); System.exit(1); }
		}
		
		System.out.printf("Exiting \"%s\". (0)\n", Game.TITLE);
		System.exit(0);
	}
	
	// Private input-handling class
	private class InputHandler implements KeyListener, MouseListener, MouseMotionListener
	{
		private final Game game;
		private Set<Integer> pressedKeys;
		
		public InputHandler(Game game)
		{
			this.game = game;
			
			pressedKeys = Collections.synchronizedSet(new HashSet<Integer>());
			
			game.addKeyListener(this);
			game.addMouseListener(this);
			game.addMouseMotionListener(this);
		}
		
		// MouseListener
		public synchronized void mousePressed(MouseEvent event)
		{
			
		}
		
		public synchronized void mouseReleased(MouseEvent event)
		{
			
		}
		
		public synchronized void mouseClicked(MouseEvent event)
		{
			
		}
		
		public synchronized void mouseEntered(MouseEvent event)
		{
			
		}
		
		public synchronized void mouseExited(MouseEvent event)
		{
			
		}
		
		// MouseMotionListener
		public synchronized void mouseDragged(MouseEvent event)
		{
			
		}
		
		public synchronized void mouseMoved(MouseEvent event)
		{
			
		}
		
		// KeyListener
		public synchronized void keyPressed(KeyEvent event)
		{
			int keycode = event.getKeyCode();
			
			if (pressedKeys.contains(keycode) == false) pressedKeys.add(keycode);
		}
		
		public synchronized void keyReleased(KeyEvent event)
		{
			/*int keycode = event.getKeyCode();
			
			try
			{
				if (pressedKeys.contains(keycode) == true) pressedKeys.remove(keycode);
			}
			catch (ConcurrentModificationException cme)
			{
				// Not handling for now, just printing the stack trace
				cme.printStackTrace();
			}*/
		}
		
		public synchronized void keyTyped(KeyEvent event)
		{
			
		}
		
		public synchronized void update(double delta)
		{
			try
			{
				Set<Integer> handledKeys = new HashSet<Integer>();
				
				for (int keycode : pressedKeys)
				{
					switch(keycode)
					{
						case KeyEvent.VK_LEFT:
						case KeyEvent.VK_A:
							break;
							
						case KeyEvent.VK_RIGHT:
						case KeyEvent.VK_D:
							break;
							
						case KeyEvent.VK_UP:
						case KeyEvent.VK_W:
							break;
							
						case KeyEvent.VK_DOWN:
						case KeyEvent.VK_S:
							break;
						
						case KeyEvent.VK_SPACE:
							break;
						
						case KeyEvent.VK_ESCAPE:
							game.stop();
							break;
							
						case KeyEvent.VK_F9:
							System.out.println("FPS printout: " + (fpsPrintoutEnabled ? "disabled." : "enabled."));
							fpsPrintoutEnabled = !fpsPrintoutEnabled;
							
							break;
						
						default:
							break;
					}
					
					handledKeys.add(keycode);
				}
				
				pressedKeys.removeAll(handledKeys);
			}
			catch (ConcurrentModificationException cme)
			{
				// Not handling this for now, just printing the stack trace
				cme.printStackTrace();
			}
		}
	}
}