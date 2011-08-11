package cz.ic.resurrection.heroic;

public abstract class Player {
	public static final byte GREY = (byte) 100;
	public static final byte LIGHT = 0;
	public static final byte DARK = 1;
	
	HeroicInterface heroicInterface;
	
	Player(HeroicInterface heroicInterface)
	{
		this.heroicInterface = heroicInterface;
	}
	
	abstract void TakeTurn();
}
