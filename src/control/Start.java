package control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.DatabaseHelper;

public class Start {

	public static void main(String[] args) {
		try {
			final Start start = new Start();
			final EError errcode = start.chkBoot();

			if (errcode.equals(EError.NO_ERROR)) {
				start.init();
			} else {
				System.err.println(errcode.getErrMessage() + " (" + errcode.getErrCode() + ")");
				System.exit(errcode.getErrCode());
			}

		} catch (final ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check basic configuration that can prevent the program from running properly.
	 *
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public EError chkBoot() {
		// Database verification
		try {
			final File file = new File(Config.DBENVFILEPATH_OLD);
			final FileReader fileReader = new FileReader(file);
			fileReader.close();
		} catch (final IOException e) {
			writeCrashLog(EError.MISSING_INVALID_CONFIG, e);
			e.printStackTrace();
			return EError.MISSING_INVALID_CONFIG;
		}

		try {
			final DatabaseHelper db = new DatabaseHelper(new Config());
			db.close();
		} catch (ClassNotFoundException | SQLException e) {
			writeCrashLog(EError.UNREACHABLE_DATABASE, e);
			e.printStackTrace();
			return EError.UNREACHABLE_DATABASE;
		}

		return EError.NO_ERROR;
	}

	/**
	 * Writes crash logs
	 * @param error
	 * @param ex
	 */
	public void writeCrashLog(EError error, Exception ex) {
		final Date date = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yy-HH-mm-ss");
		final String formattedDate = sdf.format(date);

		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("./" + formattedDate + "_crashlog.txt");
			final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);

			final StringBuilder content = new StringBuilder("An unexpected error as occured during preboot verifications.");
			content.append("\n\nError message > ").append(error.getErrMessage());
			content.append("\n\n");

			bufferedWriter.write(content.toString());
			bufferedWriter.append(sw.toString());

			bufferedWriter.close();
			fileWriter.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialization phase, post checking
	 *
	 * @throws ParseException
	 */
	public void init() throws ParseException {
		new Controller();
	}
}
