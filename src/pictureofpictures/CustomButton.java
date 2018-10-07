package pictureofpictures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

public class CustomButton 
    extends JButton {
    
    private final Color MOUSE_NOT_OVER = ProjConsts.DARK_GREY;
    private final Color MOUSE_OVER = ProjConsts.MEDIUM_GREY;
    private final Color MOUSE_DOWN = ProjConsts.LIGHT_GREY;
    
    public CustomButton(String text) {
        super(text);
        
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        this.setPreferredSize(new Dimension(Integer.MAX_VALUE, 25));
        this.setMinimumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        this.setFont(ProjConsts.NORM_FONT.deriveFont(15.0f));
        this.setForeground(ProjConsts.TEXT_COLOR);
        this.setBackground(MOUSE_NOT_OVER);
        this.setBorderPainted(false);
        this.setFocusable(false);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) {
                //setBackground(MOUSE_DOWN);
            }
            @Override public void mousePressed(MouseEvent e) {
                setBackground(MOUSE_DOWN);
                repaint();
            }
            @Override public void mouseReleased(MouseEvent e) {
                setBackground(MOUSE_OVER);
                repaint();
            }
            @Override public void mouseEntered(MouseEvent e) {
                setBackground(MOUSE_OVER);
                repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                setBackground(MOUSE_NOT_OVER);
                repaint();
            }
        });
    }
    
    @Override public void paint(Graphics g) {
        if (this.isEnabled() ) {
            g.setColor(this.getBackground());
            this.setForeground(ProjConsts.TEXT_COLOR);
        } else {
            g.setColor(ProjConsts.BACKGROUND_COLOR);
            this.setForeground(ProjConsts.DISABLED_TEXT_COLOR);
        }
        
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        super.paint(g);
    }
    
}
