package utility;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
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
		File file = new File(HIDDEN_DIR + "/" + COMPLETED_GAMES);
		file.mkdirs();
		return file.list();
	}

	public static File getCompletedGamesFile(String completedGameFileName)
	{
		String path = HIDDEN_DIR + "/" + COMPLETED_GAMES;
		new File(path).mkdirs();
		try{
			FileInputStream fstream = null;
			fstream = new FileInputStream(getPreferencesFile());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			line = br.readLine();
			line = br.readLine();
			path = line.substring(22);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new File(path + "/" + completedGameFileName);
	}
	public static File getPreferencesFile()
	{
		String path = HIDDEN_DIR;
		new File(path).mkdirs();
		return new File(path + "/" + PREFERENCES);
	}
	public static String getHiddenDir(){
		return HIDDEN_DIR;
	}
	
	public static String getDefaultCompletedLocation(){
		return HIDDEN_DIR + "/" + COMPLETED_GAMES;
	}

	static
	{
		if (System.getProperty("os.name").startsWith("Windows"))
		{
			HIDDEN_DIR = System.getProperty("user.home") + "/chess";
			try
			{
				Runtime rt = Runtime.getRuntime();
				// try to make our folder hidden on Windows
				rt.exec("attrib +H " + System.getProperty("user.home") + "/chess");
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
		}
	}

	private static final String HIDDEN_DIR;
	private static final String AI = "AI";
	private static final String VARIANTS = "variants";
	private static final String GAMES_IN_PROGRESS = "gamesInProgress";
	private static final String COMPLETED_GAMES = "completedGames";
	private static final String PREFERENCES = "preferences.txt";
}
