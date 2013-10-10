package ai;

import java.util.List;

import utility.FileUtility;

import com.google.common.collect.Lists;


public final class AIManager
{
	private AIManager()
	{
	}

	public static AIManager getInstance()
	{
		if (sInstance == null)
			sInstance = new AIManager();

		return sInstance;
	}

	public String[] getAIFiles()
	{
		String[] allFiles = FileUtility.getAIFileList();
		allFiles = FileUtility.getAIFileList();

		List<String> tempFiles = Lists.newArrayList();

		for (String fileName : allFiles)
		{
			if (fileName.endsWith(".java")) //$NON-NLS-1$
				tempFiles.add(fileName);
		}

		return tempFiles.toArray(new String[tempFiles.size()]);
	}

	private static AIManager sInstance;
}
