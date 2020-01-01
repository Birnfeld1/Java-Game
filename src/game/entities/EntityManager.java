package game.entities;

import java.awt.Graphics;
import java.util.ArrayList;

import game.Handler;
import game.entities.creatures.Player;

public class EntityManager {
	
	private Handler handler;
	private Player player;
	private ArrayList<Entity> entities; //the array of all of our entities (infinite)
	
	
	
	public EntityManager(Handler handler, Player player) {
		this.handler = handler;
		this.player = player;
		
		entities = new ArrayList<Entity>();
		addEntity(player);
	}
	
	
	public void tick() {
		for (int i = 0; i < entities.size(); i++) { //ticking each entity in the game
			Entity e = entities.get(i);
			e.tick();
		}
	}
	
	
	public void render(Graphics g) {
		for (int i = 0; i < entities.size(); i++) { //rendering each entity in the game
			Entity e = entities.get(i);
			e.render(g);
		}
	}


	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	
	
	
	
	//GETTERS SETTERS
	public Handler getHandler() {
		return handler;
	}


	public void setHandler(Handler handler) {
		this.handler = handler;
	}


	public Player getPlayer() {
		return player;
	}


	public void setPlayer(Player player) {
		this.player = player;
	}


	public ArrayList<Entity> getEntities() {
		return entities;
	}


	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}
	
	
}
