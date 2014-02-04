package no.predikament.level;

import java.util.ArrayList;
import java.util.List;

import no.predikament.Bitmap;
import no.predikament.Game;
import no.predikament.entity.Camera;
import no.predikament.entity.Character;
import no.predikament.entity.tile.Tile;
import no.predikament.util.Vector2;

public class Level 
{
	private final Game 		game;
	@SuppressWarnings("unused")
	private final Character character;
	@SuppressWarnings("unused")
	private final Camera 	camera;
	
	private final List<Tile> tiles;
	private final int TOTAL_TILES_WIDTH 	= 5;
	private final int TOTAL_TILES_HEIGHT 	= 5;
	
	public Level(Game game, Character character, Camera camera)
	{
		this.game = game;
		this.character = character;
		this.camera = camera;
		
		tiles = new ArrayList<Tile>();
	}
	
	public void init()
	{
		tiles.clear();
				
		for (int x = 0; x < TOTAL_TILES_WIDTH; ++x)
		{
			for (int y = 0; y < TOTAL_TILES_HEIGHT; ++y)
			{
				Tile t = new Tile(game, 1);
				
				t.setPosition(new Vector2(x * 16, y * 16));
				
				tiles.add(t);
			}
		}
	}
	
	public Tile getTile(int x, int y)
	{
		Tile t = null;
		
		int tnr = TOTAL_TILES_HEIGHT * x + y;
		
		if (tnr >= 0 && tnr < tiles.size()) t = tiles.get(tnr);
		
		return t;
	}
	
	public void render(Bitmap screen) 
	{
		for (Tile t : tiles)
		{
			t.render(screen);
		}
	}
	
	public void update(double delta) 
	{
		
	}
}