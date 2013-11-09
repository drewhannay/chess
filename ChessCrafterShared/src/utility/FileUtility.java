
package utility;

import java.awt.image.BufferedImage;
import java.io.File;

import logic.GameBuilder;

public interface FileUtility
{
	public String[] getAIFileList();

	public File getAIFile(String aiName);

	public String getImagePath(String imageName);

	public String[] getVariantsFileArray();

	public String[] getCustomPieceArray();

	public String[] getVariantsFileArrayNoClassic();

	public File getVariantsFile(String variantName);

	public File getPieceFile(String pieceName);

	public String[] getGamesInProgressFileArray();

	public File getGamesInProgressFile(String gameFileName);

	public String[] getCompletedGamesFileArray();

	public File getCompletedGamesFile(String completedGameFileName);

	public File getPreferencesFile();

	public String getHiddenDir();

	public String getDefaultCompletedLocation();

	public BufferedImage getFrontPageImage();

	public void deletePiece(String pieceName);
	
	public void writeGameBuilderFile(GameBuilder builder);

	static final String AI = "AI"; //$NON-NLS-1$
	static final String IMAGES = "images"; //$NON-NLS-1$
	static final String VARIANTS = "variants"; //$NON-NLS-1$
	static final String PIECES = "pieces"; //$NON-NLS-1$
	static final String GAMES_IN_PROGRESS = "gamesInProgress"; //$NON-NLS-1$
	static final String COMPLETED_GAMES = "completedGames"; //$NON-NLS-1$
	static final String PREFERENCES = "preferences"; //$NON-NLS-1$
}
