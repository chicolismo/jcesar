package cesar.panel;

import cesar.display.CharDisplay;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DisplayPanel extends JDialog {
    private static final long serialVersionUID = -4447642329893150278L;

    private CharDisplay display;

    public DisplayPanel(JFrame parent, String title) {
        super(parent, title);
        display = new CharDisplay();

        JPanel panel = new JPanel(true);
        this.setContentPane(panel);
        this.setUndecorated(true);
        panel.add(display);
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));
        initEvents();
        pack();
    }

    public void setValue(String string) {
        display.setValue(string);
        display.repaint();
    }

    public void setValue(byte[] value) {
        int size = Math.min(value.length, display.length());
        for (int i = 0; i < size; ++i) {
            display.setValueAt(i, (char) value[i]);
        }
    }

    public void setValueAt(int index, char value) {
        // TODO: Garantir que "index" está dentro do intervalo!!
        display.setValueAt(index, value);
    }

    private void initEvents() {
        var panel = this;

        MouseAdapter mouseAdapter = new MouseAdapter() {
            Point clickPoint = null;

            @Override
            public void mousePressed(MouseEvent event) {
                clickPoint = event.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                Point newPoint = event.getLocationOnScreen();
                newPoint.translate(-clickPoint.x, -clickPoint.y);
                ;
                panel.setLocation(newPoint);
            }
        };

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }
}
