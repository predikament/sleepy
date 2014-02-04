package no.predikament;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import no.predikament.util.Vector2;

public class Bitmap 
{
	public final int[] pixels;
	public final int w, h;
	
	public Bitmap(int w, int h) 
	{
		this(w, h, new int[w * h]);
	}
	
	public Bitmap(int w, int h, int[] pixels) 
	{
		this.w = w;
		this.h = h;
		this.pixels = pixels;
	}

	public Bitmap(BufferedImage img) 
	{
		this.w = img.getWidth();
		this.h = img.getHeight();
		this.pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}
	
	public void clear()
	{
		clear(0);
	}
	
	public void clear(int color)
	{
		//int color = ((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff); //65536 * r + 256 * g + b;
		// Temporary fix: adjusts all incoming color alphas to fully opaque
		int n_color = 0xFF000000 + color;
		Arrays.fill(pixels, n_color);
	}
	
	public void replaceColor(int c0, int c1)
	{
		for (int y = 0; y < h; ++y)
		{
			for (int x = 0; x < w; ++x)
			{
				if (pixels[x + y * w] == c0) pixels[x + y * w] = c1;
			}
		}
	}
	
	public void setPixel(double x, double y, int color) 
	{
		setPixel((int) x, (int) y, color);
	}
	
	public void setPixel(int x, int y, int color)
	{
		int cp = x + y * w;
		
		if (cp >= 0 && cp < pixels.length) pixels[cp] = color;
	}
	
	public int getPixel(int x, int y)
	{
		int cp = x + y * w;
		
		if (cp >= 0 && cp < pixels.length) return pixels[x + y * w];
		
		return 0;
	}
	
	public void drawLine(double x0, double y0, double x1, double y1, int color)
	{
		drawLine((int) x0, (int) y0, (int) x1, (int) y1, color);
	}

	public void drawLine(int x0, int y0, int x1, int y1, int color)
	{
		boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
		
		if (steep)
		{
			// Swap X's with Y's
			int temp = x0;
			x0 = y0;
			y0 = temp;
			temp = x1;
			x1 = y1;
			y1 = temp;
		}
		if (x0 > x1)
		{
			// Swap X's and X's and Y's with Y's
			int temp = x0;
			x0 = x1;
			x1 = temp;
			temp = y0;
			y0 = y1;
			y1 = temp;
		}
		
		int deltaX = x1 - x0;
		int deltaY = Math.abs(y1 - y0);
		int error = deltaX / 2;
		int ystep = 0;
		int y = y0;
		if (y0 < y1) ystep = 1;
		else ystep = -1;
		
		for (int x = x0; x <= x1; ++x)
		{
			if (steep) setPixel(y, x, color);
			else setPixel(x, y, color);
			
			error = error - deltaY;
			
			if (error < 0)
			{
				y = y + ystep;
				error += deltaX;
			}
		}
	}
	
	public void drawCircle(double x, double y, int radius, int color)
	{
		drawCircle((int) x, (int) y, radius, color);
	}
	
	public void drawCircle(int x, int y, int radius, int color)
	{
		int x0 = radius;
		int y0 = 0;
		int radiusError = 1-x0;
		
		while (x0 >= y0)
		{
			setPixel(x0 + x, y0 + y, color);
			setPixel(y0 + x, x0 + y, color);
			setPixel(-x0 + x, y0 + y, color);
			setPixel(-y0 + x, x0 + y, color);
			setPixel(-x0 + x, -y0 + y, color);
			setPixel(-y0 + x, -x0 + y, color);
			setPixel(x0 + x, -y0 + y, color);
			setPixel(y0 + x, -x0 + y, color);
			
			y0++;
			
			if (radiusError < 0) radiusError += 2 * y0 + 1;
			else radiusError += 2 * (y0 - --x0) + 1;
		}
	}
	
	public void drawRectangle(double x0, double y0, double x1, double y1, int color)
	{
		drawRectangle((int) x0, (int) y0, (int) x1, (int) y1, color);
	}
	
	public void drawRectangle(int x0, int y0, int x1, int y1, int color)
	{
		drawLine(x0, y0, x1, y0, color);
		drawLine(x0, y1, x1, y1, color);
		drawLine(x0, y0, x0, y1, color);
		drawLine(x1, y0, x1, y1, color);
	}
	
	public void fill(int x0, int y0, int x1, int y1, int color)
	{
		if (x0 < 0) x0 = 0;
		if (y0 < 0) y0 = 0;
		if (x1 > w) x1 = w;
		if (y1 > h) y1 = h;
		
		for (int y = y0; y < y1; ++y)
		{
			for (int x = x0; x < x1; ++x)
			{
				pixels[x + y * w] = color;
			}
		}
	}
	
	public void blend(Bitmap other, double percentage)
	{
		blend(other, 0, 0, percentage);
	}
	
	public void blend(Bitmap other, double x0, double y0, double percentage)
	{
		blend(other, (int) x0, (int) y0, percentage);
	}
	
	public void blend(Bitmap other, int x0, int y0, double percentage)
	{
		for (int y = 0; y < other.h; ++y)
		{
			for (int x = 0; x < other.w; ++x)
			{
				int xa = x + x0;
				int ya = y + y0;
				
				if (xa >= 0 && xa < w && ya >= 0 && ya < h)
				{
					// Blends all pixels at targeted position by percentage
					int srcA = (other.pixels[x + y * other.w] & ~0x00FFFFFF) >>> 24;
					int srcR = (other.pixels[x + y * other.w] & ~0xFF00FFFF) >>> 16;
					int srcG = (other.pixels[x + y * other.w] & ~0xFFFF00FF) >>> 8;
					int srcB = other.pixels[x + y * other.w] & ~0xFFFFFF00;
					
					int dstA = (pixels[xa + ya * w] & ~0x00FFFFFF) >>> 24;
					int dstR = (pixels[xa + ya * w] & ~0xFF00FFFF) >>> 16;
					int dstG = (pixels[xa + ya * w] & ~0xFFFF00FF) >>> 8;
					int dstB = pixels[xa + ya * w] & ~0xFFFFFF00;
					
					int newA = dstA + (int) ((srcA - dstA) * percentage);
					int newR = dstR + (int) ((srcR - dstR) * percentage);
					int newG = dstG + (int) ((srcG - dstG) * percentage);
					int newB = dstB + (int) ((srcB - dstB) * percentage);
					
					pixels[xa + ya * w] = (newA << 24) | (newR << 16) | (newG << 8) | newB; 
				}
			}
		}
	}
	
	public void draw(Bitmap b)
	{
		draw(b, 0, 0);
	}
	
	public void draw(Bitmap b, double xp, double yp)
	{
		draw(b, (int) xp, (int) yp);
	}
	
	public void draw(Bitmap b, int xp, int yp)
	{
		draw(b, xp, yp, false);
	}
	
	public void draw(Bitmap b, double xp, double yp, boolean xFlip)
	{
		draw(b, (int) xp, (int) yp, xFlip);
	}
	
	public void draw(Bitmap b, int xp, int yp, boolean xFlip)
	{
		if (xFlip)
		{
			for (int x = 0; x < b.w; ++x)
			{
				for (int y = 0; y < b.h; ++y)
				{
					int dx = xp + x;
					int dy = yp + y;
					
					if (dx >= 0 && dx < w && dy >= 0 && dy < h)
					{
						int c = b.pixels[(b.w - 1 - x) + y * b.w];
						
						if (c < 0) pixels[dx + dy * w] = c;
					}
				}
			}
		}
		else
		{
			for (int x = 0; x < b.w; ++x)
			{
				for (int y = 0; y < b.h; ++y)
				{
					int dx = xp + x;
					int dy = yp + y;
					
					if (dx >= 0 && dx < w && dy >= 0 && dy < h)
					{
						int c = b.pixels[x + y * b.w];
						
						if (c < 0) pixels[dx + dy * w] = c;
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void draw(Bitmap b, double xp, double yp, double angle, boolean xFlip)
	{
		draw(b, (int) xp, (int) yp, 0, false);
	}
	
	// Working draw function with rotation!
	@SuppressWarnings("unused")
	private void drawRotated(Bitmap b, int xp, int yp, double angle, boolean xFlip)
	{
		double angleRad = Math.toRadians(angle);
		
		Vector2 v0 = new Vector2(1 * Math.cos(angleRad) - 1 * Math.sin(angleRad), 1 * Math.sin(angleRad) + 1 * Math.cos(angleRad));
		Vector2 v1 = new Vector2(1 * Math.cos(angleRad) - (b.h - 1) * Math.sin(angleRad), 1 * Math.sin(angleRad) + (b.h - 1) * Math.cos(angleRad));
		Vector2 v2 = new Vector2((b.w - 1) * Math.cos(angleRad) - 1 * Math.sin(angleRad), (b.w - 1) * Math.sin(angleRad) + 1 * Math.cos(angleRad));
		Vector2 v3 = new Vector2((b.w - 1) * Math.cos(angleRad) - (b.h - 1) * Math.sin(angleRad), (b.w - 1) * Math.sin(angleRad) + (b.h - 1) * Math.cos(angleRad));
		
		int xMin = (int) Math.floor(Math.min(v0.getX(), Math.min(v1.getX(), Math.min(v2.getX(), v3.getX()))));
		int yMin = (int) Math.floor(Math.min(v0.getY(), Math.min(v1.getY(), Math.min(v2.getY(), v3.getY()))));
		int xMax = (int) Math.ceil(Math.max(v0.getX(), Math.max(v1.getX(), Math.max(v2.getX(), v3.getX()))));
		int yMax = (int) Math.ceil(Math.max(v0.getY(), Math.max(v1.getY(), Math.max(v2.getY(), v3.getY()))));
		
		for (int y = yMin; y <= yMax; y++)
		{
			for (int x = xMin; x <= xMax; x++)
			{
				int rx = (int) (x * Math.cos(-angleRad) - y * Math.sin(-angleRad));
				int ry = (int) (x * Math.sin(-angleRad) + y * Math.cos(-angleRad));
				
				if (rx >= 0 && rx < b.w && ry >= 0 && ry < b.h)
				{
					int p = xFlip ? b.getPixel((b.w - 1) - rx, ry) : b.getPixel(rx, ry);
					
					if (p < 0) setPixel(xp + x, yp + y, p);
				}
			}
		}
	}
	
	// Old draw function with no rotation
	@SuppressWarnings("unused")
	private void drawOld(Bitmap b, int xp, int yp, boolean xFlip) 
	{
		int x0 = xp;
		int x1 = xp + b.w;
		int y0 = yp;
		int y1 = yp + b.h;
		
		if (x0 < 0) x0 = 0;
		if (y0 < 0) y0 = 0;
		if (x1 > w) x1 = w;
		if (y1 > h) y1 = h;
		
		if (xFlip)
		{
			for (int y = y0; y < y1; ++y)
			{
				int sp = (y - y0) * b.w + xp + b.w - 1;
				int dp = y * w;
				
				for (int x = x0; x < x1; ++x)
				{
					int c = b.pixels[sp - x];
					
					if (c < 0) pixels[dp + x] = c; // Transparency
				}
			}
		}
		else
		{
			for (int y = y0; y < y1; ++y)
			{
				int sp = (y - y0) * b.w - xp;
				int dp = y * w;
				
				for (int x = x0; x < x1; ++x)
				{
					int c = b.pixels[sp + x];

					if (c < 0) pixels[dp + x] = c; // Transparency
				}
			}
		}
	}
	
	// Working forward mapped draw function with centered pivot point
	@SuppressWarnings("unused")
	private void drawOldRotation2(Bitmap b, int xp, int yp, double angle) 
	{
		double angleInRad = Math.toRadians(angle);
		
		double cX = b.w / 2.0;
		double cY = b.h / 2.0;
		
		for (int y = 0; y < b.h; ++y)
		{
			double ccY = cY - y;
			
			for (int x = 0; x < b.w; ++x)
			{
				double ccX = cX - x;
				
				double rx = ccX * Math.cos(angleInRad) - ccY * Math.sin(angleInRad) + (xp + cX);
				double ry = ccX * Math.sin(angleInRad) + ccY * Math.cos(angleInRad) + (yp + cY);
				
				int rp = (int) rx + (int) ry * w;
				
				if (rp >= 0 && rp < pixels.length)
				{
					int c = b.pixels[x + y * b.w];
					
					if (c < 0) pixels[rp] = c;
				}
			}
		}
	}
	
	// Working forward mapped draw function
	@SuppressWarnings("unused")
	private void drawOldRotation1(Bitmap b, int xp, int yp, double angle) 
	{
		double angleInRad = (Math.PI / 180.0) * angle;
		
		for (int y = 0; y < b.h; ++y)
		{
			for (int x = 0; x < b.w; ++x)
			{
				double rx = x * Math.cos(angleInRad) - y * Math.sin(angleInRad) + xp;
				double ry = x * Math.sin(angleInRad) + y * Math.cos(angleInRad) + yp;
				
				int rp = (int) rx + (int) ry * w;
				
				if (rp >= 0 && rp < pixels.length)
				{
					int c = b.pixels[x + y * b.w];
					
					if (c < 0) pixels[rp] = c;
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	// Creates a kernel for use with a Gaussian-filtered blur
	// Currently unused
	private double[][] createFilter()
	{
		double result[][] = new double[5][5];
		
		// set standard deviation to 1.0
	    double sigma = 1.0;
	    double r, s = 2.0 * sigma * sigma;
	 
	    // sum is for normalization
	    double sum = 0.0;
	 
	    // generate 5x5 kernel
	    for (int x = -2; x <= 2; x++)
	    {
	        for(int y = -2; y <= 2; y++)
	        {
	            r = Math.sqrt(x*x + y*y);
	            result[x + 2][y + 2] = (Math.exp(-(r*r)/s))/(Math.PI * s);
	            sum += result[x + 2][y + 2];
	        }
	    }
	 
	    // normalize the Kernel
	    for(int i = 0; i < 5; ++i)
	    {
	        for(int j = 0; j < 5; ++j)
	        {
	            result[i][j] /= sum;
	        }
	    }
		
		return result;
	}
}