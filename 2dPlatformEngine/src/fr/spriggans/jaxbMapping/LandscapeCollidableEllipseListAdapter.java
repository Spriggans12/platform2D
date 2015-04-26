package fr.spriggans.jaxbMapping;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import fr.spriggans.game.level.levelObjects.landscape.LandscapeCollidableEllipse;

public class LandscapeCollidableEllipseListAdapter extends
XmlAdapter<AdaptedLandcapeCollidableEllipseList, List<LandscapeCollidableEllipse>> {

	@Override
	public AdaptedLandcapeCollidableEllipseList marshal(List<LandscapeCollidableEllipse> arg0) throws Exception {
		return null;
	}

	@Override
	public List<LandscapeCollidableEllipse> unmarshal(AdaptedLandcapeCollidableEllipseList arg0) throws Exception {
		final List<LandscapeCollidableEllipse> res = new ArrayList<LandscapeCollidableEllipse>();
		for (final AdaptedLandcapeCollidableEllipse e : arg0.getList()) {
			res.add(new LandscapeCollidableEllipse(e.getxNW(), e.getyNW(), e.getTotalW(), e.getTotalH()));
		}
		return res;
	}

}
