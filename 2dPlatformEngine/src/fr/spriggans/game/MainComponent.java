package fr.spriggans.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import fr.spriggans.gfx.Screen;

public class MainComponent extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	private boolean running = false;
	private Screen screen;
	private BufferedImage img;
	private int pixels[];
	private Thread thread;
	private JFrame frame;
	private Inputs inputs;
	private Game game;

	private final boolean fullScreen = false;

	public void init(final int w, final int h) {
		setPreferredSize(new Dimension(w, h));
		setMinimumSize(new Dimension(w, h));
		setMaximumSize(new Dimension(w, h));
		frame = new JFrame("2D Platform Engine");
		frame.setLayout(new BorderLayout());
		if (fullScreen) {
			frame.setUndecorated(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);

		screen = new Screen(w, h);
		inputs = new Inputs(this);
		game = new Game(inputs, screen);
		img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}

	public void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		final int FPS = 60;
		final double secondsPerTick = 1 / ((double) FPS);
		int ticks = 0;
		int fps = 0;
		double delta = 0;
		long lastTime = System.nanoTime();
		while (running) {
			final long now = System.nanoTime();
			long elapsedTime = now - lastTime;
			lastTime = now;
			if (elapsedTime < 0)
				elapsedTime = 0;
			if (elapsedTime > 1000000000)
				elapsedTime = 1000000000;
			delta += elapsedTime / 1000000000.;
			boolean ticked = false;
			while (delta > secondsPerTick) {
				tick();
				ticks++;
				delta -= secondsPerTick;
				ticked = true;
				if (ticks % FPS == 0) {
					if (Launcher.DEBUG_SHOW_FPS)
						System.out.println("fps : " + fps);
					lastTime += 1000;
					fps = 0;
				}
			}
			if (ticked) {
				render();
				fps++;
			} else
				try {
					Thread.sleep(1);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
		}
		stop();
	}

	private void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void tick() {
		game.tick(screen);
	}

	private void render() {
		final BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		game.render(screen);
		for (int i = 0; i < screen.getPixels().length; i++)
			pixels[i] = screen.getPixels()[i];
		final Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
}
