import com.sun.awt.AWTUtilities;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import java.util.*;
import java.text.*;

import javax.swing.UIManager.*;

import static java.awt.SystemTray.getSystemTray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;
import javax.swing.text.StyleConstants.ColorConstants;

/**
 *
 * Beschreibung
 *
 * @version 1.0 vom 02.05.2012
 * @author juri bieler
 */
public class TimeKeeper extends Thread implements MouseMotionListener,
        MouseListener {
    // Anfang Attribute

    private JFrame f;
    //private JPanel contentPane;
    private JLabel headLine = new JLabel();
    private JButton action = new JButton();
    private JCheckBox tS = new JCheckBox();
    private JTextField time = new JTextField();
    private Uhr timer;
    private Boolean runs = false;
    private JButton save = new JButton();
    private Boolean newStart = true;
    private JTextField actionStr = new JTextField();
    private JButton reset = new JButton();
    private JButton exit = new JButton();
    private JButton mini = new JButton();
    private JButton extend = new JButton();
    private String[] projects;
    private JComboBox projectChoose = new JComboBox();
    private JTextArea trace = new JTextArea();
    URL resource = getClass().getResource("time.png");
    Image image = Toolkit.getDefaultToolkit().getImage(resource);
    final TrayIcon icon = new TrayIcon(image);
    // Ende Attribute

    public TimeKeeper() throws HeadlessException {
        // Frame-Initialisierung

        UIManager.put("nimbusBase", new Color(0, 255, 0));
        UIManager.put("nimbusBlueGrey", new Color(50, 50, 50));
        UIManager.put("control", new Color(0, 255, 0));
        UIManager.put("text", new Color(0, 0, 0));
        
        //UIManager.put("JTextField.background", new Color(0, 255, 0));
        
        f = new JFrame("TimeKeeper 2.0");

        f.setUndecorated(true);
        AWTUtilities.setWindowOpaque(f, false);
        
        
        

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("CATCH");
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        
        /*
         //f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         f.addWindowListener(new WindowAdapter(){
         @Override
         public void windowClosing(WindowEvent evt){
         try{
         timer.interrupt();
         System.exit(0);
         }
         catch (Exception e) {
         System.exit(0);
         }
         System.exit(0);
         }
         });
         */

        int frameWidth = 280;
        int frameHeight = 130;
        f.setSize(frameWidth, frameHeight);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - f.getSize().width) / 2;
        int y = (d.height - f.getSize().height) / 2;
        f.setLocation(x, y);
        f.setResizable(false);
        //Container cp = f.getContentPane();
        //cp.setLayout(null);
        // Anfang Komponenten





        f.setIconImage(image);
        if (SystemTray.isSupported()) {
            //icon = new TrayIcon(image);
            icon.setToolTip("Task Logger");
            icon.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    f.setVisible(true);
                    f.setExtendedState(f.NORMAL);
                    getSystemTray().remove(icon);
                }
            });
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowIconified(WindowEvent e) {
                    f.setVisible(false);
                    try {
                        getSystemTray().add(icon);
                    } catch (AWTException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }

        loadIniData();

        headLine.setBounds(8, 4, 233, 20);
        headLine.setText(" mach mal ne Pause! ;-)");
        headLine.setBackground(new Color(0, 255, 0));//, 120
        headLine.setForeground(Color.black);
        headLine.setOpaque(true);
        headLine.addMouseMotionListener(this);
        headLine.addMouseListener(this);
        f.add(headLine);

        exit.setBounds(245, 0, 15, 15);
        exit.setText("");
        exit.setFont(new Font("Arial", Font.PLAIN, 9));
        exit.setFocusPainted(false);
        exit.setBackground(new Color(255, 0, 0));
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                exit_ActionPerformed(evt);
            }
        });
        f.add(exit);

        mini.setBounds(245, 15, 15, 15);
        mini.setText("");
        mini.setBackground(new Color(125, 0, 125));
        mini.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                mini_ActionPerformed(evt);
            }
        });
        f.add(mini);
        
        extend.setBounds(245, 30, 15, 15);
        extend.setText("");
        extend.setBackground(new Color(0, 125, 125));
        extend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                extend_ActionPerformed(evt);
            }
        });
        f.add(extend);

        projectChoose.setBounds(8, 30, 233, 25);
        projectChoose.addItem("Projekt...");
        for (int i = 0; i < projects.length; i++) {
            projectChoose.addItem(projects[i]);
        }
        f.add(projectChoose);

        tS.setBounds(8, 90, 50, 25);
        tS.setText("T.S.");
        tS.setFocusPainted(false);
        f.add(tS);

        action.setBounds(65, 90, 25, 25);
        action.setText("|>");
        action.setFocusPainted(false);
        action.setMargin(new Insets(2, 2, 2, 2));
        action.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                action_ActionPerformed(evt);
            }
        });
        f.add(action);

        time.setBounds(96, 90, 60, 25);
        time.setText("text");
        time.setOpaque(true);
        time.setBackground(new Color(255, 255, 255));
        //time.setBorder(new LineBorder(Color.black));
        f.add(time);

        save.setBounds(192, 90, 49, 25);
        save.setText("save");
        save.setFocusPainted(false);
        save.setMargin(new Insets(2, 2, 2, 2));
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                save_ActionPerformed(evt);
            }
        });
        f.add(save);

        actionStr.setBounds(8, 60, 233, 25);
        f.add(actionStr);
        
        reset.setBounds(160, 90, 25, 25);
        reset.setText("x");
        reset.setFocusPainted(false);
        reset.setMargin(new Insets(2, 2, 2, 2));
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                reset_ActionPerformed(evt);
            }
        });
        f.add(reset);

        f.add(new JLabel(""));
        // Ende Komponenten

        f.setVisible(true);

        timer = new Uhr(0, 0, 0, false, time);
        timer.start();
    } // end of public TimeKeeper

    public void exit_ActionPerformed(ActionEvent evt) {
        runs = false;
        timer.stopIt();
        System.exit(0);
    }

    public void mini_ActionPerformed(ActionEvent evt) {
        f.setVisible(false);
        try {
            getSystemTray().add(icon);
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }
    
    public void extend_ActionPerformed(ActionEvent evt) {
        //f.setVisible(false);
    	System.out.println("action 3");
    	try {
    		Desktop.getDesktop().open(new File(projectChoose.getSelectedItem() + ".csv"));
    	} catch (Exception ex) {
    		
    	}
    }

    // Anfang Methoden
    public void action_ActionPerformed(ActionEvent evt) {
        if (!runs) {
            startTimer();
        } else {
            stopTimer();
        }
    } // end of action_ActionPerformed

    public void save_ActionPerformed(ActionEvent evt) {
        if (projectChoose.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(f, "bitte w�hle ein Projekt! ;-)", "Projekt?", JOptionPane.OK_OPTION);
            return;
        }
        System.out.println(projectChoose.getSelectedItem());
        saveToLog(projectChoose.getSelectedItem() + ".csv");
    } // end of save_ActionPerformed

    private void saveToLog(String paht) {
        if(runs)stopTimer();
        try {
            File f = new File(paht);
            if (f.exists()) {
                newStart = false;
            }

            BufferedWriter output = new BufferedWriter(new FileWriter(paht, true)); //öffnen der neuen Datei

            //if new File was created, add headLine legend
            if (newStart) {
                output.append("date,time,note");
                output.newLine();
                newStart = false;
            }

            output.append(getDateAsString() + "," + time.getText() + ",\"" + actionStr.getText() + "\"");//schreiben auf die neue Datei
            output.newLine();
            output.close();                   // schließen Bufferwriter
            headLine.setText("gespeichert!");
        } catch (IOException e) {
            System.out.println("Datei konnte nicht erstellt werden!");
            e.printStackTrace();
        }
    }

    public void reset_ActionPerformed(ActionEvent evt) {
        stopTimer();
        timer.setTime(0, 0, 0);
        headLine.setText(" mach mal ne Pause! ;-)");
    }

    public void startTimer() {
        timer.setUseTimeStamp(tS.isSelected());
        runs = true;
        timer.startIt();
        time.setEditable(false);
        action.setText("||");
        headLine.setText("die Zeit läuft...");
    }

    public void stopTimer() {
        runs = false;
        timer.stopIt();
        time.setEditable(true);
        action.setText("|>");
        headLine.setText("Pause...");
    }

    public String getDateAsString() {
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy(HH:mm)");
        return formatter.format(new Date());
    }

    private void loadIniData() {
        Ini iniHandle = new Ini("config.ini");
        String projectString = iniHandle.getData("projects");
        projects = projectString.split(",");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        f.setLocation(e.getXOnScreen() - 115, e.getYOnScreen() - 10);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    // Ende Methoden
    public static void main(String[] args) {
        new TimeKeeper();
    } // end of main
} // end of class time
