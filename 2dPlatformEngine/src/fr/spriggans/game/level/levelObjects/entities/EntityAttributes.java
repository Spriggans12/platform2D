package fr.spriggans.game.level.levelObjects.entities;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class EntityAttributes {
	public static final EntityAttributes att_player = new EntityAttributes("PLAYER");
	public static final EntityAttributes att_basic_ennemy = new EntityAttributes("BASIC_ENNEMY");

	private final String FILEPATH = "res/entities/entitiesAttributes.txt";
	private final String WORD_SEPARATOR = ":";

	private Map<String, ArrayList<String>> attributes = new HashMap<String, ArrayList<String>>();

	public EntityAttributes(String entityName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(FILEPATH)));
			String line;
			boolean addAttribute = false;
			while ((line = br.readLine()) != null) {
				String[] params = line.split(WORD_SEPARATOR);
				for (int i = 0; i < params.length; i++)
					if (params[i] != null)
						params[i] = params[i].replaceAll("\\[", "").replaceAll("]", "");
				if ("ENTITY".equals(params[0])) {
					addAttribute = entityName.equals(params[1]);
				} else if (addAttribute && line.startsWith("[")) {
					ArrayList<String> paramList = new ArrayList<String>();
					for (int i = 0; i < params.length - 1; i++) {
						paramList.add(params[i + 1]);
					}
					getAttributes().put(params[0], paramList);
				}
			}
			br.close();
		} catch (Exception e) {
			System.err.println("Erreur lors de l'extraction des attributs des entités.");
			e.printStackTrace();
		}
	}

	public Map<String, ArrayList<String>> getAttributes() {
		return attributes;
	}

	/**
	 * Méthode qui initialise les attributs d'une entité selon la map du
	 * EntityAttributes associé à l'entité.
	 */
	public void initaliseEntityAttributes(AbstractEntity e) {
		Iterator<Entry<String, ArrayList<String>>> it = attributes.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, ArrayList<String>> attr = it.next();
			ArrayList<String> values = attr.getValue();
			switch (attr.getKey()) {
			case "DIMENSIONS":
				e.setEntityWidth(Integer.parseInt(values.get(0)));
				e.setEntityHeight(Integer.parseInt(values.get(1)));
				break;
			case "COLLISIONS_POINTS":
				List<Point> points = new ArrayList<Point>();
				for (int i = 0; i + 1 < values.size(); i += 2) {
					points.add(new Point(Integer.parseInt(values.get(i)), Integer.parseInt(values.get(i + 1))));
				}
				e.setCollisionsPoints(points.toArray(new Point[0]));
				break;
			case "MAX_SPEEDS":
				e.setMaxSpeedX(Float.parseFloat(values.get(0)));
				e.setMaxSpeedY(Float.parseFloat(values.get(1)));
				break;
			case "GROUND_FRICTION":
				e.setGroundFriction(Float.parseFloat(values.get(0)));
				break;
			case "GROUND_ACCELERATION":
				e.setGroundAcceleration(Float.parseFloat(values.get(0)));
				break;
			case "GROUND_JUMP_SPEED":
				e.setGroundJumpSpeed(Float.parseFloat(values.get(0)));
				break;
			case "GRAVITY_STRENGTH":
				e.setGravityStrength(Float.parseFloat(values.get(0)));
				break;
			}
		}
	}
}
