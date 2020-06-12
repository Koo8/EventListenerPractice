import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
//w  ww.j av  a2  s .  c  o  m
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.plaf.metal.MetalIconFactory;

public class AbstractActionEx {
    public static void main(String[] a) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Action action = new ShowAction();
        JCheckBox button = new JCheckBox(action);

        frame.add(button, BorderLayout.CENTER);
        frame.setSize(350, 150);
        frame.setVisible(true);
    }
}
//  this action is  parameter for checkbox(check up), so the action works on checkbox
class ShowAction extends AbstractAction {
    // constructor
    public ShowAction() {
       // super("About");

        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(Action.NAME, "Go to number ");
        putValue(Action.LARGE_ICON_KEY, MetalIconFactory.getCheckBoxIcon());

    }

    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("About Swing");
    }
}
