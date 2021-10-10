package ch.unizh.ori.nabu.input.db.http;

import ch.unizh.ori.common.text.OldScript;
import ch.unizh.ori.common.text.OldUnicodeScript;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DBServlet extends HttpServlet {

	private static final long serialVersionUID = 8119897677249029505L;

	@SuppressWarnings("rawtypes")
	private static Map map;

	@SuppressWarnings("rawtypes")
	public static Map getMap() {
		return map;
	}

	static {
		initMap();
	}

	@SuppressWarnings("unchecked")
	private static void initMap() {
		map = new HashMap<Object, Object>();
		map.put("accadian", new TwoColumnVocInfo("Accadian", "lexem", OldUnicodeScript.ACCADIAN, "english", null));
		map.put("arabic", new TwoColumnVocInfo("Arabic", "lexem", OldUnicodeScript.ARABIC, "german",
				(OldScript) new OldUnicodeScript("MacRoman", "default")));
		map.put("ostia", new TwoColumnVocInfo("Ostia", "lexem", null, "german", null));
		map.put("frgrund", new TwoColumnVocInfo("FrGrund", "lexem", null, "german", null));
		map.put("englisch", new TwoColumnVocInfo("Englisch", "lexem", null, "german", null));
		map.put("jenni", new TwoColumnVocInfo("Jenni", "lexem", OldUnicodeScript.HEBREW, "german",
				(OldScript) new OldUnicodeScript("UTF-8", "default")));

		map.put("sanskrit",
				new VocInfo("Sanskrit", new ColumnInfo[] { new ColumnInfo("lexem", OldUnicodeScript.SANSKRIT, "Lexem"),
						new ColumnInfo("lexem_add", null, "Lexem_add"), new ColumnInfo("german", null, "Deutsch"),
						new ColumnInfo("lection", null, "Lektion") }));
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void destroy() {
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		PrintStream out = System.out;

		out.println("contextPath: " + request.getContextPath());
		out.println("pathInfo: " + request.getPathInfo());
		out.println("pathTranslated: " + request.getPathTranslated());
		out.println("QueryString: " + request.getQueryString());
		out.println("getRequestURI: " + request.getRequestURI());
		out.println("getServletPath: " + request.getServletPath());

		String path = request.getPathInfo().substring("/".length());
		System.out.println("path: " + path);
		int i = path.indexOf('/');
		if (i < 0) {
			throw new IllegalArgumentException("Url to short");
		}
		String key = path.substring(0, i);
		System.out.println("key: " + key);
		VocInfo vi = (VocInfo) map.get(key);
		if (vi == null) {
			throw new IllegalArgumentException("Url no good");
		}
		request.setAttribute("vocInfo", vi);
		String s = "/WEB-INF" + vi.getDispatchPath(path.substring(i), request, response);
		System.out.println("s: " + s);
		getServletContext().getRequestDispatcher(s).forward((ServletRequest) request, (ServletResponse) response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	public String getServletInfo() {
		return "Short description";
	}

	public static class VocInfo {
		private static final DBServlet.ColumnInfo vidInfo = new DBServlet.ColumnInfo("vid", null, "vid");

		private String name;

		private String description;
		private String table;
		protected DBServlet.ColumnInfo[] columnInfo;
		private String colNames = null;
		private String colUpdateNames = null;

		public VocInfo() {
		}

		public VocInfo(String name) {
			this.name = this.description = this.table = name;
		}

		public VocInfo(String name, DBServlet.ColumnInfo[] columnInfo) {
			this(name);
			this.columnInfo = columnInfo;
		}

		public String getDispatchPath(String path, HttpServletRequest request, HttpServletResponse response) {
			return "/db" + path + "?" + request.getQueryString();
		}

		public String getColNames() {
			if (this.colNames == null) {
				StringBuffer s = new StringBuffer();
				for (int i = 0; i < (getColumnInfos()).length; i++) {
					if (i != 0) {
						s.append(",");
					}
					s.append(getColumnInfos()[i].getName());
				}
				this.colNames = s.toString();
			}
			return this.colNames;
		}

		public String getColUpdateNames() {
			if (this.colUpdateNames == null) {
				StringBuffer s = new StringBuffer();
				for (int i = 0; i < (getColumnInfos()).length; i++) {
					if (i != 0) {
						s.append(",");
					}
					s.append(getColumnInfos()[i].getName()).append("=?");
				}
				this.colUpdateNames = s.toString();
			}
			return this.colUpdateNames;
		}

		public DBServlet.ColumnInfo getVidInfo() {
			return vidInfo;
		}

		public DBServlet.ColumnInfo[] getColumnInfos() {
			return this.columnInfo;
		}

		public String getDescription() {
			return this.description;
		}

		public String getName() {
			return this.name;
		}

		public String getTableName() {
			return this.table;
		}
	}

	public static class ColumnInfo {
		private String name;
		private OldScript enc;
		private String label;

		public ColumnInfo(String name, OldScript enc, String label) {
			this.name = name;
			if (enc != null) {
				this.enc = enc;
			} else {
				this.enc = (OldScript) new OldUnicodeScript(null, null);
			}
			this.label = label;
		}

		public String getName() {
			return this.name;
		}

		public OldScript getEnc() {
			return this.enc;
		}

		public String getLabel() {
			return this.label;
		}
	}

	public static class TwoColumnVocInfo extends VocInfo {
		public TwoColumnVocInfo(String table, String colAName, OldScript colAEnc, String colBName, OldScript colBEnc) {
			super(table);
			this.columnInfo = new DBServlet.ColumnInfo[] { new DBServlet.ColumnInfo(colAName, colAEnc, colAName),
					new DBServlet.ColumnInfo(colBName, colBEnc, colBName),
					new DBServlet.ColumnInfo("lection", null, "Lektion") };
		}
	}
}