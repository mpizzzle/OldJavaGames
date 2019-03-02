import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

//* Copyright (c) Mary Percival 2003                          */
/* CaveGame game                      Created December 2003   */

public class CaveGame extends JPanel implements KeyListener, Runnable {
    private static final long serialVersionUID = -4869537669824700402L;
    private static CaveArea area;
    CaveInterface currentCavern;

    // *************************************************************
    public static void main(String[] args) {
        CaveGame caveGame = new CaveGame();

        caveGame.setLayout(null);
        caveGame.setBackground(Color.white);

        area = new CaveArea(caveGame);
        caveGame.add(area);

        area.setVisible(true);
        area.setBounds(0, 0, caveGame.getBounds().width, caveGame.getBounds().height);
        area.setBounds(0, 0, 1000, 1000);

        caveGame.addKeyListener(caveGame);
        area.addKeyListener(caveGame);
        area.requestFocus();
        caveGame.start();
        EventQueue.invokeLater(caveGame);
    }

    public void start() {
        currentCavern = new FirstRoom(this, area);
        currentCavern.display();
        area.repaint();
        System.out.println("CaveGame Started");
    }

    // this class will use just the key pressed event
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            currentCavern = currentCavern.processA();
            currentCavern.display();
            area.repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_B) {
            currentCavern = currentCavern.processB();
            currentCavern.display();
            area.repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            currentCavern = currentCavern.processC();
            currentCavern.display();
            area.repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            start();
            area.repaint();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
}