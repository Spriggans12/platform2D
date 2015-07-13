package fr.spriggans.jaxbMapping.adapters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import fr.spriggans.game.level.levelObjects.entities.AbstractEntity;
import fr.spriggans.game.level.levelObjects.entities.BasicEnnemy;
import fr.spriggans.game.level.levelObjects.entities.Player;
import fr.spriggans.jaxbMapping.pojos.AdaptedAbstractEntity;
import fr.spriggans.jaxbMapping.pojos.AdaptedAbstractEntityList;

public class AbstractEntityListAdapter extends XmlAdapter<AdaptedAbstractEntityList, List<AbstractEntity>> {

	@Override
	public AdaptedAbstractEntityList marshal(List<AbstractEntity> arg0) throws Exception {
		return null;
	}

	@Override
	public List<AbstractEntity> unmarshal(AdaptedAbstractEntityList arg0) throws Exception {
		final List<AbstractEntity> res = new ArrayList<AbstractEntity>();
		
		// Ajout du player.
		res.add(new Player(arg0.getPlayer().getX(), arg0.getPlayer().getY()));
		
		// Ajout des basic_ennemy.
		for (final AdaptedAbstractEntity e : arg0.getBasicEnnemy()) {
			res.add( new BasicEnnemy(e.getX(), e.getY()));
		}
		
		return res;
	}
}
