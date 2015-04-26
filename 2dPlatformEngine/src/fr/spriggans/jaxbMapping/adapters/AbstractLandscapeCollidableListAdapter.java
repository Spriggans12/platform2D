package fr.spriggans.jaxbMapping.adapters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import fr.spriggans.game.level.levelObjects.landscape.AbstractLandscapeCollidable;
import fr.spriggans.game.level.levelObjects.landscape.LandscapeCollidableEllipse;
import fr.spriggans.game.level.levelObjects.landscape.LandscapeCollidableRectangle;
import fr.spriggans.jaxbMapping.pojos.AdaptedAbstractLandcapeCollidableList;
import fr.spriggans.jaxbMapping.pojos.AdaptedLandcapeCollidableEllipse;
import fr.spriggans.jaxbMapping.pojos.AdaptedLandcapeCollidableRectangle;

public class AbstractLandscapeCollidableListAdapter extends
		XmlAdapter<AdaptedAbstractLandcapeCollidableList, List<AbstractLandscapeCollidable>> {

	@Override
	public AdaptedAbstractLandcapeCollidableList marshal(List<AbstractLandscapeCollidable> arg0) throws Exception {
		return null;
	}

	@Override
	public List<AbstractLandscapeCollidable> unmarshal(AdaptedAbstractLandcapeCollidableList arg0) throws Exception {
		final List<AbstractLandscapeCollidable> res = new ArrayList<AbstractLandscapeCollidable>();

		for (final AdaptedLandcapeCollidableRectangle e : arg0.getRectangle()) {
			if (e.getSkewed())
				res.add(new LandscapeCollidableRectangle(e.getX(), e.getY(), e.getW(), e.getH(), e.getDeltaH()));
			else
				res.add(new LandscapeCollidableRectangle(e.getX(), e.getY(), e.getW(), e.getH()));
		}

		for (final AdaptedLandcapeCollidableEllipse e : arg0.getEllipse()) {
			res.add(new LandscapeCollidableEllipse(e.getX(), e.getY(), e.getW(), e.getH()));
		}
		return res;
	}
}
