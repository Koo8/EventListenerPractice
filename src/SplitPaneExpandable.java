import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//  ww  w  .  j ava 2  s.co  m
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class SplitPaneExpandable {
    public static void main(String args[]) {
        JFrame vFrame = new JFrame();
        vFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent leftButton = new JButton("Left");
        JComponent rightButton = new JButton("Right");
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setOneTouchExpandable(true);
        splitPane.setLeftComponent(leftButton);
        splitPane.setRightComponent(rightButton);
        ActionListener oneActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                splitPane.resetToPreferredSizes();
            }
        };
        ((JButton) rightButton).addActionListener(oneActionListener);
        ActionListener anotherActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                splitPane.setDividerLocation(10);
                splitPane.setContinuousLayout(true);
            }
        };
        ((JButton) leftButton).addActionListener(anotherActionListener);
        vFrame.getContentPane().add(splitPane, BorderLayout.CENTER);
        vFrame.setSize(300, 150);
        vFrame.setVisible(true);

    }
}