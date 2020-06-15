import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

public class Flipper extends JFrame
        implements ActionListener {
    private final GridBagConstraints constraints;
    private final JTextField headsText, totalText, devText;
    private final Border border =
            BorderFactory.createLoweredBevelBorder();
    private final JButton startButton, stopButton;
    private FlipTask flipTask; // a SwingWorker Thread

    // Utility method
    private JTextField makeText() {
        JTextField t = new JTextField(20);
        t.setEditable(false);
        t.setHorizontalAlignment(JTextField.RIGHT);
        t.setBorder(border);
        getContentPane().add(t, constraints);
        return t;
    }

    // Utility Method
    private JButton makeButton(String caption) {
        JButton b = new JButton(caption);
        b.setActionCommand(caption);
        b.addActionListener(this);
        getContentPane().add(b, constraints);
        return b;
    }

    public Flipper() {
        super("Flipper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 40, 10));
        // setSize not working
        // setSize(new Dimension(1900, 1300));

        //Make text boxes
        getContentPane().setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        // insets set the bound distance between each component
        constraints.insets = new Insets(3, 30, 3, 15);
        headsText = makeText();
        totalText = makeText();
        devText = makeText();

        //Make buttons
        startButton = makeButton("Start");
        stopButton = makeButton("Stop");
        stopButton.setEnabled(false);

        //Display the window.
        pack();
        setVisible(true);
    }

    // inner class
    private static class FlipPair {
        private final long heads, total;

        FlipPair(long heads, long total) {
            this.heads = heads;
            this.total = total;
        }
    }

    //Inner Class
    private class FlipTask extends SwingWorker<Void, FlipPair> {
        @Override
        protected Void doInBackground() throws InterruptedException {
            long heads = 0;
            long total = 0;
            Random random = new Random();
            // Returns true if this task was cancelled before it completed normally.
            while (!isCancelled()) { // SwingWorker implements Future interface, which has this boolean
               // Thread.sleep(300);
                total++;
                if (random.nextBoolean()) {
                    heads++;
                }
                publish(new FlipPair(heads, total));
            }
            return null;
        }

        @Override
        protected void process(List<FlipPair> pairs) {
            FlipPair pair = pairs.get(pairs.size() - 1);
            //System.out.println(pairs.size());
            headsText.setText(String.format("%d", pair.heads));
            totalText.setText(String.format("%d", pair.total));
            devText.setText(String.format("%.10g", // 10 digit float number
                    ((double) pair.heads) / ((double) pair.total) - 0.5));
        }
    }


    public void actionPerformed(ActionEvent e) {
        if ("Start" == e.getActionCommand()) {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            (flipTask = new FlipTask()).execute();
        } else if ("Stop" == e.getActionCommand()) {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            flipTask.cancel(true);
            flipTask = null;
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Flipper();
            }
        });


    }
}
