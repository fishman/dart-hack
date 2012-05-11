package org.ahmadsoft.ropes.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Returns SVN Info for performance logging. This info can be added to
 * performance logs, so the actual revision on which the test is run is known.
 * Performance numbers can be compared between revisions.
 * 
 * @author Elli Albek
 */
public class SVNInfo {

	/**
	 * Get revision info for performance logging.
	 */
	public static String[] svnInfo() throws IOException {
		Process p = Runtime.getRuntime().exec("svn info");
		String info = readInputStream(p.getInputStream());
		// int exitVal = p.waitFor();
		// if (exitVal != 0)
		// return null;
		return parseInfo(info);
	}

	public static void main(String[] args) throws Exception {
		Process p = Runtime.getRuntime().exec("svn info");
		String info = readInputStream(p.getInputStream());
		System.out.println("Info: " + info);
		System.out.println("Map: " + getInfo(info));
		System.out.println("Parsed: " + Arrays.asList(parseInfo(info)));
	}

	/**
	 * @param p
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private static String readInputStream(InputStream in) throws IOException,
			UnsupportedEncodingException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buff = new byte[256];
		int read = -1;
		while ((read = in.read(buff)) >= 0)
			out.write(buff, 0, read);
		out.close();
		return out.toString();
	}

	private static Map<String, String> getInfo(String svnInfo) {
		HashMap<String, String> map = new HashMap<String, String>();
		StringTokenizer st = new StringTokenizer(svnInfo, "\r\n", false);
		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			int colon = line.indexOf(':');
			map.put(line.substring(0, colon).trim(), line.substring(colon + 1)
					.trim());
		}

		return map;
	}

	private static String getLastPath(String path) {
		int slash = path.lastIndexOf('/');
		return path.substring(slash + 1);
	}

	/**
	 * Returns branch folder and revision number from SVN output.
	 */
	private static String[] parseInfo(String svnInfo) {
		Map<String, String> values = getInfo(svnInfo);
		String[] info = new String[2];
		info[0] = getLastPath(values.get("URL"));
		info[1] = values.get("Revision");
		return info;
	}
}
