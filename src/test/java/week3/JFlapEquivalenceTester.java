package week3;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.StatePair;
import dk.brics.automaton.Transition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utils.AlphanumComparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class JFlapEquivalenceTester {
	public static String lastTestDescription = "";
    static Path PATH = Paths.get(System.getProperty("user.dir"), "src", "test", "jflap");
	
	private File benchmarkFile;

	public JFlapEquivalenceTester(Path benchmarkPath) {
		this.benchmarkFile = PATH.resolve(benchmarkPath).toFile();
	}

	@Parameters(name="{0}")
	public static Collection<Object[]> data() throws IOException {
        Comparator<Object> comp = (p1, p2) -> new AlphanumComparator().compare(p1.toString(), p2.toString());
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**.jff");
		return Files.walk(PATH).sorted(comp)
                .filter(pathMatcher::matches)
                .map(p -> new Object[]{PATH.relativize(p)})
                .collect(Collectors.toList());
	}

	private static Automaton fromJFlap(File file) {
		Automaton automaton = new Automaton();
		Map<Integer, State> states = new HashMap<>();
		Set<StatePair> epsilons = new HashSet<>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);

			NodeList stateNodes = doc.getElementsByTagName("state");
			for (int i=0; i < stateNodes.getLength(); i++) {
				Element stateNode = (Element)stateNodes.item(i);
				int stateId = Integer.parseInt(stateNode.getAttribute("id"));

				State state = new State();
				state.setAccept(stateNode.getElementsByTagName("final").getLength() == 1);

				if (stateNode.getElementsByTagName("initial").getLength() == 1) {
					automaton.setInitialState(state);
				}

				states.put(stateId, state);
			}

			NodeList transitionNodes = doc.getElementsByTagName("transition");
			for (int i=0; i < transitionNodes.getLength(); i++) {
				Element transitionNode = (Element)transitionNodes.item(i);

				int fromId = Integer.parseInt(getXmlChildContent(transitionNode, "from"));
				int toId = Integer.parseInt(getXmlChildContent(transitionNode, "to"));
				State fromState = states.get(fromId);
				State toState = states.get(toId);

				String label = getXmlChildContent(transitionNode, "read");

				if (label.length() == 0) {
					epsilons.add(new StatePair(fromState, toState));
				}
				else if (label.length() == 1) {
					fromState.addTransition(new Transition(label.charAt(0), toState));
				}
				else {
					throw new IllegalArgumentException("Multichar transition label");
				}
			}

			automaton.addEpsilons(epsilons);
			automaton.restoreInvariant();
			automaton.setDeterministic(false);
			return automaton;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String getXmlChildContent(Element element, String tagName) {
		NodeList children = element.getElementsByTagName(tagName);
		if (children.getLength() != 1) {
			throw new IllegalArgumentException("Expected single child with given name");
		}

		return children.item(0).getTextContent();
	}

	@Test
	public void test() {
		Automaton benchAutomaton = fromJFlap(this.benchmarkFile);
        String fileName = benchmarkFile.getName().replace("bench", "yl");
        String fileDir = benchmarkFile.getParentFile().getName();

        File testableFile = Paths.get(System.getProperty("user.dir"), "src", "main", "jflap", fileDir, fileName).toFile();


        lastTestDescription = Paths.get(fileDir, testableFile.getName()).toString();
				
				
		if (testableFile.exists()) {
			Automaton testableAutomaton = fromJFlap(testableFile);
			if (!testableAutomaton.equals(benchAutomaton)) {
				Automaton diff = benchAutomaton.minus(testableAutomaton);
				
				if (diff.isEmpty()) {
					diff = testableAutomaton.minus(benchAutomaton);
				}
				
				fail(String.format("Esitatud automaat (%s) ei anna õiget vastust sisendiga '%s'",
                        lastTestDescription, diff.getShortestExample(true)));
			}
		} else {
			fail("Ei leia lahenduse faili");
		}
	}

}