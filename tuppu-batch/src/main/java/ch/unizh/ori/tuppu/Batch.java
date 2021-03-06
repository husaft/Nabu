package ch.unizh.ori.tuppu;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.imageio.ImageIO;

public class Batch {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		String classname = StringPlotter.class.getName();
		String propsText = "";
		if (args.length >= 1) {
			classname = args[0];
		}
		if (args.length >= 2) {
			propsText = args[1];
		}
		try {
			Plotter plotter = (Plotter) Class.forName(classname).newInstance();

			Properties props = new Properties();
			InputStream in = "-".equals(propsText) ? System.in : new ByteArrayInputStream(propsText.getBytes());

			props.load(in);
			in.close();
			plotter.putInitParams(props);

			plotter.init();

			if (args.length < 3) {
				Collection<String> codes = plotter.getCodes();
				if (codes != null) {
					for (Iterator<String> iter = codes.iterator(); iter.hasNext();) {
						String text = iter.next();
						plot(plotter, text, props);
					}
				}
			} else {
				String text = args[2];
				plot(plotter, text, props);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void plot(Plotter plotter, String text, Map<Object, Object> map) throws IOException {
		try {
			BufferedImage img = plotter.plotToImage(text, map);
			String filename = URLEncoder.encode(text, "UTF-8");
			ImageIO.write(img, "png", new File("out/" + filename + ".png"));
			ImageIO.write(img, "jpeg", new File("out/" + filename + ".jpeg"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}