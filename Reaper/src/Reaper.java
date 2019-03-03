import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

//* Copyright (c) Mary Percival 2003                          */
/* Reaper game                      Created May 2003          */

public class Reaper extends JFrame implements KeyListener, Runnable {
    private static final long serialVersionUID = -3998951090286400255L;
    boolean easymode = true;
    static int MAXLEVEL = 1;
    static int level = 1;

    // the levels so far are:
    // 1 beginner level: 4 fire hydrants & 1 runner

    static int LEFT = 1;
    static int RIGHT = 0;
    // the next 2 constants are overridden in init once we know the screen size
    static int RIGHTEDGE = 789;
    static int BOTTOMEDGE = 900;
    static int PAGEWIDTH = 741;

    static Point IMAGESIZE = new Point(152, 107);
    static final int LEFTEDGE = 0;
    static final int TOPEDGE = -10;
    static int GROUNDLEVEL = 503;
    static int startPosY;
    static int JUMPAMOUNT = 70;
    static int BOUNCEAMOUNT = 48;
    static final int FALLAMOUNT = 16; // amount he falls each time interval
    static final int RISEAMOUNT = 16; // amount he rises each time interval
    static final int XAMOUNT = 12;

    static final int X = 1;
    static final int Y = 2;
    static final int NOT = 3;
    static final int DELAY = 50;

    static final int OBSTACLEIMAGES = 1; // static obstacles - unlike reaper obstacles
    static final int HEROIMAGES = 4;
    static final int PREYIMAGES = 3;

    // ----------------------------------------------------------------------------------------------
    // PREY TYPES
    // ----------------------------------------------------------------------------------------------
    static final int PREYTYPES = 1; // runner
    static final int RUNNER = 0;

    // prey positions (height)
    static final int RUNNERHEIGHT = GROUNDLEVEL - 20;
    static int preyHeight[] = new int[PREYTYPES];

    static final int preySize[] = { 27 };
    static final int preyTopSpace[] = { 4 };

    static final int SPACE_FROM_X_TO_REAPER = 55;
    static final int REAPERHEIGHT = 86;
    static final int REAPEROFFSET = 21;
    static final int REAPERCROUCHOFFSET = 39;
    static final int REAPERSTANDINGSIZE = 65;
    static final int REAPERCROUCHINGSIZE = 45;

    static final int JUMPDURATION = 5;
    static final int SUPERJUMPDURATION = 8;
    static final int REAPERLEFTOFOBSTACLE = 45; // fudge factor because Reaper image is diff size to obstacle image
    static final int REAPERRIGHTOFOBSTACLE = 10; // fudge factor because Reaper image is diff size to obstacle image
    static final int PREYLEFTOFOBSTACLE = 33; // fudge factor for prey images
    static final int PREYRIGHTOFOBSTACLE = 7; // >49 causes panic!
    static final int REAPERLEFTOFPREY = 73;
    static final int REAPERRIGHTOFPREY = -23;

    static final int LEFTDOWN = 0;
    static final int RIGHTDOWN = 1;
    static final int DOWNDOWN = 2;
    static final int SPACEDOWN = 3;

    static final int OBSTACLEHEIGHT = 42;
    static final int TALLOBSTACLEDIFF = 28;
    static final int OBSTACLEWIDTH = 40;
    int GROUNDDIFF = 605 - GROUNDLEVEL;

    // all member variables 'static' because shared with the prey action thread
    static ReaperArea area;
    static Thread timer;
    static boolean finished = false;
    static boolean ingame = false;
    static boolean juststarted = true;
    static Point warpzone;
    static int[] keysdown = new int[4];
    static Point[] preyPositions;
    static Point playerPosition;
    static Point[] obstaclePositions;
    static boolean[] piranhaPopping; // one for each obstacle
    static boolean[] hasPiranha; // one for each obstacle
    static boolean[] isTall; // one for each obstacle
    static int preyDirection[];
    static int preyNumber = 1, preyRemaining = 1, obstacleNumber = 1;
    static boolean reapercrouching = false;
    static boolean onTheWayUp = false;
    static boolean bouncing = false;
    static boolean wasbouncing = false;
    static boolean dead = false;
    static int page = 0;
    static int reaperImageNo = 0;
    static int preyImageNo[];
    static int levellength = 1482;
    static Image[] reaperImages = new Image[HEROIMAGES];
    static Image[] obstacleImages = new Image[OBSTACLEIMAGES]; // obstacle, piranhapeep, piranha peepy etc
    static Image[] squished = new Image[PREYTYPES]; // squished images for each prey type
    static Image[][][] preyImages = new Image[PREYTYPES][2][PREYIMAGES]; // left right left right or whatever
    static int obstacleImageNo[]; // what is the current image for obstacle n
    static Image[][] preyImage = new Image[PREYTYPES][PREYIMAGES];// what is the current image for prey n?
    static Image floors[] = new Image[MAXLEVEL];
    static int preyType[]; // what is the type of prey n?
    static boolean preyDead[]; // is prey n dead?
    static boolean reaperleft = false;
    static boolean jumping = false;
    static int preyMove[] = new int[PREYTYPES];
    boolean somethingChangedSinceRepaint = false;

    // *************************************************************
    public static void main(String[] args) {
        Reaper reaper = new Reaper();

        reaper.setLayout(null);
        reaper.setBackground(Color.white);
        reaper.setSize(RIGHTEDGE, BOTTOMEDGE);

        area = new ReaperArea(reaper);
        reaper.add(area);
        //RIGHTEDGE = (Reaper.getBounds().width / XAMOUNT) * XAMOUNT + LEFTEDGE;
        //BOTTOMEDGE = Reaper.getBounds().height - 1;
        reaper.setVisible(true);
        area.setBounds(0, 0, RIGHTEDGE, BOTTOMEDGE);
        //GROUNDLEVEL = Reaper.getBounds().height - Reaper.GROUNDDIFF;
        startPosY = GROUNDLEVEL;
        //ReaperArea.FLOORLEVEL = Reaper.getBounds().height - ReaperArea.FLOORDIFF;
        area.setVisible(true);

        reaper.addKeyListener(reaper);
        area.addKeyListener(reaper);
        area.requestFocus();

        for (int i = 0; i < OBSTACLEIMAGES; i++) {
            obstacleImages[i] = area.obstacle;
        }

        preyImages[RUNNER][RIGHT][0] = area.runner;
        preyImages[RUNNER][RIGHT][1] = area.runner2;
        preyImages[RUNNER][LEFT][0] = area.runnerleft;
        preyImages[RUNNER][LEFT][1] = area.runner2left;

        preyMove[RUNNER] = 5;
        preyHeight[RUNNER] = 40;
        squished[RUNNER] = area.squishedrunner;

        // for (int i=0; i < MAXLEVEL; i++) {
        // floors[i] = area.floor4;
        // }

        timer = new Thread(reaper);
        timer.start();

    }

    synchronized void faceRight() {
        reaperImages[0] = area.reaperstand;
        reaperImages[1] = area.reaper1;
        reaperImages[2] = area.reaperstand;
        reaperImages[3] = area.reaper2;
        reaperImageNo = 0;
        reaperleft = false;
    }

    synchronized void faceLeft() {
        reaperImages[0] = area.reaperstandleft;
        reaperImages[1] = area.reaper1left;
        reaperImages[2] = area.reaperstandleft;
        reaperImages[3] = area.reaper2left;
        reaperImageNo = 0;
        reaperleft = true;
    }

    synchronized void preyFaceRight(int preyno) {
        preyImage[preyno][0] = preyImages[preyType[preyno]][RIGHT][0];
        preyImage[preyno][1] = preyImages[preyType[preyno]][RIGHT][1];
        preyDirection[preyno] = RIGHT;
    }

    synchronized void preyFaceLeft(int preyno) {
        preyImage[preyno][0] = preyImages[preyType[preyno]][LEFT][0];
        preyImage[preyno][1] = preyImages[preyType[preyno]][LEFT][1];
        preyDirection[preyno] = LEFT;
    }

    synchronized void preyDie(int preyno) {
        // displayStatus();
        preyImage[preyno][0] = squished[preyType[preyno]];
        preyImage[preyno][1] = squished[preyType[preyno]];
    }

    synchronized void standStill() {
        reaperImageNo = 0;
    }

    public void doLevel(int level) {
        // this runs one 'level'
        juststarted = true;
        dead = false;

        // position reaper at the bottom left
        playerPosition = new Point(LEFTEDGE, GROUNDLEVEL);
        faceRight();

        // *********** THIS STUFF DEPENDS WHAT LEVEL YOU ARE ON ************
        switch (level) {
        case 1:
            obstacleNumber = 4; // 4 obstacles for level 1
            preyNumber = 1;
            levellength = 1482; // 2 pages
            break;
        default:
            System.out.println("Level " + level + " has not yet been implemented");
        }
        preyImage = new Image[preyNumber][PREYIMAGES];// what is the current image for prey n?
        // initialise piranha states
        obstacleImageNo = new int[obstacleNumber];
        hasPiranha = new boolean[obstacleNumber];
        piranhaPopping = new boolean[obstacleNumber];
        isTall = new boolean[obstacleNumber];

        // initialise the arrays of prey, etc
        preyPositions = new Point[preyNumber];
        preyType = new int[preyNumber];
        preyDead = new boolean[preyNumber];
        obstaclePositions = new Point[obstacleNumber];
        preyDirection = new int[preyNumber];
        preyImageNo = new int[preyNumber];

        for (int i = 0; i < preyNumber; i++) {
            preyImageNo[i] = 0;
        }

        switch (level) {
        // -----------------------------------------------
        // LEVEL 1
        // -----------------------------------------------
        case 1:
            for (int i = 0; i < obstacleNumber; i++) {
                isTall[i] = false;
                piranhaPopping[i] = false;
                hasPiranha[i] = false;
            }
            obstaclePositions[0] = new Point(400, GROUNDLEVEL);
            obstaclePositions[1] = new Point(950, GROUNDLEVEL);
            obstaclePositions[2] = new Point(1245, GROUNDLEVEL); // 1255 for adjacent
            obstaclePositions[3] = new Point(1320, GROUNDLEVEL);

            preyPositions[0] = new Point(1000, RUNNERHEIGHT);
            preyType = new int[] { RUNNER };
            break;
        // -----------------------------------------------
        // LEVEL 2
        // -----------------------------------------------
        default:

        }

        for (int j = 0; j < preyNumber; j++) {
            preyFaceRight(j); // in this game all the 'prey' are fleeing to the right
            preyDead[j] = false;
        }

        for (int i = 0; i < obstacleNumber; i++) {
            if (hasPiranha[i]) { // pick a random image number between 0 and 18 (inclusive)
                obstacleImageNo[i] = (int) (Math.random() * 18) / 1;
            } else
                obstacleImageNo[i] = 0;
        }

        // position the warp zone at the end of the level
        warpzone = new Point(levellength, GROUNDLEVEL);
        // *********** END OF STUFF THAT DEPENDS WHAT LEVEL YOU ARE ON ************

        ingame = true;
        System.out.println("Started level " + level);
    }

    boolean isPlayerPosition(Point p) {
        return (p.x == playerPosition.x && p.y == playerPosition.y);
    }

    int isEnemyPosition(Point p) {
        for (int i = 0; i < preyNumber; i++) {
            if (isEnemyX(p) != -1 && isEnemyY(p) != -1 && !preyDead[i])
                return (i);
        }
        return (-1);
    }

    boolean isObstaclePosition(Point p, boolean forReaper) {
        int obby;
        for (int i = 0; i < obstacleNumber; i++) {
            obby = isObstacleX(p, forReaper);
            if (obby != -1 && isObstacleY(p, obby, forReaper) != -1) {
                return (true);
            }
        }
        return (false);
    }

    boolean checkIfBlockingObstacle(Point p) {
        boolean result = false;
        int obby;
        obby = isObstacleX(p, true);
        if (obby != -1 && isBlockingObstacleY(p, obby)) {
            piranhaPopping[obby] = false;
            result = true;
        }
        for (int i = 0; i < obstacleNumber; i++) {
            if (i != obby || !result) {
                if (hasPiranha[i])
                    piranhaPopping[i] = true;
            }
        }
        if (!result)
            startPosY = GROUNDLEVEL;
        return (result);
    }

    int isObstacleX(Point p, boolean forReaper) {
        int obstaclex;
        for (int i = 0; i < obstacleNumber; i++) {
            // if x is >= obstacleposition - (REAPERLEFTOFOBSTACLE) and x <=
            // obstacleposition + (REAPERRIGHTOFOBSTACLE)
            obstaclex = obstaclePositions[i].x;
            if (forReaper) {
                if (p.x >= (obstaclex - REAPERLEFTOFOBSTACLE) && p.x <= (obstaclex + REAPERRIGHTOFOBSTACLE))
                    return (i); // return the number of the matching obstacle
            } else {
                if (p.x >= (obstaclex - PREYLEFTOFOBSTACLE) && p.x <= (obstaclex + PREYRIGHTOFOBSTACLE))
                    return (i); // return the number of the matching obstacle
            }
        }
        return (-1);
    }

    boolean isBlockingObstacleY(Point p, int obstaclenumber) {
        // String s = "Is Reaper blocking obstacle " + obstaclenumber + " which is " +
        // (isTall[obstaclenumber] ? "" : "not ") + "tall";
        if (isTall[obstaclenumber]) {
            if (p.y <= obstaclePositions[obstaclenumber].y - (OBSTACLEHEIGHT + TALLOBSTACLEDIFF)) {
                // System.out.println(s+": Yes");
                return (true);
            }
        } else {
            if (p.y <= obstaclePositions[obstaclenumber].y - OBSTACLEHEIGHT) {
                // System.out.println(s+": Yes");
                return (true);
            }
        }
        // System.out.println(s+": No");
        return (false);
    }

    int isObstacleY(Point p, int obstaclenumber, boolean forReaper) {
        if (isTall[obstaclenumber]) {
            if (p.y > obstaclePositions[obstaclenumber].y - (OBSTACLEHEIGHT + TALLOBSTACLEDIFF)) {
                return (obstaclenumber);
            }
        } else {
            if (p.y > obstaclePositions[obstaclenumber].y - OBSTACLEHEIGHT) {
                return (obstaclenumber);
            }
        }
        return (-1);
    }

    int isEnemyX(Point p) {
        int preyx;
        for (int i = 0; i < preyNumber; i++) {
            // if x is >= preyposition - (LEFTOFPREY) and x <= preyposition + (RIGHTOFPREY)
            preyx = preyPositions[i].x;
            // System.out.println("Player got by prey if " +
            // p.x + " >= " + (preyx - REAPERLEFTOFPREY) + " && " + p.x + " <= " + (preyx +
            // REAPERRIGHTOFPREY));
            if (p.x >= (preyx - REAPERLEFTOFPREY) && p.x <= (preyx + REAPERRIGHTOFPREY) && !preyDead[i])
                return (i); // return the number of the matching prey
        }
        return (-1);
    }

    int isEnemyY(Point p) {
        int offset = reapercrouching ? REAPERCROUCHOFFSET : REAPEROFFSET;
        for (int i = 0; i < preyNumber; i++) {
            if (!preyDead[i] && ((p.y + offset <= preyPositions[i].y + preySize[preyType[i]])
                    && (p.y + REAPERHEIGHT) >= preyPositions[i].y))
                return (i);
        }
        return (-1);
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
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            displayStatus();
        }
        if (ingame) {
            if (!(e.getKeyCode() == KeyEvent.VK_LEFT && playerPosition.x <= (LEFTEDGE + XAMOUNT))) {

                // valid move: move the player and then move the prey
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    reapercrouching = true;
                    incKeysDown(DOWNDOWN);
                    setSomethingChangedSinceRepaint(true);
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    incKeysDown(LEFTDOWN);
                    leftPressed(false);
                    setSomethingChangedSinceRepaint(true);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    incKeysDown(RIGHTDOWN);
                    rightPressed(false);
                    setSomethingChangedSinceRepaint(true);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (!jumping) {
                        startPosY = playerPosition.y;
                        jumping = true;
                        onTheWayUp = true;
                        setSomethingChangedSinceRepaint(true);
                    }
                }
                if (somethingChangedSinceRepaint) {
                    area.repaint();
                }
            } // if valid key press
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (level == MAXLEVEL || dead)
                doLevel(level);
            else
                doLevel(++level);
            page = 0;
            area.repaint();
        }
    }

    /* synchronized */ void setSomethingChangedSinceRepaint(boolean val) {
        somethingChangedSinceRepaint = val;
    }

    void leftPressed(boolean skid) {
        if (!reapercrouching) {
            // if Hero was facing right, turn him left
            if (!reaperleft)
                faceLeft();
            else {
                // if the target position is not occupied by a obstacle...
                for (int i = 0; i < obstacleNumber; i++) {
                    if (isObstaclePosition(new Point(playerPosition.x - XAMOUNT, playerPosition.y), true))
                        return; // can't move
                }
                playerPosition.x -= XAMOUNT;
                if (!skid)
                    incrementHeroImage();
            }
        }
    }

    void rightPressed(boolean skid) {
        if (!reapercrouching) {
            if ((playerPosition.x + SPACE_FROM_X_TO_REAPER) >= (warpzone.x + 4)) {
                // level complete!
                ingame = false;
            } else {
                // if Hero was facing left, turn him right
                if (reaperleft)
                    faceRight();
                else {
                    for (int i = 0; i < obstacleNumber; i++) {
                        if (isObstaclePosition(new Point(playerPosition.x + XAMOUNT, playerPosition.y), true))
                            return; // can't move
                    }
                    playerPosition.x += XAMOUNT;
                    // System.out.println("Reaper's x position is " + playerPosition.x);
                    if (!skid)
                        incrementHeroImage();
                }
            }
        }
    }

    boolean anythingVisibleChanged() {
        return (somethingChangedSinceRepaint);
    }

    @Override
    public void run() {
        boolean odd = true; // only change piranha image every other cycle
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
                if (keysdown[LEFTDOWN] == 0 && keysdown[RIGHTDOWN] == 0 && keysdown[DOWNDOWN] == 0)
                    standStill();

                if (keysdown[LEFTDOWN] > 0) {
                    leftPressed(false);
                    setSomethingChangedSinceRepaint(true);
                } else if (keysdown[RIGHTDOWN] > 0) {
                    rightPressed(false);
                    setSomethingChangedSinceRepaint(true);
                }

                if (!odd) {
                    // for each obstacle, cycle through the piranha pictures
                    for (int i = 0; i < obstacleNumber; i++) {
                        if (piranhaPopping[i]) {
                            incrementObstacleImage(i);
                            if (isVisibleObstacle(i))
                                setSomethingChangedSinceRepaint(true);
                        }
                    }
                }
                odd = !odd;

                // for each prey, move the prey in the direction it was going
                for (int i = 0; i < preyNumber; i++) {
                    // TODO if there is a fatal obstacle at that position, fall into it and
                    // decrement count of prey
                    // and set that prey's position to -1, -1
                    /*
                     * if (isObstaclePosition(preyPositions[i])) {
                     * System.out.println("A prey fell into a obstacle");
                     * 
                     * preyPositions[i] = new Point(-1, -1); preyRemaining--;
                     * System.out.println(preyNumber-preyRemaining + " down, " + preyRemaining +
                     * " to go..."); }
                     */
                    // if there is a non-fatal obstacle
                    if (isObstaclePosition(preyPositions[i], false)) {
                        // change direction
                        changeDirection(i);
                        if (isVisibleEnemy(i))
                            setSomethingChangedSinceRepaint(true);
                    }
                    if (!preyDead[i]) {
                        preyPositions[i] = move(i, preyPositions[i], preyDirection[i], odd);
                        if (isVisibleEnemy(i))
                            setSomethingChangedSinceRepaint(true);
                    }

                    // if player is caught by a prey, end the game
                    int preyno = isEnemyPosition(playerPosition);
                    if (preyno > -1) {
                        int NEEDTOBEABOVE = preyPositions[preyno].y + preyTopSpace[preyno] - REAPERHEIGHT;
                        if (easymode)
                            NEEDTOBEABOVE = preyPositions[preyno].y - preyHeight[preyType[preyno]];
                        if ((!onTheWayUp && (playerPosition.y <= NEEDTOBEABOVE))) {
                            // player may have squished an prey
                            preyDead[preyno] = true;
                            preyDie(preyno);
                            // player bounces
                            bouncing = true;
                            startPosY = playerPosition.y;
                            setSomethingChangedSinceRepaint(true);
                        } else {
                            int offset = reapercrouching ? REAPERCROUCHOFFSET : REAPEROFFSET;
                            if (playerPosition.y > NEEDTOBEABOVE && (playerPosition.y
                                    + offset <= preyPositions[preyno].y + preySize[preyType[preyno]])) {
                                die();
                                setSomethingChangedSinceRepaint(true);
                            }
                        }
                    }
                }
                if (!dead) {
                    if (onTheWayUp) {
                        if (playerPosition.y > startPosY - JUMPAMOUNT) {
                            playerPosition = new Point(playerPosition.x, playerPosition.y - RISEAMOUNT);
                            setSomethingChangedSinceRepaint(true);
                        } else {
                            onTheWayUp = false;
                        }
                    } else if (bouncing) {
                        if (playerPosition.y > startPosY - BOUNCEAMOUNT) {
                            playerPosition = new Point(playerPosition.x, playerPosition.y - RISEAMOUNT);
                            setSomethingChangedSinceRepaint(true);
                        } else {
                            bouncing = false;
                            wasbouncing = true;
                        }
                    } else {
                        int obstacleno = isObstacleX(playerPosition, true);
                        if (obstacleno == -1) { // no obstacle at this position
                            if (!onTheWayUp && !bouncing) {
                                if (playerPosition.y < startPosY) {
                                    playerPosition = new Point(playerPosition.x, playerPosition.y + FALLAMOUNT);
                                    wasbouncing = false;
                                    setSomethingChangedSinceRepaint(true);
                                } else
                                    jumping = false;
                            }
                        } else {
                            Point obstaclepos = obstaclePositions[obstacleno];
                            // if reaper's y position <= obstacle's height
                            int thisObstacleHeight = isTall[obstacleno] ? OBSTACLEHEIGHT + TALLOBSTACLEDIFF
                                    : OBSTACLEHEIGHT;
                            if (playerPosition.y <= (obstaclepos.y - thisObstacleHeight) && // can't land on the
                                                                                            // obstacle if just < (??!)
                                    (playerPosition.y + FALLAMOUNT >= (obstaclepos.y - thisObstacleHeight))) {
                                // allow reaper to land on the obstacle
                                jumping = false;
                                if (playerPosition.y != obstaclepos.y - thisObstacleHeight)
                                    setSomethingChangedSinceRepaint(true);
                                playerPosition = new Point(playerPosition.x, obstaclepos.y - thisObstacleHeight);
                                // if the piranha was up, Hero dies
                                if (obstacleImages[obstacleImageNo[obstacleno]] != area.obstacle) {
                                    die();
                                } else {
                                    // otherwise stop this Piranha
                                    piranhaPopping[obstacleno] = false;
                                }
                            } else {
                                if (!onTheWayUp && !bouncing) {
                                    if (playerPosition.y < startPosY) {
                                        setSomethingChangedSinceRepaint(true);
                                        playerPosition = new Point(playerPosition.x, playerPosition.y + FALLAMOUNT);
                                        wasbouncing = false;
                                    } else
                                        jumping = false;
                                }
                            }
                        }
                    }
                    if (!dead) {
                        checkIfBlockingObstacle(playerPosition);
                        if (playerPosition.y > GROUNDLEVEL)
                            playerPosition.y = GROUNDLEVEL;
                    }
                    if (anythingVisibleChanged()) {
                        area.repaint();
                    }
                } else {
                    if (!area.showingMessage)
                        area.repaint();
                }
            }
        }
    }

    boolean isVisibleObstacle(int i) {
        // TODO - this probably needs fixing when obstacle is near page boundary
        return ((obstaclePositions[i].x >= page * PAGEWIDTH) && (obstaclePositions[i].x < (page + 1) * PAGEWIDTH));
    }

    boolean isVisibleEnemy(int i) {
        // TODO - this probably needs fixing when prey is near page boundary
        return ((preyPositions[i].x >= page * PAGEWIDTH) && (preyPositions[i].x < (page + 1) * PAGEWIDTH)
                && !preyDead[i]);
    }

    void changeDirection(int preyno) {
        if (preyDirection[preyno] == LEFT) {
            preyFaceRight(preyno);
        } else {
            preyFaceLeft(preyno);
        }
    }

    synchronized void die() {
        System.out.println("The Player is dead. Press the Enter key to restart the level");
        ingame = false;
        dead = true;
        displayStatus();
    }

    synchronized void incrementObstacleImage(int i) {
        obstacleImageNo[i]++;
        if (obstacleImageNo[i] == OBSTACLEIMAGES)
            obstacleImageNo[i] = 0;
    }

    synchronized void incrementHeroImage() {
        reaperImageNo++;
        if (reaperImageNo == HEROIMAGES)
            reaperImageNo = 0;
    }

    synchronized void incrementEnemyImage(int i) {
        preyImageNo[i]++;
        if (preyImageNo[i] > (PREYIMAGES - 2))
            preyImageNo[i] = 0;
    }

    Point move(int preyNo, Point start, int direction, boolean odd) {
        if (odd)
            incrementEnemyImage(preyNo);
        if (direction == LEFT) {
            return (new Point(start.x - preyMove[preyType[preyNo]], start.y));
        } else {
            return (new Point(start.x + preyMove[preyType[preyNo]], start.y));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            reapercrouching = false;
            decKeysDown(DOWNDOWN);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            decKeysDown(LEFTDOWN);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            decKeysDown(RIGHTDOWN);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    synchronized Image getHeroImage() {
        return (reaperImages[reaperImageNo]);
    }

    synchronized Image getObstacleImage(int obstaclenum) {
        return (obstacleImages[obstacleImageNo[obstaclenum]]);
    }

    synchronized Image getEnemyImage(int preyno) {
        return (preyImage[preyno][preyImageNo[preyno]]);
    }

    void displayStatus() {
        /*
         * // when a certain F Key is pressed, display positions of Reaper, // all
         * obstacles and all prey and the end flag (warp zone)
         * System.out.println("--------"); System.out.println("Reaper is " + (dead?
         * "dead" : "alive")); if (dead) { int preyno = isEnemyPosition(playerPosition);
         * System.out.println("Reaper was killed by prey " + preyno); }
         * System.out.println("In game is " + ingame); System.out.println("Reaper is " +
         * (onTheWayUp? "on the way up" : "not on the way up"));
         * System.out.println("Reaper's position is (" + playerPosition.x + ", " +
         * playerPosition.y + ")");
         * System.out.println("Reaper's visible left, bottom co-ordinates are " +
         * (playerPosition.x + 55) + " and " + (playerPosition.y + REAPERHEIGHT));
         * System.out.println("Reaper is on page " + page +
         * " (which shows x positions from " + (page*PAGEWIDTH) + " to " +
         * ((page+1)*PAGEWIDTH) + ")"); System.out.println("Screen bounds are (" +
         * getBounds().width + ", " + getBounds().height + ")"); System.out.println("");
         * for (int prey=0; prey < preyNumber; prey++) { System.out.println("Enemy " +
         * prey + " is " + preyTypeString(preyType[prey]) + " and is " + (preyDead[prey]
         * ? " " : "not ") + "dead"); System.out.println("Enemy " + prey +
         * " is at position (" + preyPositions[prey].x + ", " +
         * preyPositions[prey].y+") and has size " + preySize[preyType[prey]]); }
         * //System.out.println(""); //for (int obstacle=0; obstacle < obstacleNumber;
         * obstacle++) { // System.out.println("Obstacle " + obstacle +
         * " is at position (" + obstaclePositions[obstacle].x + // ", " +
         * obstaclePositions[obstacle].y+")"); //} System.out.println("");
         * System.out.println("The warp zone is positioned at (" + warpzone.x + ", " +
         * warpzone.y + ")"); System.out.println("========");
         */
    }

    String preyTypeString(int fortype) {
        switch (fortype) {
        case RUNNER:
            return ("RUNNER");
        default:
            return ("Unknown type (" + fortype + ")");
        }
    }

    void changePageIfNecessary() {
        if (playerPosition.x == LEFTEDGE)
            return;
        if ((playerPosition.x + SPACE_FROM_X_TO_REAPER) > (page * PAGEWIDTH + RIGHTEDGE - XAMOUNT)) {
            // System.out.println("Change page up: (" + playerPosition.x+ "+" +
            // SPACE_FROM_X_TO_REAPER+ ") > (" + page+ "*" + PAGEWIDTH + "+" + RIGHTEDGE +
            // "-" + XAMOUNT + ")");
            page++;
        } else if ((playerPosition.x + SPACE_FROM_X_TO_REAPER) - (page * PAGEWIDTH) < (LEFTEDGE + XAMOUNT)) {
            // System.out.println("Change page down: (" + playerPosition.x+ "+" +
            // SPACE_FROM_X_TO_REAPER+ ")-(" + page+ "*" + PAGEWIDTH + ") < (" + LEFTEDGE+
            // "+" + XAMOUNT+")");
            page--;
        }
    }
}
