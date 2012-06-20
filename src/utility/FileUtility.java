package utility;

import java.io.File;

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
		return new File(path + "/" + completedGameFileName);
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
}
