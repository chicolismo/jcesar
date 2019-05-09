package cesar.panel;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import cesar.display.CharDisplay;

public class DisplayPanel extends JDialog {
    private static final long serialVersionUID = -4447642329893150278L;

    private CharDisplay display;

    public DisplayPanel(JFrame parent, String title) {
        super(parent, title);
        this.display = new CharDisplay();

        JPanel panel = new JPanel(true);
        BevelBorder border = new BevelBorder(BevelBorder.RAISED);
//        Border border = AdvancedBevelBorder.withDefaults();
        panel.setBorder(new CompoundBorder(border, new EmptyBorder(1, 1, 1, 1)));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(this.display);
        setUndecorated(true);
        setContentPane(panel);
        pack();
        setResizable(false);
        initEvents();
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
                DisplayPanel.this.setLocation(newPoint);
            }
        };

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

}
