package net.jsa.arealle.task.geom;

import java.io.IOException;
import java.io.InputStream;

public interface IShape {
	boolean getIsNull();
	void setIsNull(boolean isNull);
	public PointPair getBoundingBox();
	public void setBoundingBox(PointPair boundingBox);
	public void read(InputStream is) throws IOException;
	public String toWkt();
}
