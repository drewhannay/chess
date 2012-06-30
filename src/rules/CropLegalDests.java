package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import logic.Piece;

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
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -2048642921268915519L;

	/**
	 * The names of the methods to do. Used to read back in during
	 * Serialization.
	 */
	private ArrayList<String> names = new ArrayList<String>();
	/**
	 * The methods to perform, since there can be more than one compatable
	 * method.
	 */
	private transient ArrayList<Method> methods = new ArrayList<Method>();
	/**
	 * A hashmap to conveniently look up methods.
	 */
	private static HashMap<String, Method> doMethods = new HashMap<String, Method>();
	static
	{
		try
		{
			doMethods.put("classic", CropLegalDests.class.getMethod("classicCropLegalDests", Piece.class, Piece.class, List.class));
			doMethods.put("stationaryObjective",
					CropLegalDests.class.getMethod("stationaryObjective", Piece.class, Piece.class, List.class));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Add a method to the list of performable methods.
	 * 
	 * @param name The name of the method to add.
	 */
	public void addMethod(String name)
	{
		methods.add(doMethods.get(name));
		names.add(name);
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
			if (methods == null || methods.size() == 0)
			{
				methods = new ArrayList<Method>();
				for (String s : names)
				{
					methods.add(doMethods.get(s));
				}
			}
			for (Method m : methods)
			{
				m.invoke(this, movingObjectivePiece, toAdjust, enemyTeam);
			}
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
		{
			toAdjust.getLegalDests().clear();
		}
		else
		{
			toAdjust.adjustPinsLegalDests(movingObjective, enemyTeam);
		}
		return;
	}

}
