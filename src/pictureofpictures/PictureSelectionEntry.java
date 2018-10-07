package pictureofpictures;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class PictureSelectionEntry 
    extends JPanel {
    
    private PictureSelectionEntry this2 = this;
    
    private final int MAX_HEIGHT = 130;
    private final int MIN_HEIGHT = 100;
    
    private final Color SELECTED_COLOR = new Color( 11,96,193);
    private final Color UNSELECTED_COLOR = new Color(70,70,70);
    //private final Color UNUSED_COLOR = new Color( 50, 20, 20); //just make things darker
    //private final Color UNUSED_AND_SELECTED_COLOR = new Color(  4, 39, 67);
    
    //private JCheckBox selectedButton;
    boolean selected; //no checkbox needed
    private CenteredImagePanel picturePanel;
    protected SpecialPicture picture;
    private JPanel rightPanel;
        private JTextArea textArea;
        private JCheckBox disableButton;
    
    private String pictureName;
    
    private MainFrame2 master;
    
    private int repaintCount = 0;
    
    public PictureSelectionEntry(SpecialPicture picture, MainFrame2 master) {
        super();
        
        this.master = master;
        
        this.pictureName = picture.name;
        
        this.picture = picture;
        this.makePanel();
    }
    
    private void makePanel() {
        
        //this.setBorder(new LineBorder(Color.BLACK));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {
                
                if (master.km.shiftKeyDown == true) {
                    master.sm.shiftSelect(this2);
                } else {
                    if ( !disableButton.isSelected() ) //if not disabled
                            selected = !selected;
                    repaint();      //repaint only this
                }
                
                master.sm.lastSelectedIndex = master.sm.indexOf(this2);     //last selected entry
                
                master.selectionChanged();
            }
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
            
        });
        //this.setOpaque(true);
        this.setBackground(this.UNSELECTED_COLOR);
        
        
        //picture panel
        this.picturePanel = new CenteredImagePanel(picture.icon) {
            @Override public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                super.paint(g);
            }
        };
        //this.picturePanel.setBorder(new LineBorder(Color.RED));
        this.picturePanel.setOpaque(false);
        this.add(this.picturePanel);
        
        //right panel
        this.rightPanel = new JPanel();
        this.rightPanel.setLayout(new BorderLayout());
        this.rightPanel.setMaximumSize(new Dimension(50, Integer.MAX_VALUE));
        this.rightPanel.setOpaque(false);
        this.add(this.rightPanel);
            //text arra
            this.textArea = new JTextArea();
            this.textArea.setEditable(false);
            this.textArea.setLineWrap(true);
            this.textArea.setWrapStyleWord(true);
            this.textArea.setRows(5);
            this.textArea.setHighlighter(null); //makes non-selectable
            this.textArea.addMouseListener(new MouseListener() {
                @Override public void mouseClicked(MouseEvent e) {}
                @Override public void mousePressed(MouseEvent e) {
                    if ( !disableButton.isSelected() )
                        selected = !selected;
                    repaint();
                    master.selectionChanged();
                }
                @Override public void mouseReleased(MouseEvent e) {}
                @Override public void mouseEntered(MouseEvent e) {}
                @Override public void mouseExited(MouseEvent e) {}

            });
            this.textArea.setOpaque(false);
            
            this.textArea.setBorder(new LineBorder(Color.BLACK));
            this.textArea.setText(this.getTextAreaText());
            
            this.rightPanel.add(this.textArea, BorderLayout.CENTER);
            
            //diable button
            this.disableButton = new JCheckBox();
            this.disableButton.setText("Don't use");
            this.disableButton.setHorizontalAlignment(SwingConstants.CENTER);
            //this.disableButton.setBorderPainted(true);
            //this.disableButton.setBorder(new LineBorder(Color.BLACK));
            this.disableButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    selected = false;
                    repaint();
                    master.selectionChanged();
                }
            });
            this.disableButton.setOpaque(false);
            this.rightPanel.add(this.disableButton, BorderLayout.SOUTH);
        
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.MAX_HEIGHT));
        this.setMinimumSize(new Dimension(Integer.MAX_VALUE, this.MAX_HEIGHT));
    }
    
    private String getTextAreaText() {
        String s = "";
        if (this.pictureName == null || this.pictureName.length() < 1) 
            s += "<unknown name>";
        else
            s += this.pictureName;
        s += "\n# used: " + this.picture.timesUsed;

        return s;
    }
    
    protected boolean isUserEnabled() {
        return  ! this.disableButton.isSelected();
    }
    
    
    @Override public void paint(Graphics g) {
        this.repaintCount++;
        
        boolean disabled = this.disableButton.isSelected();
        
        if (this.selected && !disabled) //selected
            this.setBackground(this.SELECTED_COLOR);
        else if (disabled && !this.selected) //unused
            this.setBackground(this.UNSELECTED_COLOR);
        else if (!this.selected)            //unselected
            this.setBackground(this.UNSELECTED_COLOR);
        else {
            this.setBackground(Color.RED);
            System.out.println("BACKGROUND CHOOSING LOGIC ERROR in PictureSelectionEntry paint(g)");
        }
        
        
        super.paint(g);
    }
    
    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (this.isOpaque()) {
            Color color = (this.disableButton.isSelected()) ? ProjConsts.BACKGROUND_COLOR : this.getBackground();
            g.setColor(color);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }
    
    
}
