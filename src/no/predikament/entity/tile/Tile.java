package no.predikament.entity.tile;

import no.predikament.Art;
import no.predikament.Bitmap;
import no.predikament.Game;
import no.predikament.entity.PhysicsEntity;

public class Tile extends PhysicsEntity 
{
	protected int type;
	
	public Tile(Game game, int type)
	{
		super(game);
		
		setType(type);
	}
	
	public void render(Bitmap screen)
	{
		screen.draw(Art.instance.tiles[getType()][0], getPosition().getX(), getPosition().getY());
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	public final int getType()
	{
		return type;
	}
}
