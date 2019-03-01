
/* Copyright (c) M Percival 2002                          */
/* Zombie game                      Created October 2002     */

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class ZombieGame extends JFrame implements KeyListener, Runnable {
    private static final long serialVersionUID = 8654912377879331966L;
    // all member variables 'static' because shared with the zombie action thread
    private static ZombieArea area;
    private static boolean ingame = false;
    private boolean juststarted = false;
    private static int level = 1;
    private static int zombiesRemaining;
    protected static Point[] zombiePositions;
    protected static Point playerPosition;
    protected static Point[] pitPositions;
    private static Thread zombies;

    protected static int pitNumber = 15;
    protected static int zombieNumber = 10;
    protected static int RIGHTEDGE = 1200;
    protected static final int LEFTEDGE = 0;
    protected static final int TOPEDGE = 0;
    protected static int BOTTOMEDGE = 1000;
    protected static int XAMOUNT = 5;
    protected static int YAMOUNT = 5;
    private static final int X = 1;
    private static final int Y = 2;
    private static final int NOT = 3;
    private static final int DELAY = 125;
    protected int explosionx = -1, explosiony = -1;

    public static void main(String[] args) {
        ZombieGame zombieGame = new ZombieGame();
        zombieGame.setLayout(null);
        zombieGame.setBackground(Color.gray);
	zombieGame.setSize(RIGHTEDGE, BOTTOMEDGE);

        area = new ZombieArea(zombieGame);
        zombieGame.add(area);
        // RIGHTEDGE = getBounds().width - 1;
        // BOTTOMEDGE = getBounds().height - 1;
        XAMOUNT = 12;
        YAMOUNT = 12;
        zombieGame.setVisible(true);
        area.setBounds(0, 0, RIGHTEDGE, BOTTOMEDGE);
        area.setVisible(true);

        zombieGame.addKeyListener(zombieGame);
        area.addKeyListener(zombieGame);
        area.requestFocus();

        zombies = new Thread(zombieGame);
        zombies.start();
    }

    public void doLevel(int level) {
        // this runs one 'level'
        // create arrays of pits and zombies here.
        // Maybe each level will have different numbers?
        juststarted = true;
        Point temppoint;
        pitPositions = new Point[pitNumber];
        zombiePositions = new Point[zombieNumber];
        for (int i = 0; i < zombieNumber; i++)
            zombiePositions[i] = new Point(0, 0);
        for (int i = 0; i < pitNumber; i++)
            pitPositions[i] = new Point(0, 0);

        System.out.println("Started level " + level + ": " + zombieNumber + " zombies and " + pitNumber + " pits");

        // randomly position 1 player, x zombies and y pits
        playerPosition = randomPosition();

        for (int i = 0; i < zombieNumber; i++) {
            temppoint = randomPosition();
            while (isPlayerPosition(temppoint) || isZombiePosition(temppoint)) {
                // don't position any zombie on the player or on top of another zombie
                temppoint = randomPosition();
            }
            zombiePositions[i] = temppoint;
        }

        for (int i = 0; i < pitNumber; i++) {
            temppoint = randomPosition();
            while (isPlayerPosition(temppoint) || isPitPosition(temppoint) || isZombiePosition(temppoint)) {
                // don't position any zombie at the player, a zombie or another pit
                temppoint = randomPosition();
            }

            pitPositions[i] = temppoint;
        }

        zombiesRemaining = zombieNumber;
        // paint the panel here
        area.repaint();
        ingame = true;
    }

    private Point randomPosition() {
        return new Point((int) (Math.random() * RIGHTEDGE) / XAMOUNT * XAMOUNT,
                (int) (Math.random() * BOTTOMEDGE) / YAMOUNT * YAMOUNT);
    }

    private boolean isPlayerPosition(Point p) {
        return (p.x == playerPosition.x && p.y == playerPosition.y);
    }

    private boolean isZombiePosition(Point p) {
        for (int i = 0; i < zombieNumber; i++) {
            if (p.x == zombiePositions[i].x && p.y == zombiePositions[i].y)
                return (true);
        }
        return (false);
    }

    private int isInline(Point p) {
        // is the new position in the same row as the player?
        if (Math.abs(p.x - playerPosition.x) < XAMOUNT) {
            // see whether there is another zombie between this position and the player
            if (playerPosition.y > p.y) {
                for (int i = 0; i < zombieNumber; i++) {
                    if (p.x == zombiePositions[i].x && p.y < zombiePositions[i].y)
                        return (X);
                }
            } else { // playerPosition y < p.y
                for (int i = 0; i < zombieNumber; i++) {
                    if (p.x == zombiePositions[i].x && p.y > zombiePositions[i].y)
                        return (X);
                }
            }
        } else if (Math.abs(p.y - playerPosition.y) < YAMOUNT) {
            // see whether there is another zombie between this position and the player
            if (playerPosition.x > p.x) {
                for (int i = 0; i < zombieNumber; i++) {
                    if (p.y == zombiePositions[i].y && p.x < zombiePositions[i].x)
                        return (Y);
                }
            } else { // playerPosition x < p.x
                for (int i = 0; i < zombieNumber; i++) {
                    if (p.y == zombiePositions[i].y && p.x > zombiePositions[i].x)
                        return (Y);
                }
            }
        }
        return (NOT);
    }

    private boolean isPitPosition(Point p) {
        for (int i = 0; i < pitNumber; i++) {
            if (p.x == pitPositions[i].x && p.y == pitPositions[i].y)
                return (true);
        }

        return (false);
    }

    private Point moveCloser(Point original, Point target) {
        Point result = new Point(original);
        int inlineType = isInline(original);
        if (inlineType != X) {
            if (original.x < target.x)
                result.x = original.x + (XAMOUNT / 2);
            else if (original.x > target.x)
                result.x = original.x - (XAMOUNT / 2);
        } else {
            // move out of the Y alignment - randomly left or right
            result.x += randomMove();
        }
        if (inlineType != Y) {
            if (original.y < target.y)
                result.y = original.y + (YAMOUNT / 2);
            else if (original.y > target.y)
                result.y = original.y - (YAMOUNT / 2);
        } else {
            // move out of the Y alignment - randomly up or down
            result.y += randomMove();
        }
        if (isZombiePosition(result)) {
            if (isZombiePosition(new Point(result.x, original.y))) {
                if (isZombiePosition(new Point(original.x, result.y)))
                    result = original;
                else
                    result = new Point(original.x, result.y);
            } else
                result = new Point(result.x, original.y);
        }
        return (result);
    }

    private int randomMove() {
        // return + or - value (randomly)
        if (Math.random() >= 0.5)
            return (24);
        else
            return (-24);
    }

    // this class will use just the key pressed event
    @Override
    public void keyPressed(KeyEvent e) {
        if (ingame) {
            if (!((e.getKeyCode() == KeyEvent.VK_DOWN && playerPosition.y >= (BOTTOMEDGE - YAMOUNT))
                    || (e.getKeyCode() == KeyEvent.VK_UP && playerPosition.y <= (TOPEDGE + YAMOUNT))
                    || (e.getKeyCode() == KeyEvent.VK_LEFT && playerPosition.x <= (LEFTEDGE + XAMOUNT))
                    || (e.getKeyCode() == KeyEvent.VK_RIGHT && playerPosition.x >= (RIGHTEDGE - XAMOUNT)))) {
                // valid move: move the player and then move the zombies
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    playerPosition.y += YAMOUNT;
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    playerPosition.y -= YAMOUNT;
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    playerPosition.x -= XAMOUNT;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    playerPosition.x += XAMOUNT;
                }
                if (isZombiePosition(playerPosition)) {
                    System.out.println("The Player ran into a zombie!! Press the Enter key to restart the level");
                    ingame = false;
                }
                if (isPitPosition(playerPosition)) {
                    System.out.println("The Player ran into a pit!! Press the Enter key to restart the level");
                    ingame = false;
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
                // for each zombie, move the zombie closer to the player
                for (int i = 0; i < zombiePositions.length; i++) {
                    if (!deadZombie(zombiePositions[i])) {
                        zombiePositions[i] = moveCloser(zombiePositions[i], playerPosition);
                    }
                    // if there is a pit at that position, fall into it and decrement count of
                    // zombies
                    // and set that zombie's position to -1, -1
                    if (isPitPosition(zombiePositions[i])) {
                        System.out.println("A zombie fell into a pit");
                        explosionx = zombiePositions[i].x;
                        explosiony = zombiePositions[i].y;

                        zombiePositions[i] = new Point(-1, -1);
                        zombiesRemaining--;
                        System.out
                                .println(zombieNumber - zombiesRemaining + " down, " + zombiesRemaining + " to go...");
                    }
                    // if player is caught by a zombie or all zombies are gone, end the game
                    if (isPlayerPosition(zombiePositions[i])) {
                        System.out.println(
                                "\"Mmm! Brains...\". The Player is dead. Press the Enter key to restart the level");
                        level--; // because it will be incremented in a minute and we want to stay on the same
                                 // 'level'
                        ingame = false;
                    }
                }
                if (zombiesRemaining == 0) {
                    System.out.println(
                            "All the zombies are gone -- you won!! Press the Enter key to start the next level");
                    zombieNumber++;
                    if (pitNumber > 2)
                        pitNumber--;
                    ingame = false;
                } else
                    area.repaint();
            }
        }
    }

    private boolean deadZombie(Point p) {
        return (p.x == -1 && p.y == -1);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
