package utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import logic.PieceBuilder;

import com.google.common.collect.Lists;

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

	public static String getImagePath(String imageName)
	{
		File file = new File(HIDDEN_DIR + SLASH + IMAGES);
		file.mkdirs();
		String imagePath = HIDDEN_DIR + SLASH + IMAGES + SLASH + imageName;
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

	public static List<String> getVariantsFileArrayNoClassic()
	{
		List<String> varList = Lists.newArrayList(getVariantsFileArray());
		varList.remove(Messages.getString("classic")); //$NON-NLS-1$
		return varList;
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
			FileInputStream fileInputStream = new FileInputStream(getPreferencesFile());
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			Preference preference = (Preference) objectInputStream.readObject();
			path = preference.getSaveLocation();
			objectInputStream.close();
			fileInputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new File(path + SLASH + completedGameFileName);
	}

	public static File getPreferencesFile()
	{
		new File(HIDDEN_DIR).mkdirs();
		return new File(HIDDEN_DIR + SLASH + PREFERENCES);
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
			SLASH = "/"; //$NON-NLS-1$
		}
	}

	public static BufferedImage getFrontPageImage()
	{
		BufferedImage frontPage = null;
		String path = null;
		try
		{
			URL resource = FileUtility.class.getResource("/chess_logo.png"); //$NON-NLS-1$
			frontPage = ImageIO.read(resource);
		}
		catch (IOException e)
		{
			System.out.println(Messages.getString("cantFindPath") + path); //$NON-NLS-1$
			e.printStackTrace();
		}
		return frontPage;
	}

	public static void deletePiece(String pieceName)
	{
		File pieceFile = getPieceFile(pieceName);
		pieceFile.delete();
		PieceBuilder.removePieceType(pieceName);
		new File((getImagePath("l_" + pieceName + ".png"))).delete(); //$NON-NLS-1$ //$NON-NLS-2$
		new File((getImagePath("d_" + pieceName + ".png"))).delete(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static void deleteVariant(String name)
	{
		getVariantsFile(name).delete();
	}
	
	private static final String HIDDEN_DIR;
	private static final String AI = "AI"; //$NON-NLS-1$
	private static final String IMAGES = "images"; //$NON-NLS-1$
	private static final String VARIANTS = "variants"; //$NON-NLS-1$
	private static final String PIECES = "pieces"; //$NON-NLS-1$
	private static final String GAMES_IN_PROGRESS = "gamesInProgress"; //$NON-NLS-1$
	private static final String COMPLETED_GAMES = "completedGames"; //$NON-NLS-1$
	private static final String PREFERENCES = "preferences"; //$NON-NLS-1$

	private static final String SLASH;
}
