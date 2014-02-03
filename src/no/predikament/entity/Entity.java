package no.predikament.entity;

import no.predikament.Game;
import no.predikament.util.Vector2;

public class Entity
{
	protected final Game game;
	protected Vector2 position;
	
	public Entity(Game game)
	{
		this.game = game;
		
		position = Vector2.zero();
	}
	
	public Entity(Game game, Vector2 position)
	{
		this(game);
		
		this.position = position;
	}
	
	public final Vector2 getPosition()
	{
		return position;
	}
	
	public void setPosition(Vector2 position)
	{
		this.position = position;
	}
}
