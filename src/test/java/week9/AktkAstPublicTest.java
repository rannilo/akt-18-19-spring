package week9;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week9.ast.*;

import java.util.Arrays;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AktkAstPublicTest {

	@Test
	public void test01_simpleAstNode() {
		// Kuna seekord me testime tudengite lahendusi näidislahenduse abil,
		// siis teste me avalikuks ei teinud.
		// Moodle'i kaudu saate teste kasutada

		// Kui tahad endale JUnit teste, siis võid kasutada järnevat tehnikat:
		assertEquals(
				new Block(Arrays.asList(new ExpressionStatement(new IntegerLiteral(3)))),
				AktkAst.createAst("3")
		);
	}
}
