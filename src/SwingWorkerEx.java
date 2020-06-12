import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SwingWorkerEx {
    private JFrame frame;
    private JLabel statusLabel;
    private JButton button;

    //constructor
    public SwingWorkerEx() {
        frame = new JFrame("SwingWorker Example");
        statusLabel = new JLabel("Not Completed", JLabel.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.BLUE);
        frame.setLayout(new GridLayout(2,0));
        frame.add(statusLabel);

        button = new JButton("start counter");
       // button.setPreferredSize(new Dimension(55,5));
        frame.add(button);
        frame.setVisible(true);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startThread();
            }
        });
    }

    public void startThread() {
        SwingWorker sw = new SwingWorker() {
            @Override
            protected String doInBackground() throws Exception {
                for (int i = 10; i >0 ; i--) {
                    Thread.sleep(100);
                    publish(i);
                }
                String res = "Count down completed.";  // this can only be achieved through done().
                return res;
            }

            @Override
            protected void process(List chunks) {
                for (Object i: chunks) {
                    statusLabel.setText(String.valueOf(i));
                }
            }

            @Override
            protected void done() {
                try {
                    String res = (String) get();
                    statusLabel.setText(res);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        sw.execute();
    }
    public static void main(String[] args) {
        new SwingWorkerEx();

    }
}
