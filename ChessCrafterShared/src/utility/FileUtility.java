package utility;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public final class FileUtility
{
	public static String[] getAIFileList()
	{
		File file = new File(HIDDEN_DIR + SLASH + AI);
		file.mkdirs();
		return file.list();
	}

	public static File getAIFile(String aiName)
	{
		String path = HIDDEN_DIR + SLASH + AI;
		new File(path).mkdirs();
		return new File(path + SLASH + aiName);
	}

	public static String getImagePath(String imageName, boolean isBuiltInFile)
	{
		String imagePath;
		if (isBuiltInFile)
		{
			imagePath = ROOT_RUNNING_DIR+ SLASH + IMAGES + SLASH + imageName;
		}
		else
		{
			File file = new File(HIDDEN_DIR + SLASH + IMAGES);
			file.mkdirs();
			imagePath = HIDDEN_DIR + SLASH + IMAGES + SLASH + imageName;
		}
		return imagePath;
	}

	public static String[] getVariantsFileArray()
	{
		File file = new File(HIDDEN_DIR + SLASH + VARIANTS);
		file.mkdirs();
		return file.list();
	}
	
	public static String[] getCustomPieceArray()
	{
		File file = new File(HIDDEN_DIR + SLASH + PIECES);
		file.mkdirs();
		return file.list();
	}
	
	public static String[] getVariantsFileArrayNoClassic()
	{
		String[] variants = getVariantsFileArray();
		for (int i = 0; i < variants.length; i++)
		{
			if (variants[i].contentEquals(Messages.getString("classic"))) //$NON-NLS-1$
			{
				variants[i] = variants[variants.length - 1];
				variants[variants.length - 1] = null;
				break;
			}
		}
		return variants;
	}

	public static File getVariantsFile(String variantName)
	{
		return new File(HIDDEN_DIR + SLASH + VARIANTS + SLASH + variantName);
	}
	
	public static File getPieceFile(String pieceName)
	{
		return new File(HIDDEN_DIR + SLASH + PIECES + SLASH + pieceName);
	}

	public static String[] getGamesInProgressFileArray()
	{
		File file = new File(HIDDEN_DIR + SLASH + GAMES_IN_PROGRESS);
		file.mkdirs();
		return file.list();
	}

	public static File getGamesInProgressFile(String gameFileName)
	{
		String path = HIDDEN_DIR + SLASH + GAMES_IN_PROGRESS;
		new File(path).mkdirs();
		return new File(path + SLASH + gameFileName);
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
			DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getPreferencesFile()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(dataInputStream));
			String line;
			line = reader.readLine();
			line = reader.readLine();
			path = line.substring(22);
			reader.close();
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

	static
	{
		if (System.getProperty("os.name").startsWith("Windows")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			HIDDEN_DIR = System.getProperty("user.home") + "\\chess"; //$NON-NLS-1$ //$NON-NLS-2$
			SLASH = "\\"; //$NON-NLS-1$
			
			String userDir = System.getProperty("user.dir"); //$NON-NLS-1$
			
			ROOT_RUNNING_DIR = userDir.substring(0, userDir.lastIndexOf("\\"))+"\\ChessCrafterShared"; //$NON-NLS-1$ //$NON-NLS-2$
			try
			{
				Runtime rt = Runtime.getRuntime();
				// try to make our folder hidden on Windows
				rt.exec("attrib +H " + System.getProperty("user.home") + "\\chess"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}
		else
		{
			// if we're not on Windows, just add a period
			HIDDEN_DIR = System.getProperty("user.home") + "/.chess"; //$NON-NLS-1$ //$NON-NLS-2$
			ROOT_RUNNING_DIR = System.getProperty("user.dir"); //$NON-NLS-1$
			SLASH = "/"; //$NON-NLS-1$
		}
	}

	public static BufferedImage getFrontPageImage()
	{
		BufferedImage frontPage = null;
		try
		{
			frontPage = ImageIO.read(new File(getImagePath("chess_logo.png", true))); //$NON-NLS-1$
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return frontPage;
	}
	
	public static void deletePiece(String pieceName)
	{
		File pieceFile = getPieceFile(pieceName);
		pieceFile.delete();
		new File((getImagePath("l_"+pieceName+".png", false))).delete(); //$NON-NLS-1$ //$NON-NLS-2$
		new File((getImagePath("d_"+pieceName+".png", false))).delete(); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	private static final String HIDDEN_DIR;
	private static final String ROOT_RUNNING_DIR;
	private static final String AI = "AI"; //$NON-NLS-1$
	private static final String IMAGES = "images"; //$NON-NLS-1$
	private static final String VARIANTS = "variants"; //$NON-NLS-1$
	private static final String PIECES = "pieces"; //$NON-NLS-1$
	private static final String GAMES_IN_PROGRESS = "gamesInProgress"; //$NON-NLS-1$
	private static final String COMPLETED_GAMES = "completedGames"; //$NON-NLS-1$
	private static final String PREFERENCES = "preferences.txt"; //$NON-NLS-1$
	private static final String SLASH;
}
