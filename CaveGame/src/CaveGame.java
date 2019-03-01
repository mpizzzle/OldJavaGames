//* Copyright (c) Mary Percival 2003                          */
/* CaveGame game                      Created December 2003   */

import  java.awt.*;
import  java.awt.event.*;
import  java.applet.*;

public class CaveGame extends JPanel implements KeyListener {
    
    static boolean finished = false;
    CaveArea area;
    CaveInterface currentCavern;
    
    // *************************************************************
	public void init() {

        setLayout(null);  
        setBackground(Color.white);

        area = new CaveArea(this);
        add(area);
        
        FontMetrics fm = getFontMetrics(getFont());
        area.setVisible(true);
        area.setBounds(0,0,getBounds().width,getBounds().height);

        addKeyListener(this);
        area.addKeyListener(this);
        area.requestFocus();
        start();
	}
    
    public void start() {
        currentCavern = new FirstRoom(this, area);
        currentCavern.display();
        area.repaint();
        showStatus("CaveGame Started");
    }
    
    public void finished() {
        finished = true;
        System.exit(0);
    }
    
    public String getAppletInfo() {
        return ("CaveGame Game");
    } 
    
    // this class will use just the key pressed event
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            currentCavern = currentCavern.processA();
            currentCavern.display();
            area.repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_B)  {
            currentCavern = currentCavern.processB();
            currentCavern.display();
            area.repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_C) {
            currentCavern = currentCavern.processC();
            currentCavern.display();
            area.repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            start();
            area.repaint();
        }
    }
    
    synchronized void die() {
        showStatus("The Player is dead. Press the Enter key to restart");
        finished = true;
    }
    
    public void keyReleased(KeyEvent e) {
    }
    
    public void keyTyped(KeyEvent e) {
    }
    
}