package week3;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FormatMachineTest {

	private static String cleanUp(String s) {
        StringBuilder sb = new StringBuilder();
        FormatMachine machine = new FormatMachine();
        for (char c : s.toCharArray()) sb.append(machine.process(c));
        return sb.toString();
    }

	@Test
	public void testCleanUp01() throws Exception {
		assertEquals("One Two Three Four",
				cleanUp("One Two  Three   Four"));
		assertEquals("One Two Three Four",
				cleanUp("   One Two  Three   Four"));
	}

	@Test
	public void testCleanUp02() throws Exception {
		assertEquals("Tere, Maailm!",
				cleanUp("Tere , Maailm  !"));
		assertEquals("Tere, Maailm!", cleanUp("Tere   ,Maailm!"));
	}

	@Test
	public void testCleanUp03() throws Exception {
		assertEquals("Testing... Did it work? Do you clean it up?!",
				cleanUp("Testing . . .   Did it work ? Do you clean it up ? !"));
		assertEquals("f (x, y)",
				cleanUp("f(x,y)"));
		assertEquals("Hello\n(what?)",
				cleanUp("Hello\n  (what ? )"));
	}

	@Test
	public void testCleanUp04() throws Exception {
		assertEquals("Muide (ei tea, kas ikka) nii on.",
				cleanUp("Muide( ei tea , kas ikka )nii on ."));
		
		assertEquals("Muide (ja (kes) ei tea, kas ikka) nii on.",
				cleanUp("Muide(ja( kes ) ei tea , kas ikka )nii on ."));
	}

	@Test
	public void testCleanUp05() throws Exception {
		assertEquals("This text (all of it) has occasional lapses... in\n" +
				"punctuation (sometimes, pretty bad; sometimes, not so).\n" +
				"\n" +
				"(Ha!) Is this: fun!?! Or what?",
				cleanUp("This     text (all of it   )has occasional lapses .. .in\n" +
						"  punctuation( sometimes,pretty bad ; sometimes ,not so).\n" +
						"\n" +
						"( Ha ! )Is this  :fun ! ? !  Or   what  ?"));
	}

}
