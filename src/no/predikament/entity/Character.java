package no.predikament.entity;

import no.predikament.Art;
import no.predikament.Bitmap;
import no.predikament.Game;
import no.predikament.util.Vector2;

public class Character extends PhysicsEntity 
{
	private final int xHitboxOffset = 8;
	private final int yHitboxOffset = 0;
	
	public Character(Game game)
	{
		super(game, Vector2.zero(), Vector2.zero(), new Vector2(16, 32));
	}
	
	public Character(Game game, Vector2 position)
	{
		super(game, position, Vector2.zero(), new Vector2(15, 31));
	}
	
	public void render(Bitmap screen) 
	{
		super.render(screen);
		
		screen.draw(Art.instance.character[0][0], getPosition().getX(), getPosition().getY());
	}

	public void update(double delta) 
	{
		super.update(delta);

		// Translate hitbox by offset to match sprite bounds
		hitbox.translate(xHitboxOffset, yHitboxOffset);
		
		double vel_x = (getVelocity().getX() * 0.99) * delta;
		double vel_y = (getVelocity().getY() * 0.99) * delta;
		
		setVelocity(new Vector2(vel_x, vel_y));	
	}
}