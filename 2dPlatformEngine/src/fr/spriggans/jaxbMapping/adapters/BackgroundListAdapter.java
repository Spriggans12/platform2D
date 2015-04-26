package fr.spriggans.jaxbMapping.adapters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import fr.spriggans.game.level.levelObjects.background.Background;
import fr.spriggans.jaxbMapping.pojos.AdaptedBackground;
import fr.spriggans.jaxbMapping.pojos.AdaptedBackgroundList;

public class BackgroundListAdapter extends XmlAdapter<AdaptedBackgroundList, List<Background>> {

	@Override
	public AdaptedBackgroundList marshal(List<Background> arg0) throws Exception {
		return null;
	}

	@Override
	public List<Background> unmarshal(AdaptedBackgroundList arg0) throws Exception {
		final List<Background> res = new ArrayList<Background>();

		for (final AdaptedBackground e : arg0.getBackground()) {
			res.add(new Background(e.getX(), e.getY()));
		}
		return res;
	}
}
