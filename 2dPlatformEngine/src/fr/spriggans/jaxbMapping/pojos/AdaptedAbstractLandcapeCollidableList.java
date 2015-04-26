package fr.spriggans.jaxbMapping.pojos;

import java.util.ArrayList;
import java.util.List;

public class AdaptedAbstractLandcapeCollidableList {

	private List<AdaptedLandcapeCollidableRectangle> rectangle = new ArrayList<AdaptedLandcapeCollidableRectangle>();

	private List<AdaptedLandcapeCollidableEllipse> ellipse = new ArrayList<AdaptedLandcapeCollidableEllipse>();

	public List<AdaptedLandcapeCollidableRectangle> getRectangle() {
		return rectangle;
	}

	public void setRectangle(List<AdaptedLandcapeCollidableRectangle> rectangle) {
		this.rectangle = rectangle;
	}

	public List<AdaptedLandcapeCollidableEllipse> getEllipse() {
		return ellipse;
	}

	public void setEllipse(List<AdaptedLandcapeCollidableEllipse> ellipse) {
		this.ellipse = ellipse;
	}
}
