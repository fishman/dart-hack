package org.ahmadsoft.ropes.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Performance stats file. Uses CSV format. This class fails silently, it logs
 * errors but does not throw exceptions.
 * 
 * @author Elli Albek
 */

public class StatsFile {

	private File file;
	private boolean newLine = true;

	/**
	 * base values: a list of values added in the beginning of every line, like
	 * test date.
	 */
	private ArrayList<? extends Object> baseValues;

	StatsFile(String name, String header, List<String> baseValues) {
		if (baseValues != null || !baseValues.isEmpty())
			this.baseValues = new ArrayList<String>(baseValues);
		File f = new File(name);

		try {
			if (f.exists() == false) {
				f.getAbsoluteFile().getParentFile().mkdirs();
				FileWriter w = new FileWriter(f);
				try {
					w.write(header);
				} finally {
					w.close();
				}
			}
			// new file is ready, no errors
			file = f;
		} catch (IOException e) {
			// log and fail silently
			// this.file will not be set, so no further errors in writing.
			e.printStackTrace();
		}
	}

	/**
	 * Writes a line of values in CSV format.
	 */
	public void writeCSVLine(List<? extends Object> line) {
		if (file == null || line.isEmpty())
			return;

		// has base values, add to every line.
		if (baseValues != null) {
			List<Object> newLine = (ArrayList<Object>) baseValues.clone();
			newLine.addAll(line);
			line = newLine;
		}

		try {
			FileWriter fw = new FileWriter(file, true);
			try {
				BufferedWriter w = new BufferedWriter(fw, 1024);
				w.write('\n');
				writeValues(line, w);
				w.flush();
			} finally {
				fw.close();
			}
		} catch (IOException e) {
			// fail silently and stop writing.
			file = null;
			e.printStackTrace();
		}
	}

	public void writeCSVParam(Object param) {
		if (file == null)
			return;
		try {
			FileWriter fw = new FileWriter(file, true);
			try {
				BufferedWriter w = new BufferedWriter(fw, 1024);
				if (newLine) {
					newLine = false;
					w.write('\n');
					if (baseValues != null) {
						writeValues(baseValues, w);
						w.write(',');
					}
				} else
					w.write(',');

				w.write('"');
				w.write(param.toString());
				w.write('"');

				w.flush();
			} finally {
				fw.close();
			}
		} catch (IOException e) {
			// fail silently and stop writing.
			file = null;
			e.printStackTrace();
		}
	}

	private static void writeValues(List<? extends Object> line, Writer w)
			throws IOException {
		for (int i = 0; i < line.size(); i++) {
			if (i > 0)
				w.write(',');
			w.write('"');
			w.write(line.get(i).toString());
			w.write('"');
		}
	}

	/**
	 * Factory method that creates a stats file with date and revision
	 * information.
	 */
	public static StatsFile createSVNStatsFile(String file, String header) {
		ArrayList<String> baseValues = new ArrayList<String>();
		baseValues.add(new Date(System.currentTimeMillis()).toString());

		String fullHeader = "Date,";
		String[] svnInfo = null;
		try {
			svnInfo = SVNInfo.svnInfo();
			if (svnInfo != null) {
				fullHeader += "Branch,Revision,";
				baseValues.add(svnInfo[0]);
				baseValues.add(svnInfo[1]);
			}
		} catch (Exception e) {
			// can't get info, do not add to file.
		}
		fullHeader += header;

		return new StatsFile(file, fullHeader, baseValues);
	}
	
	public static StatsFile createSVNStatsFile(String file, List<String> header) {
		StringWriter w = new StringWriter();
		try {
			writeValues(header, w);
			w.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return createSVNStatsFile(file, w.toString());
	}
}
