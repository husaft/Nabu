package ch.unizh.ori.nabu.ui.http.sotm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MbrolaServlet extends HttpServlet {

	private static final long serialVersionUID = 3964690945746676423L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String db = "fr1";
		if (getInitParameter("db") != null) {
			db = getInitParameter("db");
		}
		String ini = "french.ini";
		if (getInitParameter("ini") != null) {
			ini = getInitParameter("ini");
		}
		String key = "fr3";
		if (getInitParameter("key") != null) {
			key = getInitParameter("key");
		}
		String text = "no";
		if (request.getParameter("text") != null)
			text = request.getParameter("text");
		boolean DEBUG = false;
		if (getInitParameter("debug") != null) {
			DEBUG = Boolean.parseBoolean(getInitParameter("debug"));
		}

		File dir = new File(getServletContext().getRealPath("/WEB-INF/mbrola/bin/"));
		String mbrdico = "/mbrdico.linux-gnu";

		String[] cmd = { dir.getAbsolutePath() + mbrdico, ini };
		File tmpDir = new File(dir, "tmp");
		File outFile = File.createTempFile("out." + db, ".wav", tmpDir);
		String[] env = { key + "=" + dir.getAbsolutePath() + "/mbrola -v 1.2 " + dir.getAbsolutePath() + "/../db/" + db
				+ "/" + db + " - " + outFile.getAbsolutePath() };
		if (DEBUG) {
			System.out.println(env[0]);
			System.out.println(cmd[0]);
		}

		Process p = Runtime.getRuntime().exec(cmd, env, dir);

		PrintStream pOut = new PrintStream(p.getOutputStream());
		pOut.print(text);
		pOut.close();

		if (DEBUG) {
			(new Copier(p.getErrorStream(), System.err)).start();
			(new Copier(p.getInputStream(), System.out)).start();
		}
		try {
			p.waitFor();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		ServletOutputStream servletOutputStream = response.getOutputStream();
		InputStream in = new BufferedInputStream(new FileInputStream(outFile));
		response.setHeader("Content-Type", "audio/wav");
		response.setHeader("Content-Length", String.valueOf(outFile.length()));

		copyStream(in, (OutputStream) servletOutputStream);
		in.close();

		if (!DEBUG) {
			outFile.delete();
		}
	}

	private static void copyStream(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[10240];

		out.flush();
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.flush();
	}

	public static class Copier extends Thread {
		InputStream in;

		OutputStream out;

		public Copier(InputStream in, OutputStream out) {
			this.in = in;
			this.out = out;
		}

		public void run() {
			try {
				MbrolaServlet.copyStream(this.in, this.out);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}