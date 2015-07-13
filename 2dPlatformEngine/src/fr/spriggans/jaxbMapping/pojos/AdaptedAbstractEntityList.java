package fr.spriggans.jaxbMapping.pojos;

import java.util.ArrayList;
import java.util.List;

public class AdaptedAbstractEntityList {

	private AdaptedAbstractEntity player;
	
	private List<AdaptedAbstractEntity> basicEnnemy = new ArrayList<AdaptedAbstractEntity>();

	public AdaptedAbstractEntity getPlayer() {
		return player;
	}

	public void setPlayer(AdaptedAbstractEntity player) {
		this.player = player;
	}

	public List<AdaptedAbstractEntity> getBasicEnnemy() {
		return basicEnnemy;
	}

	public void setBasicEnnemy(List<AdaptedAbstractEntity> basicEnnemy) {
		this.basicEnnemy = basicEnnemy;
	}
}
