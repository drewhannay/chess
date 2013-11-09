
package gson;

import junit.framework.Assert;
import logic.GameBuilder;
import models.Game;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utility.GsonUtility;
import com.google.gson.Gson;

public class GsonTest
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public final void test()
	{
		Gson gson = GsonUtility.getGson();

		Game game = GameBuilder.buildClassic();
		Assert.assertTrue(game != null);

		String json = gson.toJson(game);
		Assert.assertTrue(json != null);

		Game parsedGame = gson.fromJson(json, Game.class);
		Assert.assertEquals(game, parsedGame);
	}
}
