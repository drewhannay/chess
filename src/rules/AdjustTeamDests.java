package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import logic.Piece;
import logic.Square;

/**
 * AdjustTeamDests.java
 * 
 * Class to hold methods affecting the legal destinations for an entire team of
 * pieces.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class AdjustTeamDests implements Serializable
{

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 8968867943548622826L;

	/**
	 * The name of the method used; for reading it back in.
	 */
	private String name;
	/**
	 * Which method to use; reflection so we didn't write nxm classes.
	 */
	private transient Method doMethod;
	/**
	 * To conveniently look up the methods.
	 */
	private static HashMap<String, Method> doMethods = new HashMap<String, Method>();
	static
	{
		try
		{
			doMethods.put("classic", AdjustTeamDests.class.getMethod(
					"classicAdjustTeamDests", List.class));
			doMethods.put("mustCapture",
					AdjustTeamDests.class.getMethod("mustCapture", List.class));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the AdjustTeamDests object.
	 * 
	 * @param name The method to use.
	 */
	public AdjustTeamDests(String name)
	{
		doMethod = doMethods.get(name);
		this.name = name;
	}

	/**
	 * Nothing to be done after the move in classic chess.
	 * 
	 * @param team The team to adjust.
	 */
	public void classicAdjustTeamDests(List<Piece> team)
	{

	}

	/**
	 * Perform the method; using reflection to simulate polymorphic method
	 * invocation.
	 * 
	 * @param team The pieces on the current team.
	 */
	public void execute(List<Piece> team)
	{
		try
		{
			if (doMethod == null)
			{
				doMethod = doMethods.get(name);
			}
			doMethod.invoke(this, team);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Adjust the available destinations if the player has chosen to play a
	 * variant where captures are mandatory.
	 * 
	 * @param team The team to adjust.
	 */
	public void mustCapture(List<Piece> team)
	{
		boolean foundCapture = false;
		for (int i = 0; i < team.size(); i++)
		{
			Piece current = team.get(i);
			for (Square s : current.getLegalDests())
			{
				if (s.isOccupied())
				{
					foundCapture = true;
					break;
				}
			}
			if (foundCapture)
			{
				break;
			}
		}
		if (foundCapture)
		{
			for (int i = 0; i < team.size(); i++)
			{
				Piece current = team.get(i);
				ArrayList<Square> adjusted = new ArrayList<Square>();
				for (Square s : current.getLegalDests())
				{
					if (s.isOccupied())
					{
						adjusted.add(s);
					}
				}
				current.setLegalDests(adjusted);
			}
		}
	}

}
