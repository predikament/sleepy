package no.predikament.entity;

import no.predikament.Game;
import no.predikament.util.Vector2;

public class Camera extends Entity 
{
	protected Vector2 size;
	
	public Camera(Game game)
	{
		super(game);
		
		setSize(new Vector2(1, 1));
	}
	
	public Camera(Game game, Vector2 position, Vector2 size)
	{
		super(game, position);
		
		setSize(size);
	}
	
	public Vector2 getSize() 
	{
		return size;
	}

	public void setSize(Vector2 size) 
	{
		if (size.getX() >= 0 && size.getY() >= 0) this.size = size;
		else this.size = new Vector2(1, 1);
	}
}
