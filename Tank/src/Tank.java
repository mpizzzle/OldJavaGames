import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/* Copyright (c) Mary Percival 2002                          */
/* Tank game                      Created October 2002     */

public class Tank extends JFrame implements KeyListener, Runnable {
    private static final long serialVersionUID = 2750159877444817519L;
    // all member variables 'static' because shared with the enemy action thread
    private static TankArea area;
    private static boolean ingame = false;
    private boolean juststarted = false;
    private static int level = 1;
    private static int enemiesRemaining;
    protected static Point[] enemyPositions;
    protected static Point playerPosition;
    protected static Point[] pitPositions;
    private static Thread enemies;

    protected static int pitNumber = 15;
    protected static int enemyNumber = 1;
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

    // tank game stuff
    boolean up = true, down = false, left = false, right = false, shooting = false;
    static int NOTSHOOTING = 0;
    static int SHOOTING = 1;
    static int FIRSTUP = 0;
    static int FIRSTDOWN = 2;
    static int FIRSTLEFT = 4;
    static int FIRSTRIGHT = 6;
    int goodImageNo = FIRSTUP; // TODO - randomise
    int[] enemyImageNo;
    boolean[] enemyShooting;
    Image[][] goodImages = new Image[8][2]; // up up2 down down2 left left2 right right2 x SHOOTING and NON_SHOOTING
    Image[][] badImages = new Image[8][2]; // up up2 down down2 left left2 right right2 x SHOOTING and NON_SHOOTING

    public static void main(String[] args) {
        Tank tank = new Tank();
        tank.setLayout(null);
        tank.setBackground(Color.gray);
        tank.setSize(RIGHTEDGE, BOTTOMEDGE);

        area = new TankArea(tank);
        tank.add(area);
        //RIGHTEDGE = tank.getBounds().width - 1;
        //BOTTOMEDGE = tank.getBounds().height - 1;
        XAMOUNT = 12;
        YAMOUNT = 12;
        tank.setVisible(true);
        area.setBounds(0, 0, RIGHTEDGE, BOTTOMEDGE);
        area.setVisible(true);

        tank.addKeyListener(tank);
        area.addKeyListener(tank);
        area.requestFocus();

        tank.goodImages[FIRSTUP][NOTSHOOTING] = area.tankup;
        tank.goodImages[FIRSTUP + 1][NOTSHOOTING] = area.tankup2;
        tank.goodImages[FIRSTUP][SHOOTING] = area.tankupshoot;
        tank.goodImages[FIRSTUP + 1][SHOOTING] = area.tankupshoot2;

        tank.goodImages[FIRSTDOWN][NOTSHOOTING] = area.tankdown;
        tank.goodImages[FIRSTDOWN + 1][NOTSHOOTING] = area.tankdown2;
        tank.goodImages[FIRSTDOWN][SHOOTING] = area.tankdownshoot;
        tank.goodImages[FIRSTDOWN + 1][SHOOTING] = area.tankdownshoot2;

        tank.goodImages[FIRSTLEFT][NOTSHOOTING] = area.tankleft;
        tank.goodImages[FIRSTLEFT + 1][NOTSHOOTING] = area.tankleft2;
        tank.goodImages[FIRSTLEFT][SHOOTING] = area.tankleftshoot;
        tank.goodImages[FIRSTLEFT + 1][SHOOTING] = area.tankleftshoot2;
        
        tank.goodImages[FIRSTRIGHT][NOTSHOOTING] = area.tankright;
        tank.goodImages[FIRSTRIGHT + 1][NOTSHOOTING] = area.tankright2;
        tank.goodImages[FIRSTRIGHT][SHOOTING] = area.tankrightshoot;
        tank.goodImages[FIRSTRIGHT + 1][SHOOTING] = area.tankrightshoot2;

        tank.badImages[FIRSTUP][NOTSHOOTING] = area.badtankup;
        tank.badImages[FIRSTUP + 1][NOTSHOOTING] = area.badtankup2;
        tank.badImages[FIRSTUP][SHOOTING] = area.badtankupshoot;
        tank.badImages[FIRSTUP + 1][SHOOTING] = area.badtankupshoot2;

        tank.badImages[FIRSTDOWN][NOTSHOOTING] = area.badtankdown;
        tank.badImages[FIRSTDOWN + 1][NOTSHOOTING] = area.badtankdown2;
        tank.badImages[FIRSTDOWN][SHOOTING] = area.badtankdownshoot;
        tank.badImages[FIRSTDOWN + 1][SHOOTING] = area.badtankdownshoot2;

        tank.badImages[FIRSTLEFT][NOTSHOOTING] = area.badtankleft;
        tank.badImages[FIRSTLEFT + 1][NOTSHOOTING] = area.badtankleft2;
        tank.badImages[FIRSTLEFT][SHOOTING] = area.badtankleftshoot;
        tank.badImages[FIRSTLEFT + 1][SHOOTING] = area.badtankleftshoot2;

        tank.badImages[FIRSTRIGHT][NOTSHOOTING] = area.badtankright;
        tank.badImages[FIRSTRIGHT + 1][NOTSHOOTING] = area.badtankright2;
        tank.badImages[FIRSTRIGHT][SHOOTING] = area.badtankrightshoot;
        tank.badImages[FIRSTRIGHT + 1][SHOOTING] = area.badtankrightshoot2;

        enemies = new Thread(tank);
        enemies.start();
    }

    public void doLevel(int level) {
        // this runs one 'level'
        // create arrays of pits and enemies here.
        // Maybe each level will have different numbers?
        juststarted = true;
        Point temppoint;
        pitPositions = new Point[pitNumber];
        enemyPositions = new Point[enemyNumber];
        for (int i = 0; i < enemyNumber; i++)
            enemyPositions[i] = new Point(0, 0);
        for (int i = 0; i < pitNumber; i++)
            pitPositions[i] = new Point(0, 0);

        enemyImageNo = new int[enemyNumber];
        enemyShooting = new boolean[enemyNumber];
        for (int i = 0; i < enemyNumber; i++) {
            enemyShooting[i] = false;
            enemyImageNo[i] = FIRSTUP; // TODO - randomise
        }
        System.out.println("Started level " + level + ": " + enemyNumber + " enemies and " + pitNumber + " pits");

        // randomly position 1 player, x enemies and y pits
        playerPosition = randomPosition();

        for (int i = 0; i < enemyNumber; i++) {
            temppoint = randomPosition();
            while (isPlayerPosition(temppoint) || isTankPosition(temppoint)) {
                // don't position any enemy on the player or on top of another enemy
                temppoint = randomPosition();
            }
            enemyPositions[i] = temppoint;
        }

        for (int i = 0; i < pitNumber; i++) {
            temppoint = randomPosition();
            while (isPlayerPosition(temppoint) || isPitPosition(temppoint) || isTankPosition(temppoint)) {
                // don't position any enemy at the player, an enemy or another pit
                temppoint = randomPosition();
            }
            pitPositions[i] = temppoint;
        }

        enemiesRemaining = enemyNumber;
        // paint the panel here
        area.repaint();
        ingame = true;
    }

    synchronized void incrementImageNo() {
        goodImageNo++;
        if ((goodImageNo % 2) == 0)
            goodImageNo -= 2;
    }

    synchronized void incrementImageNo(int enemyNo) {
        enemyImageNo[enemyNo]++;
        if ((enemyImageNo[enemyNo] % 2) == 0)
            enemyImageNo[enemyNo] -= 2;
    }

    void goLeft(int enemyno) {
        if (enemyImageNo[enemyno] == FIRSTLEFT || enemyImageNo[enemyno] == FIRSTLEFT + 1) {
            incrementImageNo(enemyno);
        } else
            enemyImageNo[enemyno] = FIRSTLEFT;
    }

    void goLeft() {
        if (goodImageNo == FIRSTLEFT || goodImageNo == FIRSTLEFT + 1) {
            incrementImageNo();
        } else
            goodImageNo = FIRSTLEFT;
    }

    void goRight(int enemyno) {
        if (enemyImageNo[enemyno] == FIRSTRIGHT || enemyImageNo[enemyno] == FIRSTRIGHT + 1) {
            incrementImageNo(enemyno);
        } else
            enemyImageNo[enemyno] = FIRSTRIGHT;
    }

    void goRight() {
        if (goodImageNo == FIRSTRIGHT || goodImageNo == FIRSTRIGHT + 1) {
            incrementImageNo();
        } else
            goodImageNo = FIRSTRIGHT;
    }

    void goUp(int enemyno) {
        if (enemyImageNo[enemyno] == FIRSTUP || enemyImageNo[enemyno] == FIRSTUP + 1) {
            incrementImageNo(enemyno);
        } else
            enemyImageNo[enemyno] = FIRSTUP;
    }

    void goUp() {
        if (goodImageNo == FIRSTUP || goodImageNo == FIRSTUP + 1) {
            incrementImageNo();
        } else
            goodImageNo = FIRSTUP;
    }

    void goDown(int enemyno) {
        if (enemyImageNo[enemyno] == FIRSTDOWN || enemyImageNo[enemyno] == FIRSTDOWN + 1) {
            incrementImageNo(enemyno);
        } else
            enemyImageNo[enemyno] = FIRSTDOWN;
    }

    void goDown() {
        if (goodImageNo == FIRSTDOWN || goodImageNo == FIRSTDOWN + 1) {
            incrementImageNo();
        } else
            goodImageNo = FIRSTDOWN;
    }

    private Point randomPosition() {
        return new Point((int) (Math.random() * RIGHTEDGE) / XAMOUNT * XAMOUNT,
                (int) (Math.random() * BOTTOMEDGE) / YAMOUNT * YAMOUNT);
    }

    private boolean isPlayerPosition(Point p) {
        return (p.x == playerPosition.x && p.y == playerPosition.y);
    }

    private boolean isTankPosition(Point p) {
        for (int i = 0; i < enemyNumber; i++) {
            if (p.x == enemyPositions[i].x && p.y == enemyPositions[i].y)
                return (true);
        }
        return (false);
    }

    private int isInline(Point p) {
        // is the new position in the same row as the player?
        if (Math.abs(p.x - playerPosition.x) < XAMOUNT) {
            // see whether there is another enemy between this position and the player
            if (playerPosition.y > p.y) {
                for (int i = 0; i < enemyNumber; i++) {
                    if (p.x == enemyPositions[i].x && p.y < enemyPositions[i].y)
                        return (X);
                }
            } else { // playerPosition y < p.y
                for (int i = 0; i < enemyNumber; i++) {
                    if (p.x == enemyPositions[i].x && p.y > enemyPositions[i].y)
                        return (X);
                }
            }
        } else if (Math.abs(p.y - playerPosition.y) < YAMOUNT) {
            // see whether there is another enemy between this position and the player
            if (playerPosition.x > p.x) {
                for (int i = 0; i < enemyNumber; i++) {
                    if (p.y == enemyPositions[i].y && p.x < enemyPositions[i].x)
                        return (Y);
                }
            } else { // playerPosition x < p.x
                for (int i = 0; i < enemyNumber; i++) {
                    if (p.y == enemyPositions[i].y && p.x > enemyPositions[i].x)
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

    private Point moveCloser(int enemyNo, Point original, Point target) {
        Point result = new Point(original);
        int inlineType = isInline(original);
        // allowed to move EITHER X or Y but not both
        if (Math.random() >= 0.5) {
            // move in X direction
            if (inlineType != X) {
                if (original.x < target.x) {
                    result.x = original.x + (XAMOUNT / 2);
                    goRight(enemyNo);
                } else if (original.x > target.x) {
                    result.x = original.x - (XAMOUNT / 2);
                    goLeft(enemyNo);
                }
            }
        } else {
            // move in Y direction
            if (inlineType != Y) {
                if (original.y < target.y) {
                    result.y = original.y + (YAMOUNT / 2);
                    goDown(enemyNo);
                } else if (original.y > target.y) {
                    result.y = original.y - (YAMOUNT / 2);
                    goUp(enemyNo);
                }
            }
        }
        /*
         * if (isTankPosition(result)) { if (isTankPosition(new Point(result.x,
         * original.y))) { if (isTankPosition(new Point(original.x, result.y))) result =
         * original; else result = new Point(original.x, result.y); } else result = new
         * Point(result.x, original.y); }
         */
        inlineType = isInline(result);
        if (inlineType == X) {
            // fire along the X axis
        } else if (inlineType == Y) {
            // fire along the Y axis
        }
        return (result);
    }

    /*
     * private int randomMove() { // return + or - value (randomly) if
     * (Math.random() >= 0.5) return(24); else return(-24); }
     */

    // this class will use just the key pressed event
    @Override
    public void keyPressed(KeyEvent e) {
        if (ingame) {
            if (!((e.getKeyCode() == KeyEvent.VK_DOWN && playerPosition.y >= (BOTTOMEDGE - YAMOUNT))
                    || (e.getKeyCode() == KeyEvent.VK_UP && playerPosition.y <= (TOPEDGE + YAMOUNT))
                    || (e.getKeyCode() == KeyEvent.VK_LEFT && playerPosition.x <= (LEFTEDGE + XAMOUNT))
                    || (e.getKeyCode() == KeyEvent.VK_RIGHT && playerPosition.x >= (RIGHTEDGE - XAMOUNT)))) {
                // valid move: move the player and then move the enemies
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    playerPosition.y += YAMOUNT;
                    goDown();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    playerPosition.y -= YAMOUNT;
                    goUp();
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    playerPosition.x -= XAMOUNT;
                    goLeft();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    playerPosition.x += XAMOUNT;
                    goRight();
                }
                if (isTankPosition(playerPosition)) {
                    System.out.println("The Player ran into an enemy!! Press the Enter key to restart the level");
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
                // for each enemy, move the enemy closer to the player
                for (int i = 0; i < enemyPositions.length; i++) {
                    if (!deadTank(enemyPositions[i])) {
                        enemyPositions[i] = moveCloser(i, enemyPositions[i], playerPosition);
                    }
                    // if there is a pit at that position, fall into it and decrement count of
                    // enemies
                    // and set that enemy's position to -1, -1
                    if (isPitPosition(enemyPositions[i])) {
                        System.out.println("an enemy fell into a pit");
                        explosionx = enemyPositions[i].x;
                        explosiony = enemyPositions[i].y;

                        enemyPositions[i] = new Point(-1, -1);
                        enemiesRemaining--;
                        System.out.println(enemyNumber - enemiesRemaining + " down, " + enemiesRemaining + " to go...");
                    }
                    // if player is caught by an enemy or all enemies are gone, end the game
                    if (isPlayerPosition(enemyPositions[i])) {
                        System.out.println(
                                "\"Mmm! Brains...\". The Player is dead. Press the Enter key to restart the level");
                        level--; // because it will be incremented in a minute and we want to stay on the same
                                 // 'level'
                        ingame = false;
                    }
                }
                if (enemiesRemaining == 0) {
                    System.out.println(
                            "All the enemies are gone -- you won!! Press the Enter key to start the next level");
                    enemyNumber++;
                    if (pitNumber > 2)
                        pitNumber--;
                    ingame = false;
                } else
                    area.repaint();
            }
        }
    }

    private boolean deadTank(Point p) {
        return (p.x == -1 && p.y == -1);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
