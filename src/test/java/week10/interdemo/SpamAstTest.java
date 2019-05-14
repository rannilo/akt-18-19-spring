package week10.interdemo;

import org.junit.Test;
import week10.interdemo.spammerast.MailCmd;
import week10.interdemo.spammerast.RelayCommands;
import week10.interdemo.spammerast.SmsCmd;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static week10.interdemo.SpamAst.*;

public class SpamAstTest {

    @Test
    public void testAst() {
        List<RelayCommands> ast = createAst(exampleInput);
        RelayCommands cmd0 = new MailCmd("vesal.vojdani@spammail.com", "Vesal Vojdani", MSG);
        RelayCommands cmd1 = new SmsCmd(3716666666L, "Varmo Vene", MSG);
        assertEquals(cmd0, ast.get(0));
        assertEquals(cmd1, ast.get(1));
    }
}