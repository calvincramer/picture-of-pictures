package pictureofpictures;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.util.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class MainFrame2 
    extends JFrame {
    
    public MainFrame2(boolean testMode) {
        super("Picture of Pictures"); //title of Frame
        
        this.IS_TESTING_DEVELOPMENT = testMode;
        
        this.initializeVariables();
        
        console.println("INITIALIZING");
        
        this.PICTURE_FILTER = new FileNameExtensionFilter("only pictures", "jpg", "jpeg", "png", "tif", "tiff", "gif"); //tif not accepted, but shown in dialog
        
        this.initializeComponents();    //initialize frame
        
        if (this.IS_TESTING_DEVELOPMENT)    //if testing may add components
            this.initializeTesting();
        
        this.setVisible(true);              //show frame
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        console.println("RUNNING");
    }
    
    private void initializeVariables() {
        this.console = System.out;  //console
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();   //capture global keyboard listener
        this.km = new KeyboardManager();
        manager.addKeyEventDispatcher(km);
        
        this.aspectRatios = new ArrayList<>();       //add aspect ratios
            this.aspectRatios.add(new AspectRatio(1,1,"Square"));
            this.aspectRatios.add(new AspectRatio(2,1));
            this.aspectRatios.add(new AspectRatio(4,3));
            this.aspectRatios.add(new AspectRatio(5,4));
            this.aspectRatios.add(new AspectRatio(5,3));
            this.aspectRatios.add(new AspectRatio(5,2));
            
        this.defaultDir = FileSystemView.getFileSystemView().getDefaultDirectory().getParentFile(); //get default picture directory
        console.println("Default directory: " + this.defaultDir);
        for (File f : this.defaultDir.listFiles()) {
            if (f.getName().equals("Pictures"))
                this.picturDir = f;
        }
        if (this.picturDir == null)
            console.println("Picture directory: not found") ;
        else 
            console.println("Pictures directory: " + this.picturDir);
        
    }
    
    private void initializeTesting() {
        List<BufferedImage> testImages = new ArrayList<>(); //testImages should dump into regular images array
        this.sm = new SelectionManager(this.middlePictureSelectionPanel);
        
        try {
            // get pictures
            testImages.add(ImageIO.read(getClass().getResourceAsStream("/pictureofpictures/testPictures/testPicture1.png")));
            testImages.add(ImageIO.read(getClass().getResourceAsStream("/pictureofpictures/testPictures/testPicture2.png")));
            testImages.add(ImageIO.read(getClass().getResourceAsStream("/pictureofpictures/testPictures/testPicture3.png")));
            testImages.add(ImageIO.read(getClass().getResourceAsStream("/pictureofpictures/testPictures/testPicture4.png")));
            testImages.add(ImageIO.read(getClass().getResourceAsStream("/pictureofpictures/testPictures/testPicture5.png")));
            
            //add to images
            List<SpecialPicture> testImgs = new ArrayList<>();
            for (int i = 0; i < testImages.size(); i++) {
                BufferedImage img = testImages.get(i);
                testImgs.add(new SpecialPicture(img, "testPicture"+(i+1)));
            }
            
            //update middle panel
            this.addPicturesToMiddlePanel(testImgs);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void initializeComponents() {
        //frame stuff
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(ProjConsts.BACKGROUND_COLOR);
        this.getContentPane().setBackground(ProjConsts.BACKGROUND_COLOR);
        this.setMinimumSize(new Dimension(1000,600));
        
        try {
            BufferedImage frameImg = ImageIO.read(getClass().getResourceAsStream("/pictureofpictures/frameIcon.png"));
            ImageIcon icon = new ImageIcon(frameImg);
            this.setIconImage(frameImg);
        } catch (Exception e) {e.printStackTrace();}
        
        this.addComponentListener(new ComponentListener() {
            @Override public void componentResized(ComponentEvent e) {
                //console.println(leftPanel.getWidth() + "\t" + middlePanel.getWidth() + "\t" + rightPanel.getWidth());
                //console.println(middlePictureSelectionPanel.getComponent(0).getHeight());
            }
            @Override public void componentMoved(ComponentEvent e) {}
            @Override public void componentShown(ComponentEvent e) {}
            @Override public void componentHidden(ComponentEvent e) {}
            
        });
        this.addWindowListener(new WindowListener() {
            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) {
                console.println("WINDOW CLOSING");
            }
            @Override public void windowClosed(WindowEvent e) {}
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });

        //left panel
        // <editor-fold>
        this.leftPanel = new JPanel();
        this.leftPanel.setLayout(new BoxLayout(this.leftPanel, BoxLayout.Y_AXIS));
        this.leftPanel.setMaximumSize(this.leftPanelMaxSize);
        this.leftPanel.setPreferredSize(this.leftPanelPrefSize);
        this.leftPanel.setMinimumSize(this.leftPanelMinSize);
        //this.leftPanel.setBorder(this.simpleLineBorder);
        this.leftPanel.setBackground(ProjConsts.BACKGROUND_COLOR); //wont probably see panel, as subpanels should fit all space
        this.getContentPane().add(this.leftPanel);
            //button panel
            this.buttonPanel = new JPanel();
            this.buttonPanel.setBorder(new EmptyBorder(5,5,5,5)); //insets
            this.buttonPanel.setLayout(new GridLayout(0,1,0,4)); //one column, any number of rows,, hgap, vgap
            this.buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
            this.buttonPanel.setBackground(ProjConsts.BACKGROUND_COLOR);
            this.leftPanel.add(this.buttonPanel);
                //buttons
                //set source photo
                this.setSourcePhotoButton = new CustomButton("Set Source Photo");
                this.buttonPanel.add(this.setSourcePhotoButton);
                this.setSourcePhotoButton.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        setSourcePhotoButtonPressed();
                    }
                });
                //add pictures
                this.addPicturesButton = new CustomButton("Add Pictures");
                this.buttonPanel.add(this.addPicturesButton);
                this.addPicturesButton.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        addPicturesButtonPressed();
                    }
                });
                //repaint
                this.repaintButton = new CustomButton("Repaint");
                this.repaintButton.setEnabled(false);
                this.buttonPanel.add(this.repaintButton);
                this.repaintButton.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        repaintButtonPressed();
                    }
                });
                //remove selected photos
                this.removeSelectedButton = new CustomButton("Remove Selected");
                this.removeSelectedButton.setEnabled(false);
                this.buttonPanel.add(this.removeSelectedButton);
                this.removeSelectedButton.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        removeSelectedButtonPressed();
                    }
                });
                //export photo
                this.exportPhotoButton = new CustomButton("Export Photo");
                this.exportPhotoButton.setEnabled(true);
                this.buttonPanel.add(this.exportPhotoButton);
                this.exportPhotoButton.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        exportPhotoButtonPressed();
                    }
                });
                //set save profile button
                this.saveProfileButton = new CustomButton("Save Profile");
                this.buttonPanel.add(this.saveProfileButton);
                this.saveProfileButton.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        saveProfileButtonPressed();
                    }
                });
                //set open profile button
                this.openProfileButton = new CustomButton("Open Profile");
                this.buttonPanel.add(this.openProfileButton);
                this.openProfileButton.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        openProfileButtonPressed();
                    }
                });
                //hide show extra information button
                this.hideShowInfoButton = new CustomButton("Hide Information");
                this.buttonPanel.add(this.hideShowInfoButton);
                this.hideShowInfoButton.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        hideShowInfoButtonPressed();
                    }
                });
                //view output image fullscreen button
                this.viewOutputFullscreenButton = new CustomButton("View Output Fullscreen");
                this.buttonPanel.add(this.viewOutputFullscreenButton);
                this.viewOutputFullscreenButton.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        viewOutputImageFullscreenButtonPressed();
                    } 
                });
                
            //options label panel
            this.optionsLabelPanel = new JPanel();
            this.optionsLabelPanel.setLayout(new GridLayout(0,1,0,0));
            this.optionsLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            this.optionsLabelPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
            this.optionsLabelPanel.setBackground(ProjConsts.BACKGROUND_COLOR);
            this.leftPanel.add(this.optionsLabelPanel);
                //add options label
                this.optionsLabel = new JLabel("Options"){
                    @Override public void paint(Graphics g) {
                        Graphics2D g2 = (Graphics2D)g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                        g2.setColor(ProjConsts.GREY_6);
                        g2.fillRoundRect(4, this.getHeight() - 4, this.getWidth() - 8, 4, 10, 4);
                        
                        super.paint(g);

                }};
                this.optionsLabel.setOpaque(false);
                this.setLabelLookAndFeel(this.optionsLabel, 26);
                this.optionsLabel.setVerticalAlignment(JLabel.TOP);
                this.optionsLabel.setHorizontalAlignment(JLabel.CENTER);
                this.optionsLabelPanel.add(this.optionsLabel);
                
            //options scroll panel
            this.optionsScrollPanel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            this.optionsScrollPanel.getVerticalScrollBar().setUI(new CustomScrollBarUI());
            this.optionsScrollPanel.getVerticalScrollBar().setUnitIncrement(ProjConsts.SCROLL_BAR_INCREMENT);
            this.optionsScrollPanel.getVerticalScrollBar().setPreferredSize(new Dimension(ProjConsts.SCROLL_BAR_WIDTH, Integer.MAX_VALUE));
            this.optionsScrollPanel.setBorder(null);
            this.optionsScrollPanel.getViewport().setBackground(ProjConsts.BACKGROUND_COLOR);
            this.leftPanel.add(this.optionsScrollPanel);
                //options panel
                this.optionsPanel = new JPanel();
                this.optionsPanel.setLayout(new BoxLayout(this.optionsPanel, BoxLayout.Y_AXIS));
                this.optionsPanel.setBackground(ProjConsts.BACKGROUND_COLOR);
                this.optionsScrollPanel.getViewport().add(this.optionsPanel);
                    //add options
                    //little photo resolution option
                    // <editor-fold>
                    this.littlePhotoResOptionPanel = new JPanel();
                    this.littlePhotoResOptionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, NORMAL_OPTION_PANEL_HEIGHT));
                    this.littlePhotoResOptionPanel.setLayout(new GridBagLayout());
                    this.littlePhotoResOptionPanel.setOpaque(false);
                    this.optionsPanel.add(this.littlePhotoResOptionPanel);
                        GridBagConstraints gbc = new GridBagConstraints();
                        
                        this.littlePhotoResOptionLabel = new JLabel("Little Photo Resolution:");
                        this.setLabelLookAndFeel(this.littlePhotoResOptionLabel, 12);
                        this.littlePhotoResOptionLabel.setHorizontalAlignment(JLabel.HORIZONTAL);
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        gbc.weightx = 0.5;
                        gbc.insets = new Insets(5,5,5,0);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.littlePhotoResOptionPanel.add(this.littlePhotoResOptionLabel, gbc);
                        

                        this.littlePhotoResOptionTextField = new JTextField();
                        this.littlePhotoResOptionTextField.setDocument(new CustomDocument(4, CustomDocument.TYPE_ONLY_DIGITS));
                        this.littlePhotoResOptionTextField.addFocusListener(new FocusListener() {
                            @Override public void focusGained(FocusEvent e) {}
                            @Override public void focusLost(FocusEvent e) {
                                littlePhotoResOptionTextFieldFocusLost();
                        }});
                        this.littlePhotoResOptionTextField.setText("50");
                        gbc.gridx = 1;
                        gbc.gridy = 0;
                        gbc.weightx = 0.5;
                        gbc.insets = new Insets(5,0,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.littlePhotoResOptionPanel.add(this.littlePhotoResOptionTextField, gbc);
                        
                        this.littlePhotoResOptionSlider = new CustomSlider();
                        this.littlePhotoResOptionSlider.addMouseMotionListener(new MouseMotionAdapter() {
                            @Override public void mouseDragged(MouseEvent e) {
                                littlePhotoResOptionTextField.setText(littlePhotoResOptionSlider.getValue() + "");
                        }});
                        this.littlePhotoResOptionSlider.setMinimum(10);
                        this.littlePhotoResOptionSlider.setMaximum(500);
                        this.littlePhotoResOptionSlider.setOpaque(false);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        gbc.insets = new Insets(0,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        gbc.gridwidth = 2;
                        this.littlePhotoResOptionPanel.add(this.littlePhotoResOptionSlider, gbc);
                    // </editor-fold> 
                       
                    this.optionsPanel.add(this.getNewHorizontalSeperator(this.SEPERATOR_HEIGHT));
                    //scan resolution option
                    // <editor-fold>
                    this.scanResOptionPanel = new JPanel();
                    this.scanResOptionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, NORMAL_OPTION_PANEL_HEIGHT));
                    this.scanResOptionPanel.setLayout(new GridBagLayout());
                    this.scanResOptionPanel.setOpaque(false);
                    this.optionsPanel.add(this.scanResOptionPanel);
                        gbc = new GridBagConstraints();
                        
                        this.scanResOptionLabel = new JLabel("Scan Resolution:");
                        this.setLabelLookAndFeel(this.scanResOptionLabel, 12);
                        this.scanResOptionLabel.setHorizontalAlignment(JLabel.HORIZONTAL);
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        gbc.weightx = 0.6;
                        gbc.insets = new Insets(5,5,5,0);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.scanResOptionPanel.add(this.scanResOptionLabel, gbc);

                        this.scanResOptionTextField = new JTextField();
                        this.scanResOptionTextField.setDocument(new CustomDocument(4, CustomDocument.TYPE_ONLY_DIGITS));
                        this.scanResOptionTextField.addFocusListener(new FocusListener() {
                            @Override public void focusGained(FocusEvent e) {}
                            @Override public void focusLost(FocusEvent e) {
                                scanResOptionTextFieldFocusLost();
                        }});
                        this.scanResOptionTextField.setText("64");
                        gbc.gridx = 1;
                        gbc.gridy = 0;
                        gbc.weightx = 0.4;
                        gbc.insets = new Insets(5,0,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.scanResOptionPanel.add(this.scanResOptionTextField, gbc);

                        this.scanResOptionSlider = new CustomSlider();
                        this.scanResOptionSlider.addMouseMotionListener(new MouseMotionAdapter() {
                            @Override public void mouseDragged(MouseEvent e) {
                                scanResOptionTextField.setText(scanResOptionSlider.getValue() + "");
                        }});
                        this.scanResOptionSlider.setMinimum(10);
                        this.scanResOptionSlider.setMaximum(500);
                        this.scanResOptionSlider.setValue(64);
                        this.scanResOptionSlider.setOpaque(false);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        gbc.insets = new Insets(0,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        gbc.gridwidth = 2;
                        this.scanResOptionPanel.add(this.scanResOptionSlider, gbc);
                    // </editor-fold>  
                       
                    this.optionsPanel.add(this.getNewHorizontalSeperator(this.SEPERATOR_HEIGHT));
                    //scan type option
                    // <editor-fold>
                    this.scanTypeOptionPanel = new JPanel();
                    this.scanTypeOptionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, NORMAL_OPTION_PANEL_HEIGHT + 30));
                    this.scanTypeOptionPanel.setLayout(new GridBagLayout());
                    this.scanTypeOptionPanel.setOpaque(false);
                    this.optionsPanel.add(this.scanTypeOptionPanel);
                        gbc = new GridBagConstraints();
                        
                        this.scanTypeOptionLabel = new JLabel("Scan Type:");
                        this.setLabelLookAndFeel(this.scanTypeOptionLabel, 12);
                        this.scanTypeOptionLabel.setHorizontalAlignment(JLabel.HORIZONTAL);
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        gbc.weightx = 0.0;
                        gbc.insets = new Insets(5,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.scanTypeOptionPanel.add(this.scanTypeOptionLabel, gbc);

                        this.scanTypeOptionComboBox = new JComboBox(this.aspectRatios.toArray(new AspectRatio[this.aspectRatios.size()]));
                        ((JLabel)this.scanTypeOptionComboBox.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
                        this.scanTypeOptionComboBox.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                aspectRatioComboBoxAction();
                        }});
                        this.scanTypeOptionComboBox.addMouseWheelListener(new MouseWheelListener() { //otherwise event is consumed and scrollpane does not scroll
                            @Override public void mouseWheelMoved(MouseWheelEvent e) {
                                Component source = (Component) e.getSource();
                                source.getParent().dispatchEvent(e);
                        }});
                        this.scanTypeOptionComboBox.setMaximumRowCount(this.scanTypeOptionComboBox.getItemCount());
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        gbc.weightx = 0.0;
                        gbc.insets = new Insets(0,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.scanTypeOptionPanel.add(this.scanTypeOptionComboBox, gbc);

                        this.scanTypeOptionFlipARButton = new JButton("Flip Aspect Ratio");
                        this.scanTypeOptionFlipARButton.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                flipAspectRatioButtonPressed(e);
                        }});
                        gbc.gridx = 0;
                        gbc.gridy = 3;
                        gbc.weightx = 0.0;
                        gbc.insets = new Insets(0,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.scanTypeOptionPanel.add(this.scanTypeOptionFlipARButton, gbc);

                        this.scanTypeOptionARVisual = new JPanel() {
                            @Override public void paint(Graphics g) {
                                super.paint(g);
                                
                                Graphics2D g2 = (Graphics2D)g;
                                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                                
                                AspectRatio ar = (AspectRatio) scanTypeOptionComboBox.getSelectedItem();
                                
                                double panelAR = this.getWidth() * 1.0 / this.getHeight();
                                double visAR = ar.width * 1.0 / ar.height;
                                
                                //background
                                g.setColor(SCAN_TYPE_OPTION_AR_VISUAL_COLOR);
                                int x, y, width, height;
                                if(panelAR > visAR) { //panel too horizontal
                                    width = (int) (ar.width / (ar.height * 1.0 / this.getHeight() ) );
                                    x = (this.getWidth() / 2) - (width / 2);
                                    y = 0;
                                    height = this.getHeight();
                                } else if(panelAR < visAR) { //panel too vertical
                                    height = (int) (ar.height / (ar.width * 1.0 / this.getWidth() ) );
                                    x = 0;
                                    y = (this.getHeight() / 2) - (height / 2);
                                    width = this.getWidth();
                                } else { //same aspect ratios
                                    x = 0;
                                    y = 0;
                                    width = this.getWidth();
                                    height = this.getHeight();
                                }
                                g.fillRect(x, y, width, height);  
                                
                                //AR numbers
                                g.setColor(Color.BLACK);
                                int middleW = this.getWidth() / 2;
                                int middleH = this.getHeight() / 2;
                                int rightX = this.getWidth() - ( (this.getWidth() - width) / 2 );
                                int topY =  (this.getHeight() - height) / 2 ;
                                
                                String w = ar.width + "";
                                String h = ar.height + "";
                                
                                int stringWidth = g.getFontMetrics().stringWidth(w);
                                int stringHeight = g.getFontMetrics().getHeight();
                                
                                g.drawString(w, middleW - (stringWidth / 2), topY + 12);
                                g.drawString(h, rightX - 10, middleH + (stringHeight / 2)); // y goes positive downward
                        }};
                        //this.scanTypeOptionARVisual.setBorder(this.lightGrayLineBorder);
                        this.scanTypeOptionARVisual.setOpaque(false);
                        gbc.gridx = 1;
                        gbc.gridy = 0;
                        gbc.weightx = 1;
                        gbc.insets = new Insets(5,0,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        gbc.gridheight = 4;
                        this.scanTypeOptionPanel.add(this.scanTypeOptionARVisual, gbc); 
                    // </editor-fold>
                        
                    this.optionsPanel.add(this.getNewHorizontalSeperator(this.SEPERATOR_HEIGHT));
                    //use pictures amount option
                    // <editor-fold>
                    this.usePicturesOptionPanel = new JPanel();
                    this.usePicturesOptionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, NORMAL_OPTION_PANEL_HEIGHT + 30));
                    this.usePicturesOptionPanel.setLayout(new GridBagLayout());
                    this.usePicturesOptionPanel.setOpaque(false);
                    this.optionsPanel.add(this.usePicturesOptionPanel);
                        gbc = new GridBagConstraints();
                            
                        this.usePicturesMainLabel = new JLabel("Use each Picture:");
                        this.setLabelLookAndFeel(this.usePicturesMainLabel, 15.5f);
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        gbc.gridwidth = 3;
                        gbc.weightx = 1.0;
                        gbc.insets = new Insets(5,5,5,5);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        this.usePicturesOptionPanel.add(this.usePicturesMainLabel, gbc);

                        this.usePicturesLabel2 = new JLabel("At most");
                        this.setLabelLookAndFeel(this.usePicturesLabel2, 12);
                        this.usePicturesLabel2.setHorizontalAlignment(JLabel.RIGHT);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        gbc.gridwidth = 1;
                        gbc.insets = new Insets(0,5,5,5);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        this.usePicturesOptionPanel.add(this.usePicturesLabel2, gbc);

                        Object[] ints = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
                        this.usePicturesComboBox = new JComboBox(ints);
                        this.usePicturesComboBox.setEnabled(false);
                        this.usePicturesComboBox.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                int n = (int) usePicturesComboBox.getSelectedItem();
                                String t = (n == 1) ? "time" : "times";
                                usePicturesLabel3.setText(t);
                        }});
                        this.usePicturesComboBox.addMouseWheelListener(new MouseWheelListener() { //otherwise event is consumed and scrollpane does not scroll
                            @Override public void mouseWheelMoved(MouseWheelEvent e) {
                                Component source = (Component) e.getSource();
                                source.getParent().dispatchEvent(e);
                        }});
                        gbc.gridx = 1;
                        gbc.gridy = 1;
                        gbc.gridwidth = 1;
                        gbc.insets = new Insets(0,0,5,5);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        this.usePicturesOptionPanel.add(this.usePicturesComboBox, gbc);

                        this.usePicturesLabel3 = new JLabel("time");
                        this.setLabelLookAndFeel(this.usePicturesLabel3, 12);
                        gbc.gridx = 2;
                        gbc.gridy = 1;
                        gbc.gridwidth = 1;
                        gbc.insets = new Insets(0,0,5,5);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        this.usePicturesOptionPanel.add(this.usePicturesLabel3, gbc);

                        this.usePicturesIndefCheckBox = new JCheckBox("as many times needed", true); //default is selected
                        this.usePicturesIndefCheckBox.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                if (usePicturesIndefCheckBox.isSelected())
                                    usePicturesComboBox.setEnabled(false);
                                else
                                    usePicturesComboBox.setEnabled(true);
                        }});
                        this.usePicturesIndefCheckBox.setFont(ProjConsts.NORM_FONT);
                        this.usePicturesIndefCheckBox.setForeground(ProjConsts.TEXT_COLOR);
                        this.usePicturesIndefCheckBox.setOpaque(false);
                        this.usePicturesIndefCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
                        gbc.gridx = 0;
                        gbc.gridy = 2;
                        gbc.gridwidth = 3;
                        gbc.insets = new Insets(0,5,5,5);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        this.usePicturesOptionPanel.add(this.usePicturesIndefCheckBox, gbc);
                    // </editor-fold>
                        
                    this.optionsPanel.add(this.getNewHorizontalSeperator(this.SEPERATOR_HEIGHT));
                    //select pictures option 
                    // <editor-fold>
                    this.selectPicturesOptionPanel = new JPanel();
                    this.selectPicturesOptionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, NORMAL_OPTION_PANEL_HEIGHT + 50));
                    this.selectPicturesOptionPanel.setLayout(new GridBagLayout());
                    this.selectPicturesOptionPanel.setOpaque(false);
                    this.optionsPanel.add(this.selectPicturesOptionPanel);
                        gbc = new GridBagConstraints();
                        
                        this.selectPicturesOptionLabel = new JLabel("Select Pictures By:");
                        this.setLabelLookAndFeel(this.selectPicturesOptionLabel, 15.5f);
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        gbc.gridwidth = 2;
                        gbc.insets = new Insets(5,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.selectPicturesOptionPanel.add(this.selectPicturesOptionLabel, gbc);
                        
                        this.selectPicturesBestButton = new JRadioButton("best picture");
                        this.selectPicturesBestButton.setOpaque(false);
                        this.selectPicturesBestButton.setFont(ProjConsts.NORM_FONT);
                        this.selectPicturesBestButton.setForeground(ProjConsts.TEXT_COLOR);
                        this.selectPicturesBestButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (selectPicturesBestButton.isSelected())
                                    selectPicturesRangeSlider.setEnabled(false);
                                else
                                    selectPicturesRangeSlider.setEnabled(true);
                        }});
                        this.selectPicturesBestButton.setSelected(true);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        gbc.gridwidth = 2;
                        gbc.insets = new Insets(0,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.selectPicturesOptionPanel.add(this.selectPicturesBestButton, gbc);
                       
                        this.selectPicturesWithinRangeButton = new JRadioButton("from within range:");
                        this.selectPicturesWithinRangeButton.setOpaque(false);
                        this.selectPicturesWithinRangeButton.setFont(ProjConsts.NORM_FONT);
                        this.selectPicturesWithinRangeButton.setForeground(ProjConsts.TEXT_COLOR);
                        this.selectPicturesWithinRangeButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (selectPicturesBestButton.isSelected())
                                    selectPicturesRangeSlider.setEnabled(false);
                                else
                                    selectPicturesRangeSlider.setEnabled(true);
                        }});
                        gbc.gridx = 0;
                        gbc.gridy = 2;
                        gbc.gridwidth = 2;
                        gbc.insets = new Insets(0,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.selectPicturesOptionPanel.add(this.selectPicturesWithinRangeButton, gbc);
                        
                        this.selectPicturesRangeSlider = new CustomSlider();
                        this.selectPicturesRangeSlider.setOpaque(false);
                        this.selectPicturesRangeSlider.setPreferredSize(new Dimension(140, Integer.MAX_VALUE));
                        this.selectPicturesRangeSlider.setEnabled(false);
                        this.selectPicturesRangeSlider.addMouseMotionListener(new MouseMotionAdapter() {
                            @Override public void mouseDragged(MouseEvent e) {
                                int val = selectPicturesRangeSlider.getValue();
                                String s = val + "%";
                                for (int i = s.length(); i <= 4; i++)
                                    s += " ";
                                selectPicturesRangeLabel.setText(s);
                        }});
                        this.selectPicturesRangeSlider.setMinimum(0);
                        this.selectPicturesRangeSlider.setMaximum(100);
                        this.selectPicturesRangeSlider.setValue(10);
                        gbc.gridx = 0;
                        gbc.gridy = 3;
                        gbc.gridwidth = 1;
                        gbc.weightx = 0.6;
                        gbc.insets = new Insets(0,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.selectPicturesOptionPanel.add(this.selectPicturesRangeSlider, gbc);
                        
                        this.selectPicturesRangeLabel = new JLabel("10% ");
                        this.selectPicturesRangeLabel.setVerticalAlignment(JLabel.CENTER);
                        this.setLabelLookAndFeel(this.selectPicturesRangeLabel, 12);
                        gbc.gridx = 1;
                        gbc.gridy = 3;
                        gbc.gridwidth = 1;
                        gbc.weightx = 0.4;
                        gbc.insets = new Insets(0,0,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.selectPicturesOptionPanel.add(this.selectPicturesRangeLabel, gbc);
                        
                        this.selectPicturesButtonGroup = new ButtonGroup();
                            this.selectPicturesButtonGroup.add(this.selectPicturesBestButton);
                            this.selectPicturesButtonGroup.add(this.selectPicturesWithinRangeButton);
                    // </editor-fold>
                          
                    this.optionsPanel.add(this.getNewHorizontalSeperator(this.SEPERATOR_HEIGHT));
                    //original res option
                    // <editor-fold>
                    this.retainOriginalResOptionPanel = new JPanel();
                    this.retainOriginalResOptionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, NORMAL_OPTION_PANEL_HEIGHT + 20));
                    this.retainOriginalResOptionPanel.setLayout(new GridBagLayout());
                    this.retainOriginalResOptionPanel.setOpaque(false);
                    this.optionsPanel.add(this.retainOriginalResOptionPanel);
                        gbc = new GridBagConstraints();
                        
                        this.retainOrigResLabel = new JLabel("Retain original resolution?");
                        this.setLabelLookAndFeel(this.retainOrigResLabel, 15.5f);
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        gbc.insets = new Insets(5,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.retainOriginalResOptionPanel.add(this.retainOrigResLabel, gbc);
                        
                        this.retainARButton = new JRadioButton("use entire source image");
                        this.retainARButton.setSelected(true);
                        this.retainARButton.setOpaque(false);
                        this.retainARButton.setFont(ProjConsts.NORM_FONT);
                        this.retainARButton.setForeground(ProjConsts.TEXT_COLOR);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        gbc.insets = new Insets(0,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.retainOriginalResOptionPanel.add(this.retainARButton, gbc);
                        
                        this.onlyWholeImageButton = new JRadioButton("only output whole images");
                        this.onlyWholeImageButton.setSelected(false);
                        this.onlyWholeImageButton.setOpaque(false);
                        this.onlyWholeImageButton.setFont(ProjConsts.NORM_FONT);
                        this.onlyWholeImageButton.setForeground(ProjConsts.TEXT_COLOR);
                        gbc.gridx = 0;
                        gbc.gridy = 2;
                        gbc.insets = new Insets(0,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.retainOriginalResOptionPanel.add(this.onlyWholeImageButton, gbc);
                        
                        this.retainOriginalResButtonGroup = new ButtonGroup();
                        this.retainOriginalResButtonGroup.add(this.retainARButton);
                        this.retainOriginalResButtonGroup.add(this.onlyWholeImageButton);
                    // </editor-fold>
                        
                    this.optionsPanel.add(this.getNewHorizontalSeperator(this.SEPERATOR_HEIGHT));
                    //blur sharpen option
                    // <editor-fold>
                    this.sharpenBlurImageOptionPanel = new JPanel();
                    this.sharpenBlurImageOptionPanel.setBorder(new LineBorder(Color.gray, 1));
                    this.sharpenBlurImageOptionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, NORMAL_OPTION_PANEL_HEIGHT + 20));
                    this.sharpenBlurImageOptionPanel.setLayout(new GridBagLayout());
                    this.sharpenBlurImageOptionPanel.setOpaque(false);
                    this.optionsPanel.add(this.sharpenBlurImageOptionPanel);
                    
                        this.sharpenBlurLabel = new JLabel("Sharpen or blur image:");
                        this.sharpenBlurLabel.setBorder(new LineBorder(Color.WHITE, 1));
                        this.setLabelLookAndFeel(this.sharpenBlurLabel, 15.5f);
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        gbc.insets = new Insets(5,5,5,0);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.sharpenBlurImageOptionPanel.add(this.sharpenBlurLabel, gbc);
                        
                        this.sharpenBlurCheckBox = new JCheckBox();
                        this.sharpenBlurCheckBox.setText("no");
                        this.sharpenBlurCheckBox.setFont(ProjConsts.NORM_FONT);
                        this.sharpenBlurCheckBox.setForeground(ProjConsts.TEXT_COLOR);
                        this.sharpenBlurCheckBox.setOpaque(false);
                        this.sharpenBlurCheckBox.setPreferredSize(new Dimension(45,15));
                        this.sharpenBlurCheckBox.setBorder(new LineBorder(Color.WHITE, 1));
                        this.sharpenBlurCheckBox.setSelected(false);
                        this.sharpenBlurCheckBox.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                sharpenBlurCheckBox.setText( sharpenBlurCheckBox.isSelected() ? "yes" : "no" );
                        }});
                        gbc.gridx = 1;
                        gbc.gridy = 0;
                        gbc.insets = new Insets(5,0,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.sharpenBlurImageOptionPanel.add(this.sharpenBlurCheckBox, gbc);
                        
                        this.sharpenLabel = new JLabel("Sharpen");
                        this.sharpenLabel.setBorder(new LineBorder(Color.WHITE, 1));
                        this.sharpenLabel.setPreferredSize(new Dimension(45,15));
                        this.sharpenLabel.setMaximumSize(new Dimension(45,15));
                        this.sharpenLabel.setMinimumSize(new Dimension(45,15));
                        this.sharpenLabel.setFont(ProjConsts.NORM_FONT);
                        this.sharpenLabel.setForeground(ProjConsts.TEXT_COLOR);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        gbc.gridwidth = 1;
                        gbc.weightx = 0.25;
                        gbc.insets = new Insets(5,5,5,0);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.sharpenBlurImageOptionPanel.add(this.sharpenLabel, gbc);
                        
                        this.blurLabel = new JLabel("Blur");
                        this.blurLabel.setHorizontalAlignment(JLabel.RIGHT);
                        this.setLabelLookAndFeel(this.blurLabel, 15.5f);
                        gbc.gridx = 1;
                        gbc.gridy = 1;
                        gbc.insets = new Insets(5,5,5,5);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.sharpenBlurImageOptionPanel.add(this.blurLabel, gbc);
                        
                        this.sharpenBlurSlider = new CustomSlider();
                        this.setSliderLookAndFeel(this.sharpenBlurSlider);
                        this.sharpenBlurSlider.setMinimum(-1000);
                        this.sharpenBlurSlider.setMaximum(1000);
                        this.sharpenBlurSlider.setValue(0);
                        gbc.gridx = 0;
                        gbc.gridy = 2;
                        gbc.gridwidth = 2;
                        gbc.insets = new Insets(5,0,15,0);
                        gbc.fill = GridBagConstraints.BOTH;
                        this.sharpenBlurImageOptionPanel.add(this.sharpenBlurSlider, gbc);
                        
                        
                    // </editor-fold>
                    
                    //end options
                    
        //end left panel
        // </editor-fold>            
        
        //middle panel
        // <editor-fold>
        this.middlePanel = new JPanel();
        this.middlePanel.setLayout(new GridLayout(1,1));
        this.middlePanel.setOpaque(true);
        //this.middlePanel.setBorder(new LineBorder(ProjConsts.MEDIUM_GREY, 2));
        this.middlePanel.setMaximumSize(this.middlePanelMaxSize);
        this.middlePanel.setPreferredSize(this.middlePanelPrefSize);
        this.middlePanel.setMinimumSize(this.middlePanelMinSize);
        this.getContentPane().add(this.middlePanel);
            //make scroll pane
            this.middleScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            this.middleScrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
            this.middleScrollPane.getVerticalScrollBar().setUnitIncrement(ProjConsts.SCROLL_BAR_INCREMENT);
            this.middleScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(ProjConsts.SCROLL_BAR_WIDTH, Integer.MAX_VALUE));
            this.middleScrollPane.setOpaque(false);
            this.middleScrollPane.setBorder(null);
            this.middleScrollPane.getVerticalScrollBar().setUnitIncrement(8);
            //this.middlePanel.add(this.middleScrollPane);
                //scrolling panel
                this.middleScrollingPanel = new JPanel();
                this.middleScrollingPanel.setLayout(new BoxLayout(this.middleScrollingPanel, BoxLayout.Y_AXIS));
                this.middleScrollingPanel.setOpaque(false);
                this.middleScrollPane.getViewport().add(this.middleScrollingPanel);
                    //add panel
                    this.middlePictureSelectionPanel = new JPanel();
                    this.middlePictureSelectionPanel.setBorder(new EmptyBorder(10,10,10,10));
                    this.middlePictureSelectionPanel.setLayout(new BoxLayout(this.middlePictureSelectionPanel, BoxLayout.Y_AXIS));
                    this.middlePictureSelectionPanel.setBackground(ProjConsts.BACKGROUND_COLOR);
                    this.middleScrollingPanel.add(this.middlePictureSelectionPanel);
                    //add addpicturespanel
                    this.middleAddPicturesPanel = new JPanel() {
                        @Override public void paint(Graphics g) {
                            Graphics2D g2 = (Graphics2D)g;
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                            
                            g.setColor(ProjConsts.BACKGROUND_COLOR);
                            g.fillRect(0, 0, this.getWidth(), this.getHeight());
                            
                            int mid = this.getWidth() / 2;
                            int roundBy = 8;
                            g.setColor(ProjConsts.LIGHT_GREY);
                            g.fillOval(mid - 30, 0, 60, 60);
                            
                            g.setColor(ProjConsts.BACKGROUND_COLOR);
                            g.fillRoundRect(mid - 4, 8, 8, 44, roundBy, roundBy);
                            g.fillRoundRect(mid - 30 + 8, 30 - 4, 44, 8, roundBy, roundBy);
                            
                            
                        }
                        @Override public void update(Graphics g) {}
                    };
                    this.middleAddPicturesPanel.setBackground(ProjConsts.BACKGROUND_COLOR);
                    this.middleAddPicturesPanel.setMinimumSize(new Dimension(100, 70));
                    this.middleAddPicturesPanel.setPreferredSize(new Dimension(100, 70));
                    this.middleAddPicturesPanel.addMouseListener(new MouseListener() {
                        @Override public void mouseClicked(MouseEvent e) {}
                        @Override public void mousePressed(MouseEvent e) {
                            addPicturesButton.doClick();
                        }
                        @Override public void mouseReleased(MouseEvent e) {}
                        @Override public void mouseEntered(MouseEvent e) {}
                        @Override public void mouseExited(MouseEvent e) {}
                        
                    });
                    this.middleScrollingPanel.add(this.middleAddPicturesPanel);
                
            //add no pictures temp panel
            this.middlePanelNoPicturesLabel = new JLabel();
            this.middlePanelNoPicturesLabel.setText("(click 'Add Pictures' button)");
            this.middlePanelNoPicturesLabel.setOpaque(false);
            this.setLabelLookAndFeel(this.middlePanelNoPicturesLabel, 18);
            this.middlePanelNoPicturesLabel.setHorizontalAlignment(JLabel.CENTER);
            this.middlePanelNoPicturesLabel.addMouseListener(new MouseListener() {
                    @Override public void mousePressed(MouseEvent e) {
                        addPicturesButtonPressed();
                        
                        if (sm.getNumEntries() != 0) {
                            middlePanel.removeAll(); //remove no pictures temp panel
                            middlePanel.add(middleScrollPane); //add scroll pane which contains the pictures
                            middlePanel.revalidate();
                            middlePanel.repaint();
                        }
                    }
                    @Override public void mouseClicked(MouseEvent e) {}
                    @Override public void mouseReleased(MouseEvent e) {}
                    @Override public void mouseEntered(MouseEvent e) {}
                    @Override public void mouseExited(MouseEvent e) {}
                });
            //if in testing mode, add pictureSelectionPanel
            //otherwise add no pictures placeholder
            if (this.IS_TESTING_DEVELOPMENT)
                this.middlePanel.add(this.middleScrollPane);
            else
                this.middlePanel.add(this.middlePanelNoPicturesLabel);
            
        // </editor-fold>
            
        //right panel
        // <editor-fold>
        this.rightPanel = new JPanel();
        this.rightPanel.setLayout(new GridLayout(1,1));
        this.rightPanel.setOpaque(false);
        this.rightPanel.setMaximumSize(this.rightPanelMaxSize);
        this.rightPanel.setPreferredSize(this.rightPanelPrefSize);
        this.getContentPane().add(this.rightPanel);
            //split pane
            this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            
            this.splitPane.setOpaque(false);
            this.splitPane.setContinuousLayout(true);
            this.splitPane.setDividerLocation( (int) (ProjConsts.SCREEN_SIZE.height * 0.3) );
            this.splitPane.setDividerSize(10);
            this.splitPane.setUI(new BasicSplitPaneUI() {
                public BasicSplitPaneDivider createDefaultDivider() {
                    return new BasicSplitPaneDivider(this) {
                        public void setBorder(Border b) {}

                        @Override public void paint(Graphics g) {
                            Graphics2D g2 = (Graphics2D)g;
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                            
                            g.setColor(ProjConsts.MEDIUM_GREY);
                            //g.fillRect(0, 0, getSize().width, getSize().height);
                            g.fillRect(0, 1, getSize().width, getSize().height - 2);
                            g.setColor(ProjConsts.GREY_2);
                            g.drawLine(0, 0, this.getWidth(), 0);
                            g.drawLine(0, this.getHeight()-1, this.getWidth(), this.getHeight()-1);
                            
                            g.setColor(ProjConsts.BACKGROUND_COLOR);
                            int midX = this.getWidth() / 2;
                            int widthCircles = this.getHeight() - 4;
                            int hp = 6; //horizontal padding
                            
                            g.fillOval(midX - (widthCircles / 2),                       2, widthCircles, widthCircles);
                            g.fillOval(midX - (widthCircles / 2) - hp - widthCircles,   2, widthCircles, widthCircles);
                            g.fillOval(midX + (widthCircles / 2) + hp + 1,              2, widthCircles, widthCircles);
                            
                            
                            super.paint(g);
                        }
                    };
                }
            });
            
            this.rightPanel.add(this.splitPane);
                //top panel
                this.topPanel = new InteractiveImagePanel(null) {
                    @Override public void paint(Graphics g) {
                        super.paint(g);
                        //paint overlay
                        if (!showExtraInformation) return;
                        
                        g.setColor(Color.WHITE);
                        g.setFont(ProjConsts.NORM_FONT);
                        FontMetrics fm = g.getFontMetrics(ProjConsts.NORM_FONT);
                        
                        String[] infoToPrint = getInfoStrings();
                        int py = this.getHeight() - 4;
                        int px = 4;
                        for (String s : infoToPrint) {
                            g.drawString(s, px, py);
                            py -= fm.getHeight();
                        }
                    }
                    @Override protected void noImageMousePress() {
                        setSourcePhotoButtonPressed();
                    }
                };
                this.topPanel.setLayout(new BorderLayout());
                this.topPanel.setOpaque(false);
                this.topPanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 100));
                this.splitPane.setTopComponent(this.topPanel);
                    //placeholder label
                    this.topPanelNoPicturePlaceholder = new CustomLabel("(click 'Set Source Photo' to start)");
                    this.setLabelLookAndFeel(this.topPanelNoPicturePlaceholder, 30);
                    this.topPanelNoPicturePlaceholder.setHorizontalAlignment(JLabel.CENTER);
                    this.topPanel.add(this.topPanelNoPicturePlaceholder, BorderLayout.CENTER);
                //bottom panel
                this.bottomPanel = new InteractiveImagePanel(null) {
                    @Override protected void noImageMousePress() {
                        repaintButtonPressed();
                    }
                };
                this.bottomPanel.setLayout(new BorderLayout());
                this.bottomPanel.setOpaque(false);
                this.bottomPanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 100));
                this.splitPane.setBottomComponent(this.bottomPanel);
                    //placeholder label
                    this.bottomPanelPlaceholder = new JLabel("(next click 'Repaint' to make collage)");
                    this.setLabelLookAndFeel(this.bottomPanelPlaceholder, 26);
                    this.bottomPanelPlaceholder.setHorizontalAlignment(JLabel.CENTER);
                    this.bottomPanel.add(this.bottomPanelPlaceholder, BorderLayout.CENTER);
                    
        // </editor-fold>
                
        this.pack();
        //set frame to center screen
        int x = (ProjConsts.SCREEN_SIZE.width / 2) - (this.getWidth() / 2);
        int y = (ProjConsts.SCREEN_SIZE.height / 2) - (this.getHeight() / 2);
        this.setLocation(x, y);
    }
    
    // <editor-fold desc="Form Actions (methods called by actions)">
    protected void setSourcePhotoButtonPressed() {
        console.print("Setting source photo: ");
        
        //dialog box
        JFileChooser chooserDialog = new JFileChooser();
        chooserDialog.setDialogTitle("Choose Background Image");
        chooserDialog.setFileFilter(this.PICTURE_FILTER);
        if (this.picturDir == null)
            chooserDialog.setCurrentDirectory(this.defaultDir);
        else
            chooserDialog.setCurrentDirectory(this.picturDir);
        
        int num = chooserDialog.showDialog(this, "OK");
        if(num == JFileChooser.APPROVE_OPTION) {
            
            //make repaint button enabled
            this.repaintButton.setEnabled(true);
            
            try {
                File chosenFile = chooserDialog.getSelectedFile();
                
                String ext = PictureHelper.getFileExtension(chosenFile.getName(), false);
                if (ext.equals("tif") || ext.equals("tiff")) {
                    JOptionPane.showMessageDialog(this, "The specified file:\n   " + 
                                        chosenFile.getAbsolutePath() + 
                                        "\nhas an unsupported file type: \t" + ext + 
                                        "\nPlease convert your photo to .png, .jpg, or .gif", 
                        "Unsupported Image Type", JOptionPane.ERROR_MESSAGE);
                    console.println("unsupported photo (.tif) selected");
                    return;
                }
                
                this.sourceImage = ImageIO.read(chosenFile);
                
                this.topPanel.removeAll(); //remove placeholder
                this.topPanel.setImage(this.sourceImage);
                
                console.println(chosenFile);
                
                this.repaint();
                
            } catch (IOException ex) {
                console.println(ex.toString());
                ex.printStackTrace();
            }
            
        } else {
            console.println("(canceled)");
        }
    }
    
    private void addPicturesButtonPressed() {
        console.println("Adding pictures...");
        
        JFileChooser chooser = new JFileChooser( (this.picturDir != null) ? this.picturDir : this.defaultDir );
        chooser.setDialogTitle("Add Pictures");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setMultiSelectionEnabled(true);
        
        int num = chooser.showDialog(this, "OK");
        
        if(num == JFileChooser.APPROVE_OPTION) {
            try {
                File[] chosenFiles = chooser.getSelectedFiles();
                
                console.println("\tUser chose " + chosenFiles.length + " files:");
                for (int i = 0; i < chosenFiles.length; i++) {
                    console.println("\t\t" + chosenFiles[i].toString());
                }
                
                List<File> files = PictureHelper.getAllFilesInList(chosenFiles); //get all files in folders
                files = PictureHelper.removeDirectories(files);                  //remove folders
                files = PictureHelper.keepFileTypes(files, PICTURE_TYPES_ACCEPTED); //keep the pictures
                
                console.println("\tFound " + files.size() + " pictures in selected files");
                
                List<SpecialPicture> toAdd = new ArrayList<>();
                for (int i = 0; i < files.size(); i++) {
                    BufferedImage img = ImageIO.read(files.get(i));
                    toAdd.add(new SpecialPicture(img, files.get(i).getName()));
                }
                
                //add images to middle panel
                this.addPicturesToMiddlePanel(toAdd);
                
                console.println("Added " + toAdd.size() + " pictures");

            } catch (IOException ex) {
                console.println(ex.toString());
            } catch (Exception e) {
                e.printStackTrace();
                console.println(e.toString());
            }
        }

    }
    
    private void repaintButtonPressed() {
        console.print("Repainting collage... ");
        
        BufferedImage toPaint = this.createCollage();
        
        if (toPaint != null) {
            this.bottomPanel.removeAll();
            
            this.bottomPanel.image = toPaint;
            this.bottomPanel.repaint();
        }
        
        //save reference of output image
        this.outputImage = toPaint;
        
        console.println("Done repainting collage.");
    }
    
    private void removeSelectedButtonPressed() {
        console.println("Removing selected photos...");
        
        //remove stuff
        int numRemoved = this.sm.removeSelectedFromSource();
        this.sm.removeAdjacentSeperatorsFromSource();
        
        //set remove button disabled
        this.removeSelectedButton.setEnabled(false);
        
        console.println("\tremoved " + numRemoved + " photo" 
                + (numRemoved != 1 ? "":"s") 
                + " from the list");
        
        //if no pictures, set placeholder label
        if (this.sm.getNumEntries() == 0) {
            this.middlePanel.removeAll(); //remove scroll pane
            this.middlePanel.add(this.middlePanelNoPicturesLabel);
        }
        
        //repaint
        this.middlePanel.revalidate();
        this.middlePanel.repaint();
    }
    
    private void exportPhotoButtonPressed() {
        console.print("Exporting photo...");
        
        if (this.outputImage == null) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        
        JFileChooser saveDialog = new JFileChooser( (this.picturDir != null) ? this.picturDir : this.defaultDir );
        saveDialog.setDialogTitle("Save Output Picture");
        saveDialog.setMultiSelectionEnabled(true);
        saveDialog.setSelectedFile(new File("collage1.png"));
        
        if (saveDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            //save file
            try {
                File fileToSave = saveDialog.getSelectedFile();
                ImageIO.write(this.outputImage, "png", fileToSave);
            } catch (Exception e) {e.printStackTrace(); }
        }
        
    }

    private void saveProfileButtonPressed() {
        console.println("Saving profile...");
    }
    
    private void openProfileButtonPressed() {
        console.println("Opening profile...");
    }
    
    private void viewOutputImageFullscreenButtonPressed() {
        console.print("Making output collage fullscreen... ");
        
        Image imageToShow = this.bottomPanel.image; //may be null if no image there
        
        if (imageToShow == null) {
            console.print("No image found");
            Toolkit.getDefaultToolkit().beep(); //button should not even be enabled if output image is null
            return;
        }
        console.print("");
        
        //make window
        this.fullScreenWindow = new JFrame("");
        this.fullScreenWindow.setBackground(ProjConsts.BACKGROUND_COLOR);
        
        this.fullScreenWindow.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) {
                fullScreenWindow.dispose();
            }
            @Override public void keyPressed(KeyEvent e) {
                fullScreenWindow.dispose();
            }
            @Override public void keyReleased(KeyEvent e) {
                fullScreenWindow.dispose();
            }
            
        });
        this.fullScreenWindow.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) {
                fullScreenWindow.dispose();
            }
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });
        
        
        JPanel imagePanel = new CenteredImagePanel(imageToShow); //output image
        imagePanel.setBackground(ProjConsts.BACKGROUND_COLOR);
        this.fullScreenWindow.add(imagePanel);
        
        //make fullscreen
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        screen.setFullScreenWindow(this.fullScreenWindow);
        this.fullScreenWindow.validate();
        this.fullScreenWindow.setVisible(true);
        
        
    }
    
    private void flipAspectRatioButtonPressed(ActionEvent e) {
        AspectRatio ar = (AspectRatio) this.scanTypeOptionComboBox.getSelectedItem();
        
        ar.invertRatio();
        this.repaint();
    }
    
    private void aspectRatioComboBoxAction() {
        this.repaint();
    }
    
    private void hideShowInfoButtonPressed() {
        this.showExtraInformation = !this.showExtraInformation;
        
        if (this.showExtraInformation == true)
            this.hideShowInfoButton.setText("Hide Info");
        else
            this.hideShowInfoButton.setText("Show Info");
        
        this.topPanel.repaint();
    }
    
    private void littlePhotoResOptionTextFieldFocusLost() {
        String s = this.littlePhotoResOptionTextField.getText();
        int n = Integer.parseInt(s);
        this.littlePhotoResOptionSlider.setValue(n);
        
        if (n < this.littlePhotoResOptionSlider.getMinimum())
            this.littlePhotoResOptionTextField.setText(this.littlePhotoResOptionSlider.getMinimum() + "");
    }
    
    private void scanResOptionTextFieldFocusLost() {
        String s = this.scanResOptionTextField.getText();
        int n = Integer.parseInt(s);
        this.scanResOptionSlider.setValue(n);
        
        if (n < this.scanResOptionSlider.getMinimum())
            this.scanResOptionTextField.setText(this.scanResOptionSlider.getMinimum() + "");
    }
    // </editor-fold>
    //end of form action methods
    
    private BufferedImage createCollage() {
        
        if (this.sourceImage == null) {
            Toolkit.getDefaultToolkit().beep();
            return null;
        }
        
        this.console.print("Making new collage... ");
        
        Dimension littlePhotoDim = this.calcLittlePhotoRes();
        Dimension scanDim = this.calcScanRes(); 
        Dimension outputImageDim = this.calcOutputRes(this.onlyWholeImageButton.isSelected(), scanDim, littlePhotoDim);
        
        int timesEachPictureUsed = this.usePicturesIndefCheckBox.isSelected() ? -1 : (int) this.usePicturesComboBox.getSelectedItem();
        double range = this.selectPicturesRangeSlider.getValue() * 1.0 / this.selectPicturesRangeSlider.getMaximum();
        double choosePicsInRange = this.selectPicturesWithinRangeButton.isSelected() ? range : -1; //-1 for best picture
        
        this.console.println("with dimensions " + outputImageDim.width + " by " + outputImageDim.height);
        
        BufferedImage temp = PictureHelper.makeCollageOfPictues(this.sm.getAllEnabledSpecialPictures(), this.sourceImage, 
                scanDim.width, scanDim.height, 
                new Dimension(littlePhotoDim.width, littlePhotoDim.height), new Dimension(outputImageDim.width, outputImageDim.height), 
                timesEachPictureUsed, choosePicsInRange);
        
        return temp;
    }
    
    private void setLabelLookAndFeel(JLabel label, float fontSize) {
        //label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(ProjConsts.NORM_FONT);
        label.setFont(ProjConsts.NORM_FONT.deriveFont( (float) fontSize));
        label.setDoubleBuffered(true);
        
        label.setForeground(ProjConsts.TEXT_COLOR);
    }
    
    private void setButtonLookAndFeel(JButton button) {
        button.setFont(ProjConsts.NORM_FONT);
        button.setForeground(ProjConsts.TEXT_COLOR);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
    }
    
    private void setSliderLookAndFeel(JSlider slider) {
        slider.setOpaque(false);
        
    }
    
    private void addPicturesToMiddlePanel(List<SpecialPicture> pictures) {
        if (pictures == null) return;
        if (pictures.size() <= 0) return;
        
        for (int i = 0; i < pictures.size(); i++) {
            if (this.middlePictureSelectionPanel.getComponentCount() != 0)
                this.middlePictureSelectionPanel.add(this.getNewHorizontalSeperator(4));
            
            this.middlePictureSelectionPanel.add( new PictureSelectionEntry(pictures.get(i), this) );
        }
        
        this.middleScrollPane.revalidate();
    }
    
    protected void selectionChanged() {
        Integer[] selected = this.sm.getSelectedIndecies();
        if (selected.length > 0)
            this.removeSelectedButton.setEnabled(true);
        else
            this.removeSelectedButton.setEnabled(false);
    }
    
    private JPanel getNewHorizontalSeperator(int height) {
        return new HorizontalSeperator(ProjConsts.HORIZONTAL_SPACER_COLOR, 6);
    }
    
    private int calcNumLittlePics() {
        if (this.sourceImage == null) return -1;
        
        Dimension scanRes = this.calcScanRes();
        
        int columns = this.sourceImage.getWidth() /  scanRes.width;
        int rows = this.sourceImage.getHeight() / scanRes.height;
        
        if (this.retainARButton.isSelected()) {
            if (this.sourceImage.getWidth() % scanRes.width != 0)   //if excess pixels filled with cut off image
                columns += 1;
            if (this.sourceImage.getHeight() % scanRes.height != 0) //if bottom has cut off images
                rows += 1;
        }
        
        return columns * rows;
    }
    
    private Dimension calcScanRes() {
        
        int scanRes = this.scanResOptionSlider.getValue();
        int width;
        int height;
        
        AspectRatio AR = (AspectRatio) this.scanTypeOptionComboBox.getSelectedItem();
        
        if (AR.width > AR.height) {
            
            height = scanRes; //smallest dimension not less than user selected level
            width = (int) Math.round(height * (AR.width * 1.0 / AR.height));
            
        } else if (AR.width < AR.height) {
            
            width = scanRes;
            height = (int) Math.round(width * (AR.height * 1.0 / AR.width));
            
        } else { //1:1
            assert AR.width == AR.height;
            
            width = scanRes;
            height = scanRes;
        }
        
        return new Dimension(width, height);
    }
    
    private Dimension calcLittlePhotoRes() {
        
        AspectRatio AR = (AspectRatio) this.scanTypeOptionComboBox.getSelectedItem();
        int littleImageRes = this.littlePhotoResOptionSlider.getValue();
        int width;
        int height;
        
        if (AR.width > AR.height) {
            height = littleImageRes;
            width = (int) Math.round(height * (AR.width * 1.0 / AR.height));
            
        } else if (AR.width < AR.height) {
            width = littleImageRes;
            height = (int) Math.round(width * (AR.height * 1.0 / AR.width));
            
        } else { //1:1
            assert AR.width == AR.height;

            width = littleImageRes;
            height = littleImageRes;
        }
        
        return new Dimension(width, height);
    }
    
    private Dimension calcOutputRes(boolean cutOffBorder, Dimension scanDim, Dimension littlePhotoDim) {
        
        if (this.sourceImage == null) return new Dimension(-1,-1);
        
        int width;
        int height;
        
        if (cutOffBorder == true) { //cut off border
            
            int numColumns = this.sourceImage.getWidth() / scanDim.width;
            int numRows    = this.sourceImage.getHeight() / scanDim.height;
            
            width  = numColumns * littlePhotoDim.width;
            height = numRows * littlePhotoDim.height;
            
        } else { //retain scale of source image (not same dimension)
            
            width  = (int) (this.sourceImage.getWidth() * littlePhotoDim.width * 1.0 / scanDim.width);
            height = (int) (this.sourceImage.getHeight() * littlePhotoDim.height * 1.0 / scanDim.height);
            
        }
        
        return new Dimension(width, height);
    }
    
    /**
     * Collects relevant information to print to the screen.
     * @return Array of strings of various points of information
     */
    private String[] getInfoStrings() {
        ArrayList<String> strings = new ArrayList<>();
        
        if (this.sourceImage != null)
            strings.add("Source res: " + this.sourceImage.getWidth() + " x " + this.sourceImage.getHeight());
        if (this.outputImage != null)
            strings.add("Output res: " + this.outputImage.getWidth() + " x " + this.outputImage.getHeight());
        if (this.sourceImage != null)
            strings.add("Num output pics: " + this.calcNumLittlePics());
        if (this.usePicturesIndefCheckBox != null) {
            String numToPrint;
            if (this.usePicturesIndefCheckBox.isSelected()) {
                numToPrint = Character.toString( '\u221e'); //infinity character
            } else {
                numToPrint = "" + ((this.usePicturesComboBox.getSelectedIndex() + 1) * this.sm.countNonDisabledEntries());
            }
            strings.add("Num input pics: " + numToPrint);
        }
            
        //more...
        
        
        
        return strings.toArray( new String[strings.size()] );
    }
    
    public static void main(String[] args) {
        
        boolean isTestingMode = false;
        
        //arguments
        System.out.print("Arguments at start: ");
        if (args.length > 0) {
            System.out.println("");
            for (int i = 0; i < args.length; i++)
                System.out.println(i + "\t" + args[i]);
            
            //search if testing dev
            for (String s : args) {
                if (s.toLowerCase().equals("testing"))
                    isTestingMode = true;
            }
        } else {
            System.out.println("[none]");
        }
        System.out.println("");
        
        //L&F
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        //System settings
        System.setProperty("awt.useSystemAAFontSettings", "off");
        System.setProperty("swing.aatext", "true");
        
        //run
        new MainFrame2(isTestingMode).show();
        
    }
    
    //finals
    private final int SEPERATOR_HEIGHT = 8;
    private final LineBorder simpleLineBorder = new LineBorder(Color.WHITE, 1);
    private final LineBorder lightGrayLineBorder = new LineBorder(Color.gray, 1);
    private final int NORMAL_OPTION_PANEL_HEIGHT = 62;
    private final FileNameExtensionFilter PICTURE_FILTER;
    private final String[] PICTURE_TYPES_ACCEPTED = {"jpg", "jpeg", "png", "gif"};

    //non finals (instance)
    protected PrintStream console;
    protected SelectionManager sm;
    protected KeyboardManager km;
    
    private List<AspectRatio> aspectRatios; //list of posible aspect ratios
    private File defaultDir;
    private File picturDir;
    
    private BufferedImage sourceImage;
    private BufferedImage outputImage;
    
    private JFrame fullScreenWindow;
    //private List<SpecialPicture> images; images stored in special pictures in middle panel
    
    private boolean showExtraInformation = true;
    
    private boolean IS_TESTING_DEVELOPMENT = true;
    
    //gui structure
    private JPanel leftPanel;
    private final Dimension leftPanelMaxSize = new Dimension(240, Integer.MAX_VALUE);
    private final Dimension leftPanelPrefSize = new Dimension(240, Integer.MAX_VALUE); 
    private final Dimension leftPanelMinSize = new Dimension(240, Integer.MAX_VALUE);
        private JPanel buttonPanel;
            private JButton addPicturesButton;
            private JButton repaintButton;
            private JButton removeSelectedButton;
            private JButton exportPhotoButton;
            private JButton setSourcePhotoButton;
            private JButton saveProfileButton;
            private JButton openProfileButton;
            private JButton hideShowInfoButton;
            private JButton viewOutputFullscreenButton;
        private JPanel optionsLabelPanel;
            private JLabel optionsLabel;
        private JScrollPane optionsScrollPanel;
            private JPanel optionsPanel;
                private JPanel littlePhotoResOptionPanel;
                    private JLabel littlePhotoResOptionLabel;
                    private JTextField littlePhotoResOptionTextField;
                    private JSlider littlePhotoResOptionSlider;
                private JPanel scanResOptionPanel;
                    private JLabel scanResOptionLabel;
                    private JTextField scanResOptionTextField;
                    private JSlider scanResOptionSlider;
                private JPanel scanTypeOptionPanel;
                    private JLabel scanTypeOptionLabel;
                    private JComboBox scanTypeOptionComboBox;
                    private JButton scanTypeOptionFlipARButton;
                    private JPanel scanTypeOptionARVisual;
                        private final Color SCAN_TYPE_OPTION_AR_VISUAL_COLOR = new Color(200,200,200);
                private JPanel usePicturesOptionPanel;
                    private JLabel usePicturesMainLabel;
                    private JLabel usePicturesLabel2;
                    private JComboBox usePicturesComboBox;
                    private JLabel usePicturesLabel3;
                    private JCheckBox usePicturesIndefCheckBox;
                private JPanel selectPicturesOptionPanel;
                    private JLabel selectPicturesOptionLabel;
                    private ButtonGroup selectPicturesButtonGroup;
                        private JRadioButton selectPicturesBestButton;
                        private JRadioButton selectPicturesWithinRangeButton;
                    private JSlider selectPicturesRangeSlider;
                    private JLabel selectPicturesRangeLabel;
                private JPanel retainOriginalResOptionPanel;
                    private JLabel retainOrigResLabel;
                    private ButtonGroup retainOriginalResButtonGroup;
                        private JRadioButton retainARButton;
                        private JRadioButton onlyWholeImageButton;
                private JPanel sharpenBlurImageOptionPanel;
                        private JLabel sharpenBlurLabel;
                        private JCheckBox sharpenBlurCheckBox;
                        private JLabel sharpenLabel;
                        private JSlider sharpenBlurSlider;
                        private JLabel blurLabel;
        
    private JPanel middlePanel;
    private final Dimension middlePanelMaxSize = new Dimension(300, Integer.MAX_VALUE);
    private final Dimension middlePanelPrefSize = new Dimension(300, Integer.MAX_VALUE);
    private final Dimension middlePanelMinSize = new Dimension(250, Integer.MAX_VALUE);
        private JScrollPane middleScrollPane;
            private JPanel middleScrollingPanel;
                private JPanel middlePictureSelectionPanel;
                private JPanel middleAddPicturesPanel;
        private JLabel middlePanelNoPicturesLabel;
    
    private JPanel rightPanel;
    private final Dimension rightPanelMaxSize = new Dimension(ProjConsts.SCREEN_SIZE.width - 540, Integer.MAX_VALUE);
    private final Dimension rightPanelPrefSize = new Dimension(ProjConsts.SCREEN_SIZE.width - 540, Integer.MAX_VALUE);
        private JSplitPane splitPane;
            private InteractiveImagePanel topPanel;
                private JLabel topPanelNoPicturePlaceholder;
            private InteractiveImagePanel bottomPanel;
                private JLabel bottomPanelPlaceholder;
}
