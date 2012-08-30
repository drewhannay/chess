package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import logic.Piece;
import logic.Square;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
	public AdjustTeamDests(String name)
	{
		m_doMethod = m_doMethods.get(name);
		m_methodName = name;
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
			if (m_doMethod == null)
				m_doMethod = m_doMethods.get(m_methodName);

			m_doMethod.invoke(this, team);
		}
		catch (Exception e)
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
				break;
		}
		if (foundCapture)
		{
			for (int i = 0; i < team.size(); i++)
			{
				Piece current = team.get(i);
				List<Square> adjusted = Lists.newArrayList();
				for (Square s : current.getLegalDests())
				{
					if (s.isOccupied())
						adjusted.add(s);
				}
				current.setLegalDests(adjusted);
			}
		}
	}

	private static final long serialVersionUID = 8968867943548622826L;

	private static Map<String, Method> m_doMethods = Maps.newHashMap();

	static
	{
		try
		{
			m_doMethods.put("classic", AdjustTeamDests.class.getMethod("classicAdjustTeamDests", List.class));
			m_doMethods.put("mustCapture", AdjustTeamDests.class.getMethod("mustCapture", List.class));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private String m_methodName;
	private transient Method m_doMethod;
}
