package week3.challenge;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.RegExp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class RegexChallengeGUI implements ItemListener {
    private JPanel cards; //a panel that uses CardLayout
    private static JTextField initialField; // needed to request focus for first window


    private static class TextNote extends JTextArea {
        private TextNote(String text) {
            super(text);
            setBorder(new EmptyBorder(5,5,5,5));
            setBackground(null);
            setEditable(false);
            setLineWrap(true);
            setWrapStyleWord(true);
            setFocusable(false);
        }
    }
    private static final TextNote output = new TextNote("Sisesta Regex!");

    private void addComponentToPane(Container pane) {
        //Put the JComboBox in a JPanel to get a nicer look.
        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        JComboBox<String> cb = new JComboBox<>(ProblemSpec.getNames());
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(new JLabel("Tase: "));
        comboBoxPane.add(cb);
        cards = new JPanel(new CardLayout());

        for (int i = 0; i < ProblemSpec.DEFS.length; i++) {
            ProblemSpec problemSpec = ProblemSpec.DEFS[i];
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            TextNote text = new TextNote(problemSpec.getDescription());
            JTextField input = new JTextField();
            if (i == 0) initialField = input;

            //text.setHorizontalAlignment(SwingConstants.CENTER);
            card.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    input.requestFocusInWindow();
                }
            });
            input.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = input.getText();
                    output.setText(check(problemSpec.getAutomaton(), text));
                }
            });
            card.add(text);
            card.add(input);
            cards.add(card, problemSpec.getName());
        }

        pane.add(comboBoxPane, BorderLayout.PAGE_START);
        pane.add(cards, BorderLayout.CENTER);
        pane.add(output, BorderLayout.PAGE_END);
    }

    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
        output.setText("");
     }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("AKT Regex Checker");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600, 200));

        RegexChallengeGUI demo = new RegexChallengeGUI();
        demo.addComponentToPane(frame.getContentPane());

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        initialField.requestFocusInWindow();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(RegexChallengeGUI::createAndShowGUI);
    }

    public static String check(Automaton a, String s) {
        try {
            Automaton q = (new RegExp(s, RegExp.NONE)).toAutomaton();
            String s1 = BasicOperations.minus(a, q).getShortestExample(true);
            if (s1 != null) return ("Sinu regex ei tunne ära sõna \"" + eps(s1) + "\".");
            else {
                String s2 = BasicOperations.minus(q, a).getShortestExample(true);
                if (s2 != null) return ("Sinu regex sobitub ka sõnaga \"" + eps(s2) + "\".");
                else return ("Palju õnne! Õige regex!");
            }
        } catch (IllegalArgumentException e) {
            return "Viga (" + e.getMessage() + ")";
        }
    }

    private static String eps(String s) {
        if (s.equals(""))
            return "ε";
        else
            return s;
    }
}

