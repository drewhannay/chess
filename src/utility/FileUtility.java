package utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

public final class FileUtility
{
	public static String[] getAIFileList()
	{
		File file = new File(HIDDEN_DIR + "/" + AI);
		file.mkdirs();
		return file.list();
	}

	public static File getAIFile(String aiName)
	{
		String path = HIDDEN_DIR + "/" + AI;
		new File(path).mkdirs();
		return new File(path + "/" + aiName);
	}

	protected static String getImagePath(String imageName, boolean isBuiltInFile)
	{
		File file = new File(HIDDEN_DIR + "/" + IMAGES);
		file.mkdirs();
		if (isBuiltInFile)
			return "/" + imageName;
		else
			return HIDDEN_DIR + "/" + IMAGES + "/" + imageName;
	}

	public static String[] getVariantsFileArray()
	{
		File file = new File(HIDDEN_DIR + "/" + VARIANTS);
		file.mkdirs();
		return file.list();
	}

	public static File getVariantsFile(String variantName)
	{
		String path = HIDDEN_DIR + "/" + VARIANTS;
		new File(path).mkdirs();
		return new File(path + "/" + variantName);
	}

	public static String[] getGamesInProgressFileArray()
	{
		File file = new File(HIDDEN_DIR + "/" + GAMES_IN_PROGRESS);
		file.mkdirs();
		return file.list();
	}

	public static File getGamesInProgressFile(String gameFileName)
	{
		String path = HIDDEN_DIR + "/" + GAMES_IN_PROGRESS;
		new File(path).mkdirs();
		return new File(path + "/" + gameFileName);
	}

	public static String[] getCompletedGamesFileArray()
	{
		File file = new File(HIDDEN_DIR + SLASH + COMPLETED_GAMES);
		file.mkdirs();
		return file.list();
	}

	public static File getCompletedGamesFile(String completedGameFileName)
	{
		String path = HIDDEN_DIR + SLASH + COMPLETED_GAMES;
		new File(path).mkdirs();
		try
		{
			FileInputStream fstream = null;
			fstream = new FileInputStream(getPreferencesFile());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			line = br.readLine();
			line = br.readLine();
			path = line.substring(22);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new File(path + SLASH + completedGameFileName);
	}

	public static File getPreferencesFile()
	{
		String path = HIDDEN_DIR;
		new File(path).mkdirs();
		return new File(path + SLASH + PREFERENCES);
	}

	public static String getHiddenDir()
	{
		return HIDDEN_DIR;
	}

	public static String getDefaultCompletedLocation()
	{
		String path = HIDDEN_DIR + SLASH + COMPLETED_GAMES;
		new File(path).mkdirs();
		return path;
	}
	
	public static void overwritePreferencesFileLine(int lineToOverwrite, String overwriteText) throws Exception
	{
		File preferencesFile = FileUtility.getPreferencesFile();
		File tmp = File.createTempFile("tmp", "");

		BufferedReader bufferedReader = new BufferedReader(new FileReader(preferencesFile));
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tmp));

		for (int i = 0; i < lineToOverwrite; i++)
		{
			bufferedWriter.write(bufferedReader.readLine());
			bufferedWriter.newLine();
		}

		bufferedWriter.write(overwriteText);
		bufferedWriter.newLine();
		bufferedReader.readLine();

		String readLine;
		while (null != (readLine = bufferedReader.readLine()))
		{
			bufferedWriter.write(readLine);
			bufferedWriter.newLine();
		}

		bufferedReader.close();
		bufferedWriter.close();

		File oldFile = preferencesFile;
		if (oldFile.delete())
			tmp.renameTo(oldFile);
	}

	public static String readPreferencesFileLine(int lineToRead)
	{
		StringBuilder stringBuilder = new StringBuilder();
		try
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(FileUtility.getPreferencesFile()));
			for (int i = 0; i <= lineToRead; i++)
			{
				stringBuilder.append(bufferedReader.readLine());
			}
			bufferedReader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
	
	public static void createDefaultPreferences(){
		final File preferencesFile = FileUtility.getPreferencesFile();
		try{
			if(!preferencesFile.exists()){
				preferencesFile.createNewFile();
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(preferencesFile, true));
				bufferedWriter.write("DefaultPreferencesSet = true");
				bufferedWriter.newLine();
				bufferedWriter.write("DefaultSaveLocation = " + getDefaultCompletedLocation());
				bufferedWriter.newLine();
				bufferedWriter.write("MoveHighlighting = true");
				bufferedWriter.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	static
	{
		if (System.getProperty("os.name").startsWith("Windows"))
		{
			SLASH = "\\";
			HIDDEN_DIR = System.getProperty("user.home") + "\\chess";
			try
			{
				Runtime rt = Runtime.getRuntime();
				// try to make our folder hidden on Windows
				rt.exec("attrib +H " + System.getProperty("user.home") + "\\chess");
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}
		else
		{
			// if we're not on Windows, just add a period
			HIDDEN_DIR = System.getProperty("user.home") + "/.chess";
			SLASH = "/";
		}
	}

	private static final String HIDDEN_DIR;
	private static final String AI = "AI";
	private static final String IMAGES = "images";
	private static final String VARIANTS = "variants";
	private static final String GAMES_IN_PROGRESS = "gamesInProgress";
	private static final String COMPLETED_GAMES = "completedGames";
	private static final String PREFERENCES = "preferences.txt";
	private static final String SLASH;
	public static final int defaultPreferencesSet = 0;
	public static final int defaultSaveLocation = 1;
	public static final int moveHighlighting = 2;
}
