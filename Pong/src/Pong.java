
/* Copyright (c) Mike Percival 2004                          */
/* Pong game                      Created April 2004       */

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Pong extends JFrame implements KeyListener, Runnable {
    private static final long serialVersionUID = 1018529427970832700L;
    // all member variables 'static' because shared with the ball action thread
    private static PongArea area;
    private static boolean ingame = false;
    private boolean juststarted = false;
    private static int level = 1;
    protected static Point[] ballPositions;
    protected static Point playerPosition;
    private static Thread balls;

    protected static int ballNumber = 1;
    protected static int RIGHTEDGE = 800;
    protected static final int LEFTEDGE = 0;
    protected static final int TOPEDGE = 0;
    protected static int BOTTOMEDGE = 800;
    protected static int BALL_XAMOUNT = 3;
    protected static int BALL_YAMOUNT = 3;
    protected static int PLAYER_XAMOUNT = 12;
    protected static int PLAYER_YAMOUNT = 12;
    private static final int DELAY = 15;
    boolean[] goingLeft, goingRight, goingUp, goingDown;
    boolean deadball = false;
    int timeSinceLastBall = 0;

    public static void main(String[] args) {
        Pong pong = new Pong();

        pong.setLayout(null);
        pong.setBackground(Color.black);
        pong.setSize(RIGHTEDGE, BOTTOMEDGE);
        Pong.area = new PongArea(pong);
        pong.add(area);
        //RIGHTEDGE = Pong.getBounds().width - 1;
        //BOTTOMEDGE = Pong.getBounds().height - 1;
        pong.setVisible(true);
        area.setBounds(0, 0, RIGHTEDGE, BOTTOMEDGE);
        area.setVisible(true);

        pong.addKeyListener(pong);
        area.addKeyListener(pong);
        area.requestFocus();

        balls = new Thread(pong);
        balls.start();
    }

    public void doLevel(int level) {
        // this runs one 'level'
        // create arrays of ball balls here.
        // Maybe each level will have different numbers?
        juststarted = true;
        deadball = false;
        ballNumber = 1;
        ballPositions = new Point[ballNumber];
        goingLeft = new boolean[ballNumber];
        goingRight = new boolean[ballNumber];
        goingUp = new boolean[ballNumber];
        goingDown = new boolean[ballNumber];

        for (int i = 0; i < ballNumber; i++) {
            ballPositions[i] = new Point(0, 0);
            goingLeft[i] = true;
            goingRight[i] = false;
            goingUp[i] = false;
            goingDown[i] = true;
        }

        // System.out.println("Started level " + level);

        // randomly position 1 player, x balls
        playerPosition = new Point(RIGHTEDGE - 20, 0);

        for (int i = 0; i < ballNumber; i++) {
            ballPositions[i] = randomPosition();
        }

        // paint the panel here
        area.repaint();
        ingame = true;
    }

    private Point randomPosition() {
        Point p = new Point((int) (Math.random() * RIGHTEDGE) / BALL_XAMOUNT * BALL_XAMOUNT, (int) 0);
        if (p.x >= RIGHTEDGE * 2 / 3)
            p.x = p.x - RIGHTEDGE / 3;

        return (p);
    }

    synchronized void addABall() {
        System.out.println("ADD A BALL!");
        Point[] newBallPos = new Point[ballNumber + 1];
        boolean[] newgl = new boolean[ballNumber + 1], newgr = new boolean[ballNumber + 1],
                newgu = new boolean[ballNumber + 1], newgd = new boolean[ballNumber + 1];

        for (int i = 0; i < ballNumber; i++) {
            newBallPos[i] = new Point(ballPositions[i]);
            newgl[i] = goingLeft[i];
            newgr[i] = goingRight[i];
            newgu[i] = goingUp[i];
            newgd[i] = goingDown[i];
        }

        newBallPos[ballNumber] = randomPosition();
        newgl[ballNumber] = true;
        newgr[ballNumber] = false;
        newgu[ballNumber] = false;
        newgu[ballNumber] = true;
        ballPositions = newBallPos;
        goingLeft = newgl;
        goingRight = newgr;
        goingUp = newgu;
        goingDown = newgd;
        ballNumber++;
    }

    void seeIfBallHitWallFloorOrCeiling(int ballno) {
        if (ballPositions[ballno].x <= 0) { // ball hit the left wall
            goingRight[ballno] = true;
            goingLeft[ballno] = false;
        } else if (ballPositions[ballno].y <= 0) {// ball hit the ceiling
            goingUp[ballno] = false;
            goingDown[ballno] = true;
        } else if (ballPositions[ballno].y >= BOTTOMEDGE - BALL_YAMOUNT) {// ball hit the floor
            goingDown[ballno] = false;
            goingUp[ballno] = true;
        } else if (ballPositions[ballno].x >= RIGHTEDGE) { // ball reached the right edge
            deadball = true;
            level--; // because it will be incremented in a minute and we want to stay on the same
                     // 'level'
            ingame = false;
            System.out.println("LOZER!");
        }
    }

    private boolean isPlayerPosition(Point p) {
        return (p.x >= playerPosition.x && p.y >= playerPosition.y && p.y <= playerPosition.y + 154);
    }

    private Point moveBall(int ballno, Point original) {
        Point result = new Point(original);
        if (goingLeft[ballno])
            result.x = result.x - BALL_XAMOUNT;
        else if (goingRight[ballno])
            result.x = result.x + BALL_XAMOUNT;

        if (goingUp[ballno])
            result.y = result.y - BALL_YAMOUNT;
        else if (goingDown[ballno])
            result.y = result.y + BALL_YAMOUNT;
        if (isPlayerPosition(result)) {
            goingRight[ballno] = false;
            goingLeft[ballno] = true;
        } else
            seeIfBallHitWallFloorOrCeiling(ballno);

        return (result);
    }

    // this class will use just the key pressed event
    @Override
    public void keyPressed(KeyEvent e) {
        if (ingame) {
            if (!((e.getKeyCode() == KeyEvent.VK_DOWN && playerPosition.y >= (BOTTOMEDGE - PLAYER_YAMOUNT))
                    || (e.getKeyCode() == KeyEvent.VK_UP && playerPosition.y <= (TOPEDGE + PLAYER_YAMOUNT)))) {
                // valid move: move the player and then move the balls
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    playerPosition.y += PLAYER_YAMOUNT;
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    playerPosition.y -= PLAYER_YAMOUNT;
                }

                for (int i = 0; i < ballNumber; i++) {
                    if (isPlayerPosition(ballPositions[i])) {
                        goingRight[i] = false;
                        goingLeft[i] = true;
                    }
                }
                area.repaint();
            } // if valid key press
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            doLevel(level++);
        }
    }

    @Override
    public void run() {
        while (true) {
            if (juststarted) {
                try {
                    Thread.sleep(750);
                } catch (Exception e) {
                }
                juststarted = false;
            }

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
            }

            if (ingame) {
                timeSinceLastBall += DELAY;
                // System.out.println(String.valueOf(timeSinceLastBall));
                if (timeSinceLastBall >= 10000) {
                    addABall();
                    timeSinceLastBall = 0;
                }

                // for each ball, move the ball closer to the player
                for (int i = 0; i < ballPositions.length; i++) {
                    if (!deadball) {
                        ballPositions[i] = moveBall(i, ballPositions[i]);
                    }
                    if (isPlayerPosition(ballPositions[i])) {
                        goingRight[i] = false;
                        goingLeft[i] = true;
                    }
                }
                area.repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
