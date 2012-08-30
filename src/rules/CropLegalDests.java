package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import logic.Piece;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * CropLegalDests.java
 * 
 * Class to hold methods with various rules for cropping the legal destinations
 * of a piece.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class CropLegalDests implements Serializable
{
	/**
	 * Add a method to the list of performable methods.
	 * 
	 * @param name The name of the method to add.
	 */
	public void addMethod(String name)
	{
		m_methods.add(m_doMethods.get(name));
		m_methodNames.add(name);
	}

	/**
	 * In classic chess, don't let the piece move so the king is in check.
	 * 
	 * @param movingObjective The moving objective Piece
	 * @param toAdjust The piece to adjust the dests of.
	 * @param enemyTeam The enemy team.
	 */
	public void classicCropLegalDests(Piece movingObjective, Piece toAdjust, List<Piece> enemyTeam)
	{
		toAdjust.adjustPinsLegalDests(movingObjective, enemyTeam);
	}

	/**
	 * Execute all appropriate methods
	 * 
	 * @param movingObjectivePiece The moving objective piece
	 * @param toAdjust The piece to adjust the legal destinations.
	 * @param enemyTeam The opposite team.
	 */
	public void execute(Piece movingObjectivePiece, Piece toAdjust, List<Piece> enemyTeam)
	{
		try
		{
			if (m_methods == null || m_methods.size() == 0)
			{
				m_methods = Lists.newArrayList();
				for (String methodName : m_methodNames)
					m_methods.add(m_doMethods.get(methodName));
			}
			for (Method method : m_methods)
				method.invoke(this, movingObjectivePiece, toAdjust, enemyTeam);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Don't let the objective move; don't let other pieces move so the
	 * objective is in check.
	 * 
	 * @param movingObjective The moving objective Piece.
	 * @param toAdjust The piece to adjust.
	 * @param enemyTeam The enemy team.
	 */
	public void stationaryObjective(Piece movingObjective, Piece toAdjust, List<Piece> enemyTeam)
	{
		if (toAdjust == movingObjective)
			toAdjust.getLegalDests().clear();
		else
			toAdjust.adjustPinsLegalDests(movingObjective, enemyTeam);
		return;
	}

	private static final long serialVersionUID = -2048642921268915519L;

	private static Map<String, Method> m_doMethods = Maps.newHashMap();

	static
	{
		try
		{
			m_doMethods.put("classic", CropLegalDests.class.getMethod("classicCropLegalDests", Piece.class, Piece.class, List.class));
			m_doMethods.put("stationaryObjective",
					CropLegalDests.class.getMethod("stationaryObjective", Piece.class, Piece.class, List.class));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private List<String> m_methodNames = Lists.newArrayList();
	private transient List<Method> m_methods = Lists.newArrayList();
}
