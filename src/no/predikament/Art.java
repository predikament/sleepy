package no.predikament;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Art 
{
	public static Art instance;
	
	public Bitmap[][] tiles 	= loadAndCut("/images/tiles.png", 16, 16);
	public Bitmap[][] character	= loadAndCut("/images/character.png", 32, 32);
	public Bitmap[][] particles = loadAndCut("/images/particles.png", 16, 16);
	
	public static void init() 
	{
		System.out.println("Initializing art-instance.");
		
		instance = new Art();
	}
	
	private Bitmap[][] loadAndCut(String name, int sw, int sh)
	{
		BufferedImage img;
		
		try
		{
			img = ImageIO.read(Art.class.getResource(name));
		}
		catch (IOException e) 
		{ 
			throw new RuntimeException("Failed to load " + name);
		}
		
		int xSlices = img.getWidth() / sw;
		int ySlices = img.getHeight() / sh;
		
		Bitmap[][] result = new Bitmap[xSlices][ySlices];
		
		for (int x = 0; x < xSlices; ++x)
		{
			for (int y = 0; y < ySlices; ++y)
			{
				result[x][y] = new Bitmap(sw, sh);
				
				img.getRGB(x * sw, y * sh, sw, sh, result[x][y].pixels, 0, sw);
			}
		}
		
		return result;
	}
}