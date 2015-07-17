package model;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Tim Robertson
 */
public class NamedGeometry {
	private Geometry geometry;
	private Object name;
	private Object name2;

	public NamedGeometry(Geometry geometry, Object name, Object name2) {
		this.geometry = geometry;
		this.name = name;
		this.name2 = name2;
	}

	public Geometry getGeomtery() {
		return geometry;
	}

	public Object getName() {
		return name;
	}

	public Object getName2() {
		return name2;
	}

	public String toString() {
		return "GEOM:" + name + name2;
	}
}
