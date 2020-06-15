import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

public class FlipTheCoinProbability extends JFrame implements ActionListener {

    // Fields
    private JTextField headText, totalText, devText;
    private JButton startBtn, stopBtn;
    private GridBagConstraints constrains;
    private FlipTask flipTask;

    // constructor
    FlipTheCoinProbability() {
        super("Flip Coin Deviation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(30, 20, 40, 20));
        //
        headText = buildTextField();
        totalText = buildTextField();
        devText = buildTextField();
        // define constrains
        getContentPane().setLayout(new GridBagLayout());
        constrains = new GridBagConstraints();
        constrains.insets = new Insets(5, 10, 5, 10);
        //
        startBtn = makeButton("Start");
        stopBtn = makeButton("Stop");
        stopBtn.setEnabled(false);

        // Display the window
        pack();
        setVisible(true);
    }

    // define textField
    private JTextField buildTextField() {
        JTextField t = new JTextField(20);
        t.setEditable(false);
        t.setHorizontalAlignment(JTextField.RIGHT);
        t.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        // add to JFrame
        getContentPane().add(t, constrains);
        return t;
    }

    // define JButton
    private JButton makeButton(String cap) {
        JButton btn = new JButton(cap);
        getContentPane().add(btn, constrains);
        // to control button use actionCommand later
        btn.setActionCommand(cap);
        btn.addActionListener(this);
        return btn;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getActionCommand() == "Start") { // when click start btn
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            // do the task - publish constantly updated number to textfields
            // calulate dev and publish
            // this increment number and calculte dev can be done in background thread using SWingWorker
            // highlight: changed SwingWorker from annonymous class to private inner class, so that i can get a new "flipTasK" each time when come to restart,
            // otherwise, fliptask is in "isCancelled" state and can't restart the app.
            flipTask = new FlipTask(); // a new fliptast is instantiated each time to "start", so that the program can restart when click start button.

            flipTask.execute();

        }
        if (e.getActionCommand() == "Stop") {
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            flipTask.cancel(true);

            //todo : flipTask = null;
            // todo: can't restart the program
            System.out.println(flipTask.isCancelled());
        }
    }

    private class FlipTask extends SwingWorker<Void, FlipPair> {


        @Override
        protected Void doInBackground() throws Exception {
            long total = 0;
            long head = 0;
            System.out.println(flipTask.isCancelled() + " in doINBackground "); // SwingWorker is {!isCannelled) at the start stage.
            while (!isCancelled()) { // this is implemented from Future by SwingWorker
                total++; // flip coins constantly
                if ((new Random()).nextBoolean()) { // when head is flipped
                    head++;
                }
                FlipPair fp = new FlipPair(head, total);
                // record total and head a the same time needs a class to record this - FlipPair
                publish(fp);
            }
            return null;
        }

        @Override
        protected void process(List chunks) {
            // display the last flipPair passed to process
            FlipPair thePair = (FlipPair) chunks.get(chunks.size() - 1);
            headText.setText(String.valueOf(thePair.head));
            totalText.setText(String.valueOf(thePair.total));
            devText.setText(String.format("%.10g", // 10 digit float number
                    ((double) thePair.head) / ((double) thePair.total) - 0.5));

        }


    }


    private class FlipPair {
        long head;
        long total;

        public FlipPair(long head, long total) {
            this.head = head;
            this.total = total;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FlipTheCoinProbability();
            }
        });

        // or
        //new FlipTheCoinProbability();


    }


}
