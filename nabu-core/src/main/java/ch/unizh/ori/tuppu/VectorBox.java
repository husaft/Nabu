package ch.unizh.ori.tuppu;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VectorBox extends Box {
	public static int LEFT_TO_RIGHT = 0;
	public static int TOP_TO_DOWN = 1;

	private List<Box> l = new ArrayList<Box>();

	public int direction = LEFT_TO_RIGHT;

	public void add(Box r) {
		this.l.add(r);
	}

	public void calcSize(Dimension d) {
		for (Iterator<Box> iter = this.l.iterator(); iter.hasNext();) {
			Box r = iter.next();
			Dimension sub = r.getSize();
			if (this.direction == LEFT_TO_RIGHT) {
				d.width += sub.width;
				d.height = Math.max(d.height, sub.height);
				continue;
			}
			if (this.direction == TOP_TO_DOWN) {
				d.height += sub.height;
				d.width = Math.max(d.width, sub.width);
			}
		}
	}

	public void paint(Graphics2D g, Point pen) {
		Dimension d = getSize();

		Point subPen = new Point(pen);
		if (this.direction == LEFT_TO_RIGHT) {
			for (Iterator<Box> iter = this.l.iterator(); iter.hasNext();) {
				Box r = iter.next();
				Dimension sub = r.getSize();
				pen.y += (sub.height - d.height) / 2;
				r.paint(g, subPen);
				subPen.y = pen.y;
			}
			pen.setLocation(subPen.x, pen.y - d.height);
		} else if (this.direction == TOP_TO_DOWN) {
			for (int i = this.l.size() - 1; i >= 0; i--) {
				Box r = this.l.get(i);
				Dimension sub = r.getSize();
				pen.x -= (sub.width - d.width) / 2;
				r.paint(g, subPen);
				subPen.x = pen.x;
			}
			pen.setLocation(pen.x + d.width, subPen.y);
		}
	}
}