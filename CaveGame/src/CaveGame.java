import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

//* Copyright (c) Mary Percival 2003                          */
/* CaveGame game                      Created December 2003   */

public class CaveGame extends JFrame implements KeyListener, Runnable {
    private static final long serialVersionUID = -4869537669824700402L;
    private static final int WIDTH = 613;
    private static final int HEIGHT = 482;
    private static CaveArea area;
    CaveInterface currentCavern;
    static Thread timer;

    // *************************************************************
    public static void main(String[] args) {
        CaveGame caveGame = new CaveGame();

        caveGame.setLayout(null);
        caveGame.setBackground(Color.white);
        caveGame.setSize(WIDTH, HEIGHT);
        caveGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        caveGame.setTitle("Cave Game");

        area = new CaveArea(caveGame);
        caveGame.add(area);

        caveGame.setVisible(true);
        //area.setBounds(0, 0, caveGame.getBounds().width, caveGame.getBounds().height);
        area.setBounds(0, 0, WIDTH, HEIGHT);
        area.setVisible(true);

        caveGame.addKeyListener(caveGame);
        area.addKeyListener(caveGame);
        area.requestFocus();
        caveGame.currentCavern = new FirstRoom(caveGame, area);
        caveGame.currentCavern.display();
        timer = new Thread(caveGame);
        timer.start();
    }

    // this class will use just the key pressed event
    @Override
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
            currentCavern = new FirstRoom(this, area);
            currentCavern.display();
            area.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void run() {
    }
}