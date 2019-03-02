import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/* Copyright (c) Mary Percival 2003                          */
/* Link game                      Created May 2003          */

public class LinkGame extends JFrame implements KeyListener, Runnable {
    private static final long serialVersionUID = 136423218680421502L;
    // all member variables 'static' because shared with the enemy action thread
    static LinkArea area;
    static boolean finished = false;
    static boolean ingame = false;
    static boolean juststarted = false;
    static int level = 0;
    static Point[] enemyPositions;
    static Point playerPosition;
    static Point[][] obstaclePositions;
    static int direction[];
    static Thread timer;
    static int enemyNumber = 1, enemiesRemaining = 1, obstacleNumber = 1;
    static boolean herocrouching = false;
    static final int MAXLEVELS = 1;
    static final int levellengths[] = new int[MAXLEVELS];
    boolean hasWeaponOut = false;

    static int LEFT = -1;
    static int RIGHT = 1;

    // the next 2 constants are overridden in init once we know the screen size
    static int RIGHTEDGE = 789;
    static int BOTTOMEDGE = 800;
    //
    static Point IMAGESIZE = new Point(152, 107);
    static Point HEROPOS = new Point(63, 28);
    static final int LEFTEDGE = -15;
    static final int TOPEDGE = -10;
    static final int FALLAMOUNT = 16; // amount he falls each time interval
    static final int RISEAMOUNT = 16; // amount he rises each time interval
    static int GROUNDLEVEL = 500;
    static int XAMOUNT = 5;
    static int YAMOUNT = 5;
    static int JUMPAMOUNT = 70;
    static final int X = 1;
    static final int Y = 2;
    static final int NOT = 3;
    static final int DELAY = 60;

    static int heroImageNo = 0;
    static final int OBSTACLEIMAGES = 1;
    static final int HEROIMAGES = 4;
    static Image[] heroImages = new Image[HEROIMAGES];
    static Image[] obstacleImages = new Image[OBSTACLEIMAGES]; // obstacles don't move
    static int obstacleImageNo[];
    static boolean heroleft = false;
    static boolean jumping = false;
    static Point warpzone;
    // static boolean superjump = false;
    static final int JUMPDURATION = 5;
    static final int SUPERJUMPDURATION = 8;
    static final int LEFTOFOBSTACLE = 27;
    static final int RIGHTOFOBSTACLE = 33;
    static int[] keysdown = new int[4];
    static final int LEFTDOWN = 0;
    static final int RIGHTDOWN = 1;
    static final int DOWNDOWN = 2;
    static final int SPACEDOWN = 3;

    static final int OBSTACLEHEIGHT = 42;
    boolean onTheWayUp = false;
    static int startPosY = GROUNDLEVEL;

    public static void main(String[] args) {
        LinkGame linkGame = new LinkGame();

        linkGame.setLayout(null);
        linkGame.setBackground(Color.white);
        linkGame.setSize(RIGHTEDGE, BOTTOMEDGE);

        area = new LinkArea(linkGame);
        linkGame.add(area);
        XAMOUNT = 12;
        YAMOUNT = 12;
        //RIGHTEDGE = (linkGame.getBounds().width / XAMOUNT) * XAMOUNT + LEFTEDGE;
        //BOTTOMEDGE = linkGame.getBounds().height - 1;
        linkGame.setVisible(true);
        //area.setBounds(0, 0, linkGame.getBounds().width, BOTTOMEDGE);
        area.setBounds(0, 0, RIGHTEDGE, BOTTOMEDGE);
        area.setVisible(true);

        linkGame.addKeyListener(linkGame);
        area.addKeyListener(linkGame);
        area.requestFocus();

        levellengths[0] = RIGHTEDGE;
        obstacleImages[0] = area.obstacle;

        timer = new Thread(linkGame);
        timer.start();
    }

    synchronized void faceRight() {
        heroImages[0] = area.herostand;
        heroImages[1] = area.hero1;
        heroImages[2] = area.herostand;
        heroImages[3] = area.hero2;
        heroImageNo = 0;
        heroleft = false;
    }

    synchronized void faceLeft() {
        heroImages[0] = area.herostandleft;
        heroImages[1] = area.hero1left;
        heroImages[2] = area.herostandleft;
        heroImages[3] = area.hero2left;
        heroImageNo = 0;
        heroleft = true;
    }

    synchronized void getSwordOut() {
        hasWeaponOut = true;
        if (heroleft) {
            heroImages[0] = area.heroswordleft;
            heroImages[1] = area.heroswordleft;
            heroImages[2] = area.heroswordleft;
            heroImages[3] = area.heroswordleft;
        } else {
            heroImages[0] = area.heroswordright;
            heroImages[1] = area.heroswordright;
            heroImages[2] = area.heroswordright;
            heroImages[3] = area.heroswordright;
        }
        heroImageNo = 0;
    }

    synchronized void getShieldOut() {
        hasWeaponOut = true;
        if (heroleft) {
            heroImages[0] = area.heroshieldleft;
            heroImages[1] = area.heroshieldleft;
            heroImages[2] = area.heroshieldleft;
            heroImages[3] = area.heroshieldleft;
        } else {
            heroImages[0] = area.heroshieldright;
            heroImages[1] = area.heroshieldright;
            heroImages[2] = area.heroshieldright;
            heroImages[3] = area.heroshieldright;
        }
        heroImageNo = 0;
    }

    synchronized void putWeaponAway() {
        hasWeaponOut = false;
        if (heroleft)
            faceLeft();
        else
            faceRight();
    }

    synchronized void standStill() {
        heroImageNo = 0;
    }

    public void doLevel(int level) {
        // this runs one 'level'
        juststarted = true;

        System.out.println("Started level " + level);

        // position Hero at the bottom left
        playerPosition = new Point(LEFTEDGE, GROUNDLEVEL);
        faceRight();

        // initialise obstacle states
        obstacleImageNo = new int[obstacleNumber];
        obstacleImageNo[0] = 0;

        // initialise the arrays of enemies, etc
        enemyPositions = new Point[enemyNumber];
        obstaclePositions = new Point[MAXLEVELS][obstacleNumber];
        direction = new int[enemyNumber];
        for (int i = 0; i < enemyNumber; i++) {
            enemyPositions[i] = new Point(RIGHTEDGE, GROUNDLEVEL);
            direction[i] = LEFT;
        }
        for (int i = 0; i < obstacleNumber; i++) {
            switch (level) {
            case 1:
                obstaclePositions[level - 1][i] = new Point((RIGHTEDGE + 15) / 2, GROUNDLEVEL);
                System.out.println("Obstacle positioned at (" + (RIGHTEDGE - LEFTEDGE) / 2 + ", " + GROUNDLEVEL + ")");
                break;
            default:
            }
        }

        // position the warp zone at the end of the level
        warpzone = new Point(levellengths[level - 1], GROUNDLEVEL);

        // paint the panel here
        // area.repaint();
        ingame = true;
    }

    Point randomPosition() {
        return new Point((int) (Math.random() * RIGHTEDGE) / XAMOUNT * XAMOUNT,
                (int) (Math.random() * BOTTOMEDGE) / YAMOUNT * YAMOUNT);
    }

    boolean isPlayerPosition(Point p) {
        return (p.x == playerPosition.x && p.y == playerPosition.y);
    }

    boolean isEnemyPosition(Point p) {
        for (int i = 0; i < enemyNumber; i++) {
            if (p.x == enemyPositions[i].x && p.y == enemyPositions[i].y)
                return (true);
        }
        return (false);
    }

    boolean isObstaclePosition(Point p) {
        for (int i = 0; i < obstacleNumber; i++) {
            if (isObstacleX(p) != -1 && isObstacleY(p) != -1) {
                return (true);
            }
        }
        return (false);
    }

    boolean checkIfBlockingObstacle(Point p) {
        boolean result = false;
        for (int i = 0; i < obstacleNumber; i++) {
            if (isObstacleX(p) != -1 && isBlockingObstacleY(p) != -1) {
                // piranhaPopping[i] = false;
                result = true;
            }
            // else piranhaPopping[i] = true;
        }
        if (!result)
            startPosY = GROUNDLEVEL;
        return (result);
    }

    int isObstacleX(Point p) {
        int obstaclex;
        for (int i = 0; i < obstacleNumber; i++) {
            // if x is >= obstacleposition - (LEFTOFOBSTACLE) and x <= obstacleposition +
            // (RIGHTOFOBSTACLE)
            obstaclex = obstaclePositions[level - 1][i].x;
            if (p.x >= (obstaclex - LEFTOFOBSTACLE) && p.x <= (obstaclex + RIGHTOFOBSTACLE))
                return (i); // return the number of the matching obstacle
        }
        return (-1);
    }

    int isBlockingObstacleY(Point p) {
        for (int i = 0; i < obstacleNumber; i++) {
            if (p.y <= obstaclePositions[level - 1][i].y - OBSTACLEHEIGHT)
                return (i);
        }
        return (-1);
    }

    int isObstacleY(Point p) {
        for (int i = 0; i < obstacleNumber; i++) {
            if (p.y > obstaclePositions[level - 1][i].y - OBSTACLEHEIGHT)
                return (i);
        }
        return (-1);
    }

    public void finished() {
        finished = true;
        System.exit(0);
    }

    int randomMove() {
        // return + or - value (randomly)
        if (Math.random() >= 0.5)
            return (24);
        else
            return (-24);
    }

    private synchronized void incKeysDown(int whichone) {
        keysdown[whichone] = 1;
    }

    private synchronized void decKeysDown(int whichone) {
        keysdown[whichone] = 0;
    }

    // this class will use just the key pressed event
    @Override
    public void keyPressed(KeyEvent e) {
        if (ingame) {
            if (!((e.getKeyCode() == KeyEvent.VK_LEFT && playerPosition.x <= (LEFTEDGE + XAMOUNT))
                    || (e.getKeyCode() == KeyEvent.VK_RIGHT && playerPosition.x >= (RIGHTEDGE - XAMOUNT)))) {
                // valid move: move the player and then move the enemies
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    herocrouching = true;
                    incKeysDown(DOWNDOWN);
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    incKeysDown(LEFTDOWN);
                    leftPressed();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    incKeysDown(RIGHTDOWN);
                    rightPressed();
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    // System.out.println("Jump when jumping is " + jumping);
                    if (!jumping) {
                        // incKeysDown(SPACEDOWN);
                        startPosY = playerPosition.y;
                        jumping = true;
                        onTheWayUp = true;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_Z) {
                    getSwordOut();
                } else if (e.getKeyCode() == KeyEvent.VK_X) {
                    getShieldOut();
                }
                if (isEnemyPosition(playerPosition)) {
                    System.out.println("The Player ran into an enemy!! Press the Enter key to restart the level");
                    ingame = false;
                }
                // area.repaint();
                // System.out.println("Hero's position = (" + playerPosition.x + ", " +
                // playerPosition.y + ")");
                System.out.println(" ");
            } // if valid key press
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            doLevel(++level);
        }
    }

    void leftPressed() {
        if (!herocrouching) {
            // superjump = true; // if jumping
            // if Hero was facing right, turn him left
            if (!heroleft && !hasWeaponOut)
                faceLeft();
            else {
                // if the target position is not occupied by a obstacle...
                for (int i = 0; i < obstacleNumber; i++) {
                    if (isObstaclePosition(new Point(playerPosition.x - XAMOUNT, playerPosition.y)))
                        return; // can't move
                }
                playerPosition.x -= XAMOUNT;
                incrementHeroImage();
            }
        }
    }

    void rightPressed() {
        if (!herocrouching) {
            // superjump = true; // if jumping
            // if Hero was facing left, turn him right
            if (heroleft && !hasWeaponOut)
                faceRight();
            else {
                for (int i = 0; i < obstacleNumber; i++) {
                    if (isObstaclePosition(new Point(playerPosition.x + XAMOUNT, playerPosition.y)))
                        return; // can't move
                }
                playerPosition.x += XAMOUNT;
                incrementHeroImage();
            }
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
                area.repaint();
                juststarted = false;
            }
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
            }
            if (ingame) {
                // System.out.println("Run: BEFORE: Hero's position = (" + playerPosition.x + ",
                // " + playerPosition.y + ")");
                if (keysdown[LEFTDOWN] == 0 && keysdown[RIGHTDOWN] == 0
                        && keysdown[DOWNDOWN] == 0 /*
                                                    * && keysdown[SPACEDOWN] == 0
                                                    */)
                    standStill();

                if (keysdown[LEFTDOWN] > 0)
                    leftPressed();
                else if (keysdown[RIGHTDOWN] > 0)
                    rightPressed();

                // for each obstacle, cycle through the piranha pictures
                for (int i = 0; i < obstacleNumber; i++) {
                    incrementObstacleImage(i);
                }

                /*
                 * // for each enemy, move the enemy in the direction it was going for (int i=0;
                 * i < enemyPositions.length; i++) { if (!deadHero(enemyPositions[i])) {
                 * enemyPositions[i] = move(enemyPositions[i], direction[i]); } // if there is a
                 * obstacle at that position, fall into it and decrement count of enemies // and
                 * set that enemy's position to -1, -1 if
                 * (isObstaclePosition(enemyPositions[i])) {
                 * System.out.println("A enemy fell into a obstacle");
                 * 
                 * enemyPositions[i] = new Point(-1, -1); enemiesRemaining--;
                 * System.out.println(enemyNumber-enemiesRemaining + " down, " +
                 * enemiesRemaining + " to go..."); } // if player is caught by a enemy or all
                 * enemies are gone, end the game if (isPlayerPosition(enemyPositions[i])) {
                 * System.out.
                 * println("\"Mmm! Brains...\". The Player is dead. Press the Enter key to restart the level"
                 * ); level--; // because it will be incremented in a minute and we want to stay
                 * on the same 'level' ingame = false; } } if (enemiesRemaining == 0) {
                 * System.out.
                 * println("All the enemies are gone -- you won!! Press the Enter key to start the next level"
                 * ); enemyNumber++; if (obstacleNumber > 2) obstacleNumber--; ingame = false; }
                 */
                if (onTheWayUp) {
                    // System.out.println("OnTheWayUp. Start Y Position =" + startPosY);
                    if (playerPosition.y > startPosY - JUMPAMOUNT)
                        playerPosition = new Point(playerPosition.x, playerPosition.y - RISEAMOUNT);
                    else {
                        onTheWayUp = false;
                    }
                } else {
                    int obstacleno = isObstacleX(playerPosition);
                    if (obstacleno == -1) { // no obstacle at this position
                        if (onTheWayUp == false) {
                            if (playerPosition.y < startPosY)
                                playerPosition = new Point(playerPosition.x, playerPosition.y + FALLAMOUNT);
                            else
                                jumping = false;
                        }
                    } else {
                        Point obstaclepos = obstaclePositions[level - 1][obstacleno];
                        // if hero's y position <= obstacle's height
                        if (playerPosition.y <= (obstaclepos.y - OBSTACLEHEIGHT)) { // can't land on the obstacle if
                                                                                    // just < (??!)
                            // allow hero to land on the obstacle
                            // System.out.println("Allow Hero to land (or stay) on obstacle");
                            jumping = false;
                            playerPosition = new Point(playerPosition.x, obstaclepos.y - OBSTACLEHEIGHT);
                            // if the piranha was up, Hero dies
                            if (obstacleImages[obstacleImageNo[obstacleno]] != area.obstacle) {
                                System.out.println("Hero dies!!");
                                ingame = false;

                            }
                            /*
                             * else { // otherwise stop this Piranha piranhaPopping[obstacleno] = false; }
                             */
                        } else {
                            if (onTheWayUp == false) {
                                if (playerPosition.y < startPosY)
                                    playerPosition = new Point(playerPosition.x, playerPosition.y + FALLAMOUNT);
                                else
                                    jumping = false;
                            }
                        }
                    }
                }
                checkIfBlockingObstacle(playerPosition);
                // System.out.println("Run: AFTER: Hero's position = (" + playerPosition.x + ",
                // " + playerPosition.y + ")");
                area.repaint();
            }
        }
    }

    synchronized void incrementObstacleImage(int i) {
        obstacleImageNo[i]++;
        if (obstacleImageNo[i] == OBSTACLEIMAGES)
            obstacleImageNo[i] = 0;
    }

    synchronized void incrementHeroImage() {
        heroImageNo++;
        if (heroImageNo == HEROIMAGES)
            heroImageNo = 0;
    }

    Point move(Point start, int direction) {
        return (new Point(start.x + direction, start.y));
    }

    boolean deadHero(Point p) {
        return (p.x == -1 && p.y == -1);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        boolean needrepaint = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            herocrouching = false;
            decKeysDown(DOWNDOWN);
            needrepaint = true;
        }
        /*
         * else if (e.getKeyCode() == KeyEvent.VK_SPACE) { decKeysDown(SPACEDOWN); }
         */
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            decKeysDown(LEFTDOWN);
            // superjump = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            decKeysDown(RIGHTDOWN);
            // superjump = false;
        } else
            putWeaponAway();
        // if (needrepaint) area.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    synchronized Image getHeroImage() {
        return (heroImages[heroImageNo]);
    }

    synchronized Image getObstacleImage(int obstaclenum) {
        return (obstacleImages[obstacleImageNo[obstaclenum]]);
    }

}
