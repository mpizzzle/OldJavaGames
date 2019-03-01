
/* Copyright (c) Mary Percival 2003                          */
/* Mario game                      Created May 2003          */

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class MarioGame extends JFrame implements KeyListener, Runnable {
    private static final long serialVersionUID = 7030451342808203557L;
    boolean easymode = true;
    static int MAXLEVEL = 9;
    static int level = 0;

    // the levels so far are:
    // 1 beginner level: 1 piranha & 1 goomba
    // 2 introducing green koopa
    // 3 more of the same
    // 4 introducing red koopa (flying)
    // 5 more and harder
    // 6 ?
    // 7 ?
    // 8 ?
    // 9 ?

    static int LEFT = 1;
    static int RIGHT = 0;
    // the next 2 constants are overridden in init once we know the screen size
    static int RIGHTEDGE = 789;
    static int BOTTOMEDGE = 800;
    static int PAGEWIDTH = 741;

    static Point IMAGESIZE = new Point(152, 107);
    static final int LEFTEDGE = -45;
    static final int TOPEDGE = -10;
    static int GROUNDLEVEL = 503;
    static int startPosY;
    static int JUMPAMOUNT = 90;
    static int BOUNCEAMOUNT = 90;
    static final int FALLAMOUNT = 16; // amount he falls each time interval
    static final int RISEAMOUNT = 16; // amount he rises each time interval
    static final int XAMOUNT = 12;

    static final int X = 1;
    static final int Y = 2;
    static final int NOT = 3;
    static final int DELAY = 50;

    static final int OBSTACLEIMAGES = 19;
    static final int HEROIMAGES = 4;
    static final int ENEMYIMAGES = 3;

    // ----------------------------------------------------------------------------------------------
    // ENEMY TYPES
    // ----------------------------------------------------------------------------------------------
    static final int ENEMYTYPES = 15; // goomba, koopa red, koopa green, koopa red flying, banzai, wiggler, boo,
                                      // fireboo,bobomb,warship,yeti,chainchomp,atom
    static final int GOOMBA = 0;
    static final int KOOPARED = 1;
    static final int KOOPAGREEN = 2;
    static final int KOOPAREDFLYING = 3;
    static final int BANZAI = 4;
    static final int WIGGLER = 5;
    static final int BOO = 6;
    static final int FIREBOO = 7;
    static final int BOBOMB = 8;
    static final int BULLET = 9;
    static final int WARSHIP = 10;
    static final int YETI = 11;
    static final int FLYER = 12;
    static final int CHOMP = 13;
    static final int ATOM = 14;

    // enemy positions (height)
    static final int GOOMBAHEIGHT = GROUNDLEVEL + 55;
    static final int KOOPAREDHEIGHT = GROUNDLEVEL + 39;
    static final int KOOPAGREENHEIGHT = GROUNDLEVEL + 39;
    static final int KOOPAREDFLYINGHEIGHT = GROUNDLEVEL - 16;
    static final int BANZAIHEIGHT = GROUNDLEVEL - 90;
    static final int WIGGLERHEIGHT = GROUNDLEVEL + 16;
    static final int BOOHEIGHT = GROUNDLEVEL - 7;
    static final int FIREBOOHEIGHT = GROUNDLEVEL - 7;
    static final int BOBOMBHEIGHT = GROUNDLEVEL + 5;
    static final int BULLETHEIGHT = GROUNDLEVEL + 55;
    static final int WARSHIPHEIGHT = GROUNDLEVEL + 100;
    static final int YETIHEIGHT = GROUNDLEVEL;
    static final int FLYERHEIGHT = GROUNDLEVEL - 20;
    static final int CHOMPHEIGHT = GROUNDLEVEL - 55;
    static final int ATOMHEIGHT = GROUNDLEVEL + 40;
    static int enemyHeight[] = new int[ENEMYTYPES];

    static final int enemySize[] = { 27, 42, 42, 42, 172, 75, 37, 37, 74, 8, 152, 86, 86, 75, 25 };
    static final int enemyTopSpace[] = { 4, 13, 13, 0, 0, 10, 6, 6, 7, 0, 20, 8, 17, 10, 0 };

    static final int SPACE_FROM_X_TO_MARIO = 55;
    static final int MARIOHEIGHT = 86;
    static final int MARIOOFFSET = 21;
    static final int MARIOCROUCHOFFSET = 39;
    static final int MARIOSTANDINGSIZE = 65;
    static final int MARIOCROUCHINGSIZE = 45;

    static final int JUMPDURATION = 5;
    static final int SUPERJUMPDURATION = 8;
    static final int MARIOLEFTOFOBSTACLE = 45; // fudge factor because Mario image is diff size to obstacle image
    static final int MARIORIGHTOFOBSTACLE = 10; // fudge factor because Mario image is diff size to obstacle image
    static final int ENEMYLEFTOFOBSTACLE = 16; // fudge factor for enemy images
    static final int ENEMYRIGHTOFOBSTACLE = 49; // >49 causes panic!
    static final int MARIOLEFTOFENEMY = 73;
    static final int MARIORIGHTOFENEMY = -23;

    static final int LEFTDOWN = 0;
    static final int RIGHTDOWN = 1;
    static final int DOWNDOWN = 2;
    static final int SPACEDOWN = 3;

    static final int OBSTACLEHEIGHT = 42;
    static final int TALLOBSTACLEDIFF = 28;
    static final int OBSTACLEWIDTH = 86;
    static int GROUNDDIFF = 605 - GROUNDLEVEL;

    // all member variables 'static' because shared with the enemy action thread
    static MarioArea area;
    static Thread timer;
    static boolean finished = false;
    static boolean ingame = false;
    static boolean juststarted = true;
    static Point warpzone;
    static int[] keysdown = new int[4];
    static Point[] enemyPositions;
    static Point playerPosition;
    static Point[] obstaclePositions;
    static boolean[] piranhaPopping; // one for each obstacle
    static boolean[] hasPiranha; // one for each obstacle
    static boolean[] isTall; // one for each obstacle
    static int enemyDirection[];
    static int enemyNumber = 1, enemiesRemaining = 1, obstacleNumber = 1;
    static boolean herocrouching = false;
    static boolean onTheWayUp = false;
    static boolean bouncing = false;
    static boolean wasbouncing = false;
    static boolean dead = false;
    static int page = 0;
    static int heroImageNo = 0;
    static int enemyImageNo[];
    static int levellength = 1482;
    static Image[] heroImages = new Image[HEROIMAGES];
    static Image[] obstacleImages = new Image[OBSTACLEIMAGES]; // pipe, piranhapeep, piranha peepy etc
    static Image[] squished = new Image[ENEMYTYPES]; // squished images for each enemy type
    static Image[][][] enemyImages = new Image[ENEMYTYPES][2][ENEMYIMAGES]; // left right left right or whatever
    static int obstacleImageNo[]; // what is the current image for obstacle n
    static Image[][] enemyImage = new Image[ENEMYTYPES][ENEMYIMAGES];// what is the current image for enemy n?
    static Image floors[] = new Image[MAXLEVEL];
    static int enemyType[]; // what is the type of enemy n?
    static boolean enemyDead[]; // is enemy n dead?
    static boolean heroleft = false;
    static boolean jumping = false;
    static int enemyMove[] = new int[ENEMYTYPES];
    boolean somethingChangedSinceRepaint = false;

    // *************************************************************
    public static void main(String[] args) {
        MarioGame marioGame = new MarioGame();
        marioGame.setLayout(null);
        marioGame.setBackground(Color.white);
        marioGame.setSize(RIGHTEDGE, BOTTOMEDGE);
        area = new MarioArea(marioGame);
        marioGame.add(area);
        //RIGHTEDGE = (marioGame.getBounds().width / XAMOUNT) * XAMOUNT + LEFTEDGE;
        //BOTTOMEDGE = marioGame.getBounds().height - 1;
        marioGame.setVisible(true);
        area.setBounds(0, 0, RIGHTEDGE, BOTTOMEDGE);
        //GROUNDLEVEL = marioGame.getBounds().height - GROUNDDIFF;
        //GROUNDLEVEL = BOTTOMEDGE;// - GROUNDDIFF;
        startPosY = GROUNDLEVEL;
        //MarioArea.FLOORLEVEL = marioGame.getBounds().height - MarioArea.FLOORDIFF;
        area.setVisible(true);

        marioGame.addKeyListener(marioGame);
        area.addKeyListener(marioGame);
        area.requestFocus();

        obstacleImages[0] = area.obstacle;
        obstacleImages[1] = area.obstacle;
        obstacleImages[2] = area.obstacle;
        obstacleImages[3] = area.obstacle;
        obstacleImages[4] = area.piranhapeepo;
        obstacleImages[5] = area.piranhapeep;
        obstacleImages[6] = area.piranhapeepy;
        obstacleImages[7] = area.piranha1;
        obstacleImages[8] = area.piranha1;
        obstacleImages[9] = area.piranha1;
        obstacleImages[10] = area.piranha2; //
        obstacleImages[11] = area.piranha2; //
        obstacleImages[12] = area.piranha2; //
        obstacleImages[13] = area.piranhapeepy; //
        obstacleImages[14] = area.piranhapeep; //
        obstacleImages[15] = area.piranhapeepo;
        obstacleImages[16] = area.obstacle; //
        obstacleImages[17] = area.obstacle; //
        obstacleImages[18] = area.obstacle; //

        enemyImages[GOOMBA][RIGHT][0] = area.goomba;
        enemyImages[GOOMBA][RIGHT][1] = area.goomba2;
        enemyImages[GOOMBA][LEFT][0] = area.goombaleft;
        enemyImages[GOOMBA][LEFT][1] = area.goomba2left;

        enemyImages[KOOPARED][RIGHT][0] = area.kooparedleft;
        enemyImages[KOOPARED][RIGHT][1] = area.kooparedleft;
        enemyImages[KOOPARED][LEFT][0] = area.koopared;
        enemyImages[KOOPARED][LEFT][1] = area.koopared;

        enemyImages[KOOPAGREEN][RIGHT][0] = area.koopagreen;
        enemyImages[KOOPAGREEN][RIGHT][1] = area.koopagreen;
        enemyImages[KOOPAGREEN][LEFT][0] = area.koopagreenleft;
        enemyImages[KOOPAGREEN][LEFT][1] = area.koopagreenleft;

        enemyImages[KOOPAREDFLYING][RIGHT][0] = area.kooparedfleft;
        enemyImages[KOOPAREDFLYING][RIGHT][1] = area.kooparedfleft;
        enemyImages[KOOPAREDFLYING][LEFT][0] = area.kooparedf;
        enemyImages[KOOPAREDFLYING][LEFT][1] = area.kooparedf;

        enemyImages[BANZAI][RIGHT][0] = area.Banzai;
        enemyImages[BANZAI][RIGHT][1] = area.Banzai;
        enemyImages[BANZAI][LEFT][0] = area.Banzai;
        enemyImages[BANZAI][LEFT][1] = area.Banzai;

        enemyImages[WIGGLER][RIGHT][0] = area.Wiglerright;
        enemyImages[WIGGLER][RIGHT][1] = area.Wiglerright1;
        enemyImages[WIGGLER][RIGHT][2] = area.Wiglerright2;
        enemyImages[WIGGLER][LEFT][0] = area.Wiglerleft;
        enemyImages[WIGGLER][LEFT][1] = area.Wiglerleft1;
        enemyImages[WIGGLER][LEFT][2] = area.Wiglerleft2;

        enemyImages[BOO][RIGHT][0] = area.Boo2;
        enemyImages[BOO][RIGHT][1] = area.Boo2;
        enemyImages[BOO][LEFT][0] = area.Boo1;
        enemyImages[BOO][LEFT][1] = area.Boo1;

        enemyImages[FIREBOO][RIGHT][0] = area.Fireboo2;
        enemyImages[FIREBOO][RIGHT][1] = area.Fireboo2;
        enemyImages[FIREBOO][LEFT][0] = area.Fireboo1;
        enemyImages[FIREBOO][LEFT][1] = area.Fireboo1;

        enemyImages[BOBOMB][LEFT][0] = area.Bobomb;
        enemyImages[BOBOMB][LEFT][1] = area.Bobomb2;
        enemyImages[BOBOMB][RIGHT][0] = area.Bobombf;
        enemyImages[BOBOMB][RIGHT][1] = area.Bobombf2;

        enemyImages[BULLET][LEFT][0] = area.Bullet;
        enemyImages[BULLET][LEFT][1] = area.Bullet;
        enemyImages[BULLET][RIGHT][0] = area.Bullet;
        enemyImages[BULLET][RIGHT][1] = area.Bullet;

        enemyImages[WARSHIP][LEFT][0] = area.Warship1;
        enemyImages[WARSHIP][LEFT][1] = area.Warship2;
        enemyImages[WARSHIP][RIGHT][0] = area.Warship1;
        enemyImages[WARSHIP][RIGHT][1] = area.Warship2;

        enemyImages[YETI][LEFT][0] = area.Yetil1;
        enemyImages[YETI][LEFT][1] = area.Yetil2;
        enemyImages[YETI][RIGHT][0] = area.Yetir1;
        enemyImages[YETI][RIGHT][1] = area.Yetir2;

        enemyImages[FLYER][LEFT][0] = area.p1fl;
        enemyImages[FLYER][LEFT][1] = area.p2fl;
        enemyImages[FLYER][RIGHT][0] = area.p1fr;
        enemyImages[FLYER][RIGHT][1] = area.p2fr;

        enemyImages[CHOMP][LEFT][0] = area.chainchomp1;
        enemyImages[CHOMP][LEFT][1] = area.chainchomp2;
        enemyImages[CHOMP][RIGHT][0] = area.chainchomp1;
        enemyImages[CHOMP][RIGHT][1] = area.chainchomp2;

        enemyImages[ATOM][LEFT][0] = area.Atom1;
        enemyImages[ATOM][LEFT][1] = area.Atom2;
        enemyImages[ATOM][RIGHT][0] = area.Atom1;
        enemyImages[ATOM][RIGHT][1] = area.Atom2;

        enemyMove[GOOMBA] = 5;
        enemyMove[KOOPARED] = 5;
        enemyMove[KOOPAGREEN] = 5;
        enemyMove[KOOPAREDFLYING] = 8;
        enemyMove[BANZAI] = 16;
        enemyMove[WIGGLER] = 15;
        enemyMove[BOO] = 5;
        enemyMove[FIREBOO] = 10;
        enemyMove[BOBOMB] = 5;
        enemyMove[BULLET] = 10;
        enemyMove[WARSHIP] = 20;
        enemyMove[YETI] = 5;
        enemyMove[FLYER] = 10;
        enemyMove[CHOMP] = 0;
        enemyMove[ATOM] = 1;

        enemyHeight[GOOMBA] = 40;
        enemyHeight[KOOPARED] = 33;
        enemyHeight[KOOPAGREEN] = 33;
        enemyHeight[KOOPAREDFLYING] = 33;
        enemyHeight[BANZAI] = 60;
        enemyHeight[WIGGLER] = 33;
        enemyHeight[BOO] = 33;
        enemyHeight[FIREBOO] = 33;
        enemyHeight[BOBOMB] = 33;
        enemyHeight[BULLET] = 5;
        enemyHeight[WARSHIP] = 50;
        enemyHeight[YETI] = 33;
        enemyHeight[FLYER] = 50;
        enemyHeight[CHOMP] = 60;
        enemyHeight[ATOM] = 22;

        squished[GOOMBA] = area.squishedgoomba;
        squished[KOOPARED] = area.redshell;
        squished[KOOPAGREEN] = area.greenshell;
        squished[KOOPAREDFLYING] = area.koopared;
        squished[BANZAI] = area.Banzai; // not actually used
        squished[WIGGLER] = area.Wiglerleft; // not actually used
        squished[BOO] = area.Boo1;
        squished[FIREBOO] = area.Fireboo1;
        squished[BOBOMB] = area.Bobomb;
        squished[BULLET] = area.Bullet;
        squished[WARSHIP] = area.Warship2;
        squished[YETI] = area.Yetil1;
        squished[FLYER] = area.p1fl;
        squished[CHOMP] = area.chainchompdead;
        squished[ATOM] = area.Atom1;

        for (int i = 0; i < MAXLEVEL; i++) {
            floors[i] = area.floor4;
        }

        floors[0] = area.floor4;
        floors[1] = area.floor5;
        floors[2] = area.floor3;
        floors[3] = area.floor7;
        floors[4] = area.floor8;
        floors[5] = area.floor9;
        floors[6] = area.floor2;
        floors[7] = area.floor6;
        floors[8] = area.Floor10;

        timer = new Thread(marioGame);
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

    synchronized void enemyFaceRight(int enemyno) {
        enemyImage[enemyno][0] = enemyImages[enemyType[enemyno]][RIGHT][0];
        enemyImage[enemyno][1] = enemyImages[enemyType[enemyno]][RIGHT][1];
        if (enemyType[enemyno] == WIGGLER)
            enemyImage[enemyno][2] = enemyImages[enemyType[enemyno]][RIGHT][2];
        enemyDirection[enemyno] = RIGHT;
    }

    synchronized void enemyFaceLeft(int enemyno) {
        enemyImage[enemyno][0] = enemyImages[enemyType[enemyno]][LEFT][0];
        enemyImage[enemyno][1] = enemyImages[enemyType[enemyno]][LEFT][1];
        if (enemyType[enemyno] == WIGGLER)
            enemyImage[enemyno][2] = enemyImages[enemyType[enemyno]][LEFT][2];
        enemyDirection[enemyno] = LEFT;
    }

    synchronized void enemyDie(int enemyno) {
        // displayStatus();
        enemyImage[enemyno][0] = squished[enemyType[enemyno]];
        enemyImage[enemyno][1] = squished[enemyType[enemyno]];
        if (enemyType[enemyno] == WIGGLER)
            enemyImage[enemyno][2] = squished[enemyType[enemyno]];
    }

    synchronized void standStill() {
        heroImageNo = 0;
    }

    public void doLevel(int level) {
        // this runs one 'level'
        juststarted = true;
        dead = false;

        // position Hero at the bottom left
        playerPosition = new Point(LEFTEDGE, GROUNDLEVEL);
        faceRight();

        // *********** THIS STUFF DEPENDS WHAT LEVEL YOU ARE ON ************
        switch (level) {
        case 1:
            obstacleNumber = 4; // 4 pipes for level 1
            enemyNumber = 1;
            levellength = 1482; // 2 pages
            break;
        case 2:
            obstacleNumber = 11;
            enemyNumber = 5;
            levellength = 2964; // 4 pages
            break;
        case 3:
            obstacleNumber = 13;
            enemyNumber = 8;
            levellength = 2223; // 3 pages
            break;
        case 4:
            obstacleNumber = 11;
            enemyNumber = 12;
            levellength = 4446; // 6 pages
            break;
        case 5:
            obstacleNumber = 17;
            enemyNumber = 13;
            levellength = 5928; // 8 pages
            break;
        case 6:
            obstacleNumber = 14;
            enemyNumber = 1;
            levellength = 5928; // 8 pages
            break;
        case 7:
            obstacleNumber = 14;
            enemyNumber = 6;
            levellength = 3705; // 5 pages
            break;
        case 8:
            obstacleNumber = 16;
            enemyNumber = 9;
            levellength = 5928; // 8 pages
            break;
        case 9:
            obstacleNumber = 6;
            enemyNumber = 9;
            levellength = 3705; // 5 pages
            break;
        case 10:
            obstacleNumber = 29;
            enemyNumber = 0;
            levellength = 4446; // 6 pages
        default:
            System.out.println("Level " + level + " has not yet been implemented");
        }

        enemyImage = new Image[enemyNumber][ENEMYIMAGES];// what is the current image for enemy n?
        // initialise piranha states
        obstacleImageNo = new int[obstacleNumber];
        hasPiranha = new boolean[obstacleNumber];
        piranhaPopping = new boolean[obstacleNumber];
        isTall = new boolean[obstacleNumber];

        // initialise the arrays of enemies, etc
        enemyPositions = new Point[enemyNumber];
        enemyType = new int[enemyNumber];
        enemyDead = new boolean[enemyNumber];
        obstaclePositions = new Point[obstacleNumber];
        enemyDirection = new int[enemyNumber];
        enemyImageNo = new int[enemyNumber];

        for (int i = 0; i < enemyNumber; i++) {
            enemyImageNo[i] = 0;
        }

        switch (level) {
        // ---------------------------------
        // LEVEL 1
        // ---------------------------------
        case 1:
            for (int i = 0; i < obstacleNumber; i++) {
                isTall[i] = false;
                if (i == 0) {
                    piranhaPopping[i] = true;
                    hasPiranha[i] = true;
                } else {
                    if (i == 3)
                        isTall[i] = true;
                    piranhaPopping[i] = false;
                    hasPiranha[i] = false;
                }
            }
            obstaclePositions[0] = new Point(400, GROUNDLEVEL);
            obstaclePositions[1] = new Point(950, GROUNDLEVEL);
            obstaclePositions[2] = new Point(1245, GROUNDLEVEL); // 1255 for adjacent
            obstaclePositions[3] = new Point(1320, GROUNDLEVEL);

            enemyPositions[0] = new Point(1000, GOOMBAHEIGHT);
            enemyType = new int[] { GOOMBA };
            break;
        // -----------------------------------------------
        // LEVEL 2
        // -----------------------------------------------
        case 2:
            for (int i = 0; i < obstacleNumber; i++) {
                isTall[i] = false;
                if (i == 2 || i == 7 || i == 8) {
                    piranhaPopping[i] = true;
                    hasPiranha[i] = true;
                } else {
                    if (i == 4 || i == 6 || i == 10)
                        isTall[i] = true;
                    piranhaPopping[i] = false;
                    hasPiranha[i] = false;
                }
            }

            // PAGE 1
            obstaclePositions[0] = new Point(150, GROUNDLEVEL);
            obstaclePositions[1] = new Point(225, GROUNDLEVEL);
            obstaclePositions[2] = new Point(300, GROUNDLEVEL);
            // PAGE 2
            obstaclePositions[3] = new Point(891, GROUNDLEVEL);
            obstaclePositions[4] = new Point(966, GROUNDLEVEL);
            // PAGE 3
            obstaclePositions[5] = new Point(1632, GROUNDLEVEL);
            obstaclePositions[6] = new Point(1707, GROUNDLEVEL);
            obstaclePositions[7] = new Point(1782, GROUNDLEVEL);
            // PAGE 4
            obstaclePositions[8] = new Point(2373, GROUNDLEVEL);
            obstaclePositions[9] = new Point(2743, GROUNDLEVEL);
            obstaclePositions[10] = new Point(2818, GROUNDLEVEL);

            enemyType = new int[] { KOOPAGREEN, GOOMBA, GOOMBA, GOOMBA, GOOMBA };
            enemyPositions[0] = new Point(375, KOOPAGREENHEIGHT);
            enemyPositions[1] = new Point(1041, GOOMBAHEIGHT);
            enemyPositions[2] = new Point(1141, GOOMBAHEIGHT);
            enemyPositions[3] = new Point(1882, GOOMBAHEIGHT);
            enemyPositions[4] = new Point(2473, GOOMBAHEIGHT);
            break;
        // -----------------------------------------------
        // LEVEL 3
        // -----------------------------------------------
        case 3:
            for (int i = 0; i < obstacleNumber; i++) {
                isTall[i] = false;
                if (i == 4)
                    isTall[i] = true;
                if (i != 4 && i != 11 && i != 12) {
                    piranhaPopping[i] = true;
                    hasPiranha[i] = true;
                } else {
                    piranhaPopping[i] = false;
                    hasPiranha[i] = false;
                }
            }
            // PAGE 1
            obstaclePositions[0] = new Point(50, GROUNDLEVEL);
            obstaclePositions[1] = new Point(130, GROUNDLEVEL);
            obstaclePositions[2] = new Point(210, GROUNDLEVEL);

            obstaclePositions[3] = new Point(540, GROUNDLEVEL);
            obstaclePositions[4] = new Point(620, GROUNDLEVEL);
            obstaclePositions[5] = new Point(700, GROUNDLEVEL);
            // PAGE 2
            obstaclePositions[6] = new Point(850, GROUNDLEVEL);
            obstaclePositions[7] = new Point(925, GROUNDLEVEL);
            obstaclePositions[8] = new Point(1000, GROUNDLEVEL);
            obstaclePositions[9] = new Point(1075, GROUNDLEVEL);
            obstaclePositions[10] = new Point(1150, GROUNDLEVEL);
            obstaclePositions[11] = new Point(1350, GROUNDLEVEL);

            // PAGE 3
            obstaclePositions[12] = new Point(2000, GROUNDLEVEL);

            enemyType = new int[] { KOOPAGREEN, GOOMBA, GOOMBA, GOOMBA, GOOMBA, GOOMBA, GOOMBA, GOOMBA };

            enemyPositions[0] = new Point(365, KOOPAGREENHEIGHT);
            enemyPositions[1] = new Point(420, GOOMBAHEIGHT);
            enemyPositions[2] = new Point(495, GOOMBAHEIGHT);
            enemyPositions[3] = new Point(1250, GOOMBAHEIGHT);
            enemyPositions[4] = new Point(1500, GOOMBAHEIGHT);
            enemyPositions[5] = new Point(1575, GOOMBAHEIGHT);
            enemyPositions[6] = new Point(1650, GOOMBAHEIGHT);
            enemyPositions[7] = new Point(1725, GOOMBAHEIGHT);

            break;
        // -----------------------------------------------
        // LEVEL 4
        // -----------------------------------------------
        case 4:
            for (int i = 0; i < obstacleNumber; i++) {
                isTall[i] = false;
                if (i == 2 || i == 5 || i == 7 || i == 8 || i == 9 || i == 10) {
                    piranhaPopping[i] = true;
                    hasPiranha[i] = true;
                } else {
                    if (i == 1 || i == 4 || i == 6)
                        isTall[i] = true;
                    piranhaPopping[i] = false;
                    hasPiranha[i] = false;
                }
            }

            // PAGE 1
            obstaclePositions[0] = new Point(185, GROUNDLEVEL);
            obstaclePositions[1] = new Point(370, GROUNDLEVEL);
            obstaclePositions[2] = new Point(700, GROUNDLEVEL);
            // PAGE 2
            obstaclePositions[3] = new Point(900, GROUNDLEVEL);
            obstaclePositions[4] = new Point(1300, GROUNDLEVEL);
            // PAGE 3
            obstaclePositions[5] = new Point(1500, GROUNDLEVEL);
            obstaclePositions[6] = new Point(1750, GROUNDLEVEL);
            obstaclePositions[7] = new Point(2000, GROUNDLEVEL);
            // PAGE 4

            // PAGE 5
            obstaclePositions[8] = new Point(3064, GROUNDLEVEL);
            obstaclePositions[9] = new Point(3264, GROUNDLEVEL);
            // PAGE 6
            obstaclePositions[10] = new Point(4100, GROUNDLEVEL);

            enemyType = new int[] { KOOPARED, KOOPARED, KOOPAREDFLYING, GOOMBA, GOOMBA, GOOMBA, GOOMBA, KOOPAREDFLYING,
                    KOOPARED, KOOPARED, KOOPARED, FLYER };

            enemyPositions[0] = new Point(470, KOOPAREDHEIGHT);
            enemyPositions[1] = new Point(520, KOOPAREDHEIGHT);

            enemyPositions[2] = new Point(1000, KOOPAREDFLYINGHEIGHT);
            enemyPositions[3] = new Point(1400, GOOMBAHEIGHT);

            enemyPositions[4] = new Point(2230, GOOMBAHEIGHT);
            enemyPositions[5] = new Point(2280, GOOMBAHEIGHT);
            enemyPositions[6] = new Point(2320, GOOMBAHEIGHT);

            enemyPositions[7] = new Point(3600, KOOPAREDFLYINGHEIGHT);

            enemyPositions[8] = new Point(3710, KOOPAREDHEIGHT);
            enemyPositions[9] = new Point(3760, KOOPAREDHEIGHT);
            enemyPositions[10] = new Point(3810, KOOPAREDHEIGHT);
            enemyPositions[11] = new Point(3500, FLYERHEIGHT);

            break;
        // -----------------------------------------------
        // LEVEL 5
        // -----------------------------------------------
        case 5:
            for (int i = 0; i < obstacleNumber; i++) {
                isTall[i] = false;
                if (i == 0 || i == 2 || i == 4 || i == 10 || i == 11 || i == 14 || i == 16) {
                    piranhaPopping[i] = true;
                    hasPiranha[i] = true;
                } else {
                    if (i == 1 || i == 3 || i == 6 || i == 7 || i == 8 || i == 9 || i == 12 || i == 15)
                        isTall[i] = true;
                    piranhaPopping[i] = false;
                    hasPiranha[i] = false;
                }
            }
            // page1
            obstaclePositions[0] = new Point(150, GROUNDLEVEL);
            obstaclePositions[1] = new Point(300, GROUNDLEVEL);
            obstaclePositions[2] = new Point(600, GROUNDLEVEL);
            // page2
            obstaclePositions[3] = new Point(800, GROUNDLEVEL);
            obstaclePositions[4] = new Point(900, GROUNDLEVEL);
            // page3
            obstaclePositions[5] = new Point(1600, GROUNDLEVEL);
            obstaclePositions[6] = new Point(1700, GROUNDLEVEL);
            obstaclePositions[7] = new Point(1800, GROUNDLEVEL);
            obstaclePositions[8] = new Point(1900, GROUNDLEVEL);
            obstaclePositions[9] = new Point(2000, GROUNDLEVEL);
            // page4
            obstaclePositions[10] = new Point(2400, GROUNDLEVEL);
            // page5
            obstaclePositions[11] = new Point(3100, GROUNDLEVEL);
            obstaclePositions[12] = new Point(3200, GROUNDLEVEL);
            // page6
            obstaclePositions[13] = new Point(3850, GROUNDLEVEL);
            obstaclePositions[14] = new Point(3950, GROUNDLEVEL);
            obstaclePositions[15] = new Point(4050, GROUNDLEVEL);
            // page7
            // NONE\\
            // page8
            obstaclePositions[16] = new Point(5550, GROUNDLEVEL);

            enemyType = new int[] { KOOPAREDFLYING, GOOMBA, GOOMBA, KOOPAGREEN, KOOPAREDFLYING, KOOPAGREEN, KOOPAGREEN,
                    KOOPAGREEN, KOOPAREDFLYING, KOOPAREDFLYING, GOOMBA, GOOMBA, GOOMBA };

            enemyPositions[0] = new Point(450, KOOPAREDFLYINGHEIGHT);
            enemyPositions[1] = new Point(700, GOOMBAHEIGHT);
            enemyPositions[2] = new Point(1000, GOOMBAHEIGHT);
            enemyPositions[3] = new Point(1100, KOOPAGREENHEIGHT);
            enemyPositions[4] = new Point(2100, KOOPAREDFLYINGHEIGHT);
            enemyPositions[5] = new Point(2500, KOOPAGREENHEIGHT);
            enemyPositions[6] = new Point(2600, KOOPAGREENHEIGHT);
            enemyPositions[7] = new Point(2700, KOOPAGREENHEIGHT);
            enemyPositions[8] = new Point(3400, KOOPAREDFLYINGHEIGHT);
            enemyPositions[9] = new Point(3600, KOOPAREDFLYINGHEIGHT);
            enemyPositions[10] = new Point(4700, GOOMBAHEIGHT);
            enemyPositions[11] = new Point(4800, GOOMBAHEIGHT);
            enemyPositions[12] = new Point(4800, GOOMBAHEIGHT);
            break;
        // -----------------------------------------------
        // LEVEL 6
        // -----------------------------------------------
        case 6:
            for (int i = 0; i < obstacleNumber; i++) {
                isTall[i] = false;
                piranhaPopping[i] = false;
                hasPiranha[i] = false;
                if (i == 2 || i == 3 || i == 4 || i == 5 || i == 7 || i == 10 || i == 11) {
                    isTall[i] = true;
                } else {
                    if (i == 13) {
                        piranhaPopping[i] = true;
                        hasPiranha[i] = true;
                    }

                }
            }
            obstaclePositions[0] = new Point(350, GROUNDLEVEL);
            obstaclePositions[1] = new Point(1091, GROUNDLEVEL);
            obstaclePositions[2] = new Point(1191, GROUNDLEVEL);
            obstaclePositions[3] = new Point(1600, GROUNDLEVEL);
            obstaclePositions[4] = new Point(1675, GROUNDLEVEL);
            obstaclePositions[5] = new Point(1750, GROUNDLEVEL);
            obstaclePositions[6] = new Point(2500, GROUNDLEVEL);
            obstaclePositions[7] = new Point(2600, GROUNDLEVEL);
            obstaclePositions[8] = new Point(2700, GROUNDLEVEL);
            obstaclePositions[9] = new Point(3975, GROUNDLEVEL);
            obstaclePositions[10] = new Point(4050, GROUNDLEVEL);
            obstaclePositions[11] = new Point(4150, GROUNDLEVEL);
            obstaclePositions[12] = new Point(4225, GROUNDLEVEL);
            obstaclePositions[13] = new Point(5550, GROUNDLEVEL);

            enemyPositions[0] = new Point(-400, BANZAIHEIGHT);
            enemyType[0] = BANZAI;

            break;

        // -----------------------------------------------
        // LEVEL 7
        // -----------------------------------------------
        case 7:

            for (int i = 0; i < obstacleNumber; i++) {
                isTall[i] = false;
                piranhaPopping[i] = false;
                hasPiranha[i] = false;
                if (i == 1 || i == 2 || i == 8) {
                    isTall[i] = true;
                } else {
                    if (i == 0 || i == 3 || i == 5 || i == 7 || i == 9 || i == 10 || i == 11) {
                        piranhaPopping[i] = true;
                        hasPiranha[i] = true;
                    }
                }
            }
            obstaclePositions[0] = new Point(250, GROUNDLEVEL);
            obstaclePositions[1] = new Point(350, GROUNDLEVEL);
            obstaclePositions[2] = new Point(450, GROUNDLEVEL);
            obstaclePositions[3] = new Point(550, GROUNDLEVEL);
            obstaclePositions[4] = new Point(800, GROUNDLEVEL);
            obstaclePositions[5] = new Point(1000, GROUNDLEVEL);
            obstaclePositions[6] = new Point(1200, GROUNDLEVEL);
            obstaclePositions[7] = new Point(1400, GROUNDLEVEL);
            obstaclePositions[8] = new Point(1800, GROUNDLEVEL);
            obstaclePositions[9] = new Point(2400, GROUNDLEVEL);
            obstaclePositions[10] = new Point(2550, GROUNDLEVEL);
            obstaclePositions[11] = new Point(2700, GROUNDLEVEL);
            obstaclePositions[12] = new Point(3100, GROUNDLEVEL);
            obstaclePositions[13] = new Point(3600, GROUNDLEVEL);

            enemyType = new int[] { GOOMBA, GOOMBA, GOOMBA, GOOMBA, GOOMBA, WIGGLER };
            enemyPositions[0] = new Point(900, GOOMBAHEIGHT);
            enemyPositions[1] = new Point(1100, GOOMBAHEIGHT);
            enemyPositions[2] = new Point(1300, GOOMBAHEIGHT);
            enemyPositions[3] = new Point(1600, GOOMBAHEIGHT);
            enemyPositions[4] = new Point(2000, GOOMBAHEIGHT);
            enemyPositions[5] = new Point(3250, WIGGLERHEIGHT);
            break;
        // -----------------------------------------------
        // LEVEL 8
        // -----------------------------------------------
        case 8:

            for (int i = 0; i < obstacleNumber; i++) {
                isTall[i] = false;
                piranhaPopping[i] = false;
                hasPiranha[i] = false;
                if (i == 2 || i == 3 || i == 4 || i == 6 || i == 8 || i == 11) {
                    isTall[i] = true;
                } else {
                    if (i == 0 || i == 1 || i == 10 || i == 12 || i == 13) {
                        piranhaPopping[i] = true;
                        hasPiranha[i] = true;
                    }
                }
            }

            obstaclePositions[0] = new Point(100, GROUNDLEVEL);
            obstaclePositions[1] = new Point(650, GROUNDLEVEL);
            obstaclePositions[2] = new Point(1000, GROUNDLEVEL);
            obstaclePositions[3] = new Point(1075, GROUNDLEVEL);
            obstaclePositions[4] = new Point(1150, GROUNDLEVEL);
            obstaclePositions[5] = new Point(1500, GROUNDLEVEL);
            obstaclePositions[6] = new Point(1575, GROUNDLEVEL);
            obstaclePositions[7] = new Point(1650, GROUNDLEVEL);
            obstaclePositions[8] = new Point(1725, GROUNDLEVEL);
            obstaclePositions[9] = new Point(1800, GROUNDLEVEL);
            obstaclePositions[10] = new Point(2500, GROUNDLEVEL);
            obstaclePositions[11] = new Point(3400, GROUNDLEVEL);
            obstaclePositions[12] = new Point(4000, GROUNDLEVEL);
            obstaclePositions[13] = new Point(4075, GROUNDLEVEL);
            obstaclePositions[14] = new Point(5275, GROUNDLEVEL);
            obstaclePositions[15] = new Point(5775, GROUNDLEVEL);

            enemyType = new int[] { BOO, FIREBOO, GOOMBA, BOO, GOOMBA, FIREBOO, GOOMBA, GOOMBA, BOBOMB };

            enemyPositions[0] = new Point(350, BOOHEIGHT);
            enemyPositions[1] = new Point(900, FIREBOOHEIGHT);
            enemyPositions[2] = new Point(1200, GOOMBAHEIGHT);
            enemyPositions[3] = new Point(1900, BOOHEIGHT);
            enemyPositions[4] = new Point(2600, GOOMBAHEIGHT);
            enemyPositions[5] = new Point(3500, FIREBOOHEIGHT);
            enemyPositions[6] = new Point(4175, GOOMBAHEIGHT);
            enemyPositions[7] = new Point(4250, GOOMBAHEIGHT);
            enemyPositions[8] = new Point(5375, BOBOMBHEIGHT);
            break;
        case 9:
            enemyType = new int[] { FLYER, GOOMBA, GOOMBA, KOOPAREDFLYING, KOOPAREDFLYING, KOOPAREDFLYING,
                    KOOPAREDFLYING, KOOPAREDFLYING, CHOMP };

            enemyPositions[0] = new Point(400, FLYERHEIGHT);
            enemyPositions[1] = new Point(950, GOOMBAHEIGHT);
            enemyPositions[2] = new Point(1000, GOOMBAHEIGHT);
            enemyPositions[3] = new Point(1300, KOOPAREDFLYINGHEIGHT - 10);
            enemyPositions[4] = new Point(1500, KOOPAREDFLYINGHEIGHT - 10);
            enemyPositions[5] = new Point(1700, KOOPAREDFLYINGHEIGHT - 10);
            enemyPositions[6] = new Point(1900, KOOPAREDFLYINGHEIGHT - 10);
            enemyPositions[7] = new Point(2100, KOOPAREDFLYINGHEIGHT - 10);
            enemyPositions[8] = new Point(2850, CHOMPHEIGHT);

            for (int i = 0; i < obstacleNumber; i++) {

                piranhaPopping[i] = false;
                hasPiranha[i] = false;
                if (i == 0 || i == 3 || i == 5) {
                    isTall[i] = true;
                } else {
                    if (i == 1 || i == 2 || i == 4) {
                        piranhaPopping[i] = true;
                        hasPiranha[i] = true;
                    }
                }
            }

            obstaclePositions[0] = new Point(200, GROUNDLEVEL);
            obstaclePositions[1] = new Point(900, GROUNDLEVEL);
            obstaclePositions[2] = new Point(1100, GROUNDLEVEL);
            obstaclePositions[3] = new Point(2400, GROUNDLEVEL);
            obstaclePositions[4] = new Point(2550, GROUNDLEVEL);
            obstaclePositions[5] = new Point(2700, GROUNDLEVEL);

        default:

        }

        for (int j = 0; j < enemyNumber; j++) {
            enemyFaceRight(j);
            enemyDead[j] = false;
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
        for (int i = 0; i < enemyNumber; i++) {
            if (isEnemyX(p) != -1 && isEnemyY(p) != -1 && !enemyDead[i])
                return (i);
        }
        return (-1);
    }

    boolean isObstaclePosition(Point p, boolean forMario) {
        int obby;
        for (int i = 0; i < obstacleNumber; i++) {
            obby = isObstacleX(p, forMario);
            if (obby != -1 && isObstacleY(p, obby, forMario) != -1) {
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

    int isObstacleX(Point p, boolean forMario) {
        int obstaclex;
        for (int i = 0; i < obstacleNumber; i++) {
            // if x is >= obstacleposition - (MARIOLEFTOFOBSTACLE) and x <= obstacleposition
            // + (MARIORIGHTOFOBSTACLE)
            obstaclex = obstaclePositions[i].x;
            if (forMario) {
                if (p.x >= (obstaclex - MARIOLEFTOFOBSTACLE) && p.x <= (obstaclex + MARIORIGHTOFOBSTACLE))
                    return (i); // return the number of the matching obstacle
            } else {
                if (p.x >= (obstaclex - ENEMYLEFTOFOBSTACLE) && p.x <= (obstaclex + ENEMYRIGHTOFOBSTACLE))
                    return (i); // return the number of the matching obstacle
            }
        }
        return (-1);
    }

    boolean isBlockingObstacleY(Point p, int obstaclenumber) {
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

    int isObstacleY(Point p, int obstaclenumber, boolean forMario) {
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
        int enemyx;
        for (int i = 0; i < enemyNumber; i++) {
            // if x is >= enemyposition - (LEFTOFENEMY) and x <= enemyposition +
            // (RIGHTOFENEMY)
            enemyx = enemyPositions[i].x;
            // System.out.println("Player got by enemy if " +
            // p.x + " >= " + (enemyx - MARIOLEFTOFENEMY) + " && " + p.x + " <= " + (enemyx
            // + MARIORIGHTOFENEMY));
            if (p.x >= (enemyx - MARIOLEFTOFENEMY) && p.x <= (enemyx + MARIORIGHTOFENEMY) && !enemyDead[i])
                return (i); // return the number of the matching enemy
        }
        return (-1);
    }

    int isEnemyY(Point p) {
        int offset = herocrouching ? MARIOCROUCHOFFSET : MARIOOFFSET;
        for (int i = 0; i < enemyNumber; i++) {
            if (!enemyDead[i] && ((p.y + offset <= enemyPositions[i].y + enemySize[enemyType[i]])
                    && (p.y + MARIOHEIGHT) >= enemyPositions[i].y))
                return (i);
        }
        return (-1);
    }

    public void finished() {
        finished = true;
        System.exit(0);
    }

    public String getAppletInfo() {
        return ("Mario Game");
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
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            displayStatus();
        }
        if (ingame) {
            if (!(e.getKeyCode() == KeyEvent.VK_LEFT && playerPosition.x <= (LEFTEDGE + XAMOUNT))) {

                // valid move: move the player and then move the enemies
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    herocrouching = true;
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
        if (!herocrouching) {
            // if Hero was facing right, turn him left
            if (!heroleft)
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
        if (!herocrouching) {
            if ((playerPosition.x + SPACE_FROM_X_TO_MARIO) >= (warpzone.x + 4)) {
                // level complete!
                ingame = false;
            } else {
                // if Hero was facing left, turn him right
                if (heroleft)
                    faceRight();
                else {
                    for (int i = 0; i < obstacleNumber; i++) {
                        if (isObstaclePosition(new Point(playerPosition.x + XAMOUNT, playerPosition.y), true))
                            return; // can't move
                    }
                    playerPosition.x += XAMOUNT;
                    // System.out.println("Mario's x position is " + playerPosition.x);
                    if (!skid)
                        incrementHeroImage();
                }
            }
        }
    }

    boolean anythingVisibleChanged() {
        return (somethingChangedSinceRepaint);
    }

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

                // for each enemy, move the enemy in the direction it was going
                for (int i = 0; i < enemyNumber; i++) {
                    // TODO if there is a fatal obstacle at that position, fall into it and
                    // decrement count of enemies
                    // and set that enemy's position to -1, -1
                    /*
                     * if (isObstaclePosition(enemyPositions[i])) {
                     * System.out.Println("A enemy fell into a obstacle");
                     * 
                     * enemyPositions[i] = new Point(-1, -1); enemiesRemaining--;
                     * System.out.Println(enemyNumber-enemiesRemaining + " down, " +
                     * enemiesRemaining + " to go..."); }
                     */
                    // if there is a non-fatal obstacle
                    if (isObstaclePosition(enemyPositions[i], false) && enemyType[i] != BANZAI) {
                        // change direction
                        changeDirection(i);
                        if (isVisibleEnemy(i))
                            setSomethingChangedSinceRepaint(true);
                    }
                    if (!enemyDead[i]) {
                        enemyPositions[i] = move(i, enemyPositions[i], enemyDirection[i], odd);
                        if (isVisibleEnemy(i))
                            setSomethingChangedSinceRepaint(true);
                    }

                    // if player is caught by a enemy, end the game
                    int enemyno = isEnemyPosition(playerPosition);
                    if (enemyno > -1) {
                        int NEEDTOBEABOVE = enemyPositions[enemyno].y + enemyTopSpace[enemyno] - MARIOHEIGHT;
                        if (easymode)
                            NEEDTOBEABOVE = enemyPositions[enemyno].y - enemyHeight[enemyType[enemyno]];
                        if ((!onTheWayUp && (playerPosition.y <= NEEDTOBEABOVE)) && enemyType[enemyno] != BANZAI) {
                            // player may have squished an enemy
                            if (enemyType[enemyno] == KOOPAREDFLYING) {
                                enemyType[enemyno] = KOOPARED;
                                enemyPositions[enemyno].y = KOOPAREDHEIGHT;
                                if (enemyDirection[enemyno] == LEFT)
                                    enemyFaceLeft(enemyno);
                                else
                                    enemyFaceRight(enemyno);
                            } else {
                                enemyDead[enemyno] = true;
                                enemyDie(enemyno);
                            }
                            // player bounces
                            bouncing = true;
                            startPosY = playerPosition.y;
                            setSomethingChangedSinceRepaint(true);
                        } else {
                            int offset = herocrouching ? MARIOCROUCHOFFSET : MARIOOFFSET;
                            if (playerPosition.y > NEEDTOBEABOVE && (playerPosition.y
                                    + offset <= enemyPositions[enemyno].y + enemySize[enemyType[enemyno]])) {
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
                                    if (playerPosition.y >= GROUNDLEVEL && floorIsIcy()) {
                                        if (!wasbouncing) {
                                            if (heroleft)
                                                skidLeft();
                                            else
                                                skidRight();
                                        }
                                    }
                                    wasbouncing = false;
                                    setSomethingChangedSinceRepaint(true);
                                } else
                                    jumping = false;
                            }
                        } else {
                            Point obstaclepos = obstaclePositions[obstacleno];
                            // if hero's y position <= obstacle's height
                            int thisObstacleHeight = isTall[obstacleno] ? OBSTACLEHEIGHT + TALLOBSTACLEDIFF
                                    : OBSTACLEHEIGHT;
                            if (playerPosition.y <= (obstaclepos.y - thisObstacleHeight) && // can't land on the
                                                                                            // obstacle if just < (??!)
                                    (playerPosition.y + FALLAMOUNT >= (obstaclepos.y - thisObstacleHeight))) {
                                // allow hero to land on the obstacle
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
                                        if (playerPosition.y >= GROUNDLEVEL && floorIsIcy()) {
                                            if (!wasbouncing) {
                                                if (heroleft)
                                                    skidLeft();
                                                else
                                                    skidRight();
                                            }
                                        }
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
        // TODO - this probably needs fixing when enemy is near page boundary
        return ((enemyPositions[i].x >= page * PAGEWIDTH) && (enemyPositions[i].x < (page + 1) * PAGEWIDTH)
                && !enemyDead[i]);
    }

    void changeDirection(int enemyno) {
        if (enemyDirection[enemyno] == LEFT) {
            enemyFaceRight(enemyno);
        } else {
            enemyFaceLeft(enemyno);
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
        heroImageNo++;
        if (heroImageNo == HEROIMAGES)
            heroImageNo = 0;
    }

    synchronized void incrementEnemyImage(int i) {
        enemyImageNo[i]++;
        if (enemyType[i] == WIGGLER) {
            if (enemyImageNo[i] > (ENEMYIMAGES - 1))
                enemyImageNo[i] = 0;
        } else {
            if (enemyImageNo[i] > (ENEMYIMAGES - 2))
                enemyImageNo[i] = 0;
        }
    }

    Point move(int enemyNo, Point start, int direction, boolean odd) {
        if (odd)
            incrementEnemyImage(enemyNo);
        if (direction == LEFT) {
            return (new Point(start.x - enemyMove[enemyType[enemyNo]], start.y));
        } else {
            return (new Point(start.x + enemyMove[enemyType[enemyNo]], start.y));
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            herocrouching = false;
            decKeysDown(DOWNDOWN);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (floorIsIcy()) {
                skidLeft();
            }
            decKeysDown(LEFTDOWN);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (floorIsIcy()) {
                skidRight();
            }
            decKeysDown(RIGHTDOWN);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    synchronized Image getHeroImage() {
        return (heroImages[heroImageNo]);
    }

    synchronized Image getObstacleImage(int obstaclenum) {
        return (obstacleImages[obstacleImageNo[obstaclenum]]);
    }

    synchronized Image getEnemyImage(int enemyno) {
        return (enemyImage[enemyno][enemyImageNo[enemyno]]);
    }

    boolean floorIsIcy() {
        return (floors[level - 1] == area.icefloor);
    }

    void skidLeft() {
        for (int i = 0; i < 6; i++) {
            leftPressed(true);
            try {
                Thread.sleep(7);
            } catch (Exception e) {
            }
            area.repaint();
        }
    }

    void skidRight() {
        for (int i = 0; i < 6; i++) {
            rightPressed(true);
            area.repaint();
            try {
                Thread.sleep(7);
            } catch (Exception e) {
            }
        }
    }

    void displayStatus() {
        /*
         * // when a certain F Key is pressed, display positions of Mario, // all pipes
         * and all enemies and the end flag (warp zone) System.out.println("--------");
         * System.out.println("Mario is " + (dead? "dead" : "alive")); if (dead) { int
         * enemyno = isEnemyPosition(playerPosition);
         * System.out.println("Mario was killed by enemy " + enemyno); }
         * System.out.println("In game is " + ingame); System.out.println("Mario is " +
         * (onTheWayUp? "on the way up" : "not on the way up"));
         * System.out.println("Mario's position is (" + playerPosition.x + ", " +
         * playerPosition.y + ")");
         * System.out.println("Mario's visible left, bottom co-ordinates are " +
         * (playerPosition.x + 55) + " and " + (playerPosition.y + MARIOHEIGHT));
         * System.out.println("Mario is on page " + page +
         * " (which shows x positions from " + (page*PAGEWIDTH) + " to " +
         * ((page+1)*PAGEWIDTH) + ")"); System.out.println("Screen bounds are (" +
         * getBounds().width + ", " + getBounds().height + ")"); System.out.println("");
         * for (int enemy=0; enemy < enemyNumber; enemy++) { System.out.println("Enemy "
         * + enemy + " is " + enemyTypeString(enemyType[enemy]) + " and is " +
         * (enemyDead[enemy] ? " " : "not ") + "dead"); System.out.println("Enemy " +
         * enemy + " is at position (" + enemyPositions[enemy].x + ", " +
         * enemyPositions[enemy].y+") and has size " + enemySize[enemyType[enemy]]); }
         * //System.out.println(""); //for (int obstacle=0; obstacle < obstacleNumber;
         * obstacle++) { // System.out.println("Obstacle " + obstacle +
         * " is at position (" + obstaclePositions[obstacle].x + // ", " +
         * obstaclePositions[obstacle].y+")"); //} System.out.println("");
         * System.out.println("The warp zone is positioned at (" + warpzone.x + ", " +
         * warpzone.y + ")"); System.out.println("========");
         */
    }

    String enemyTypeString(int fortype) {
        switch (fortype) {
        case GOOMBA:
            return ("GOOMBA");
        case KOOPARED:
            return ("KOOPA RED");
        case KOOPAGREEN:
            return ("KOOPA GREEN");
        case KOOPAREDFLYING:
            return ("KOOPA RED FLYING");
        case BANZAI:
            return ("BANZAI");
        case WIGGLER:
            return ("WIGGLER");
        case BOO:
            return ("BOO");
        case FIREBOO:
            return ("FOREBOO");
        case BOBOMB:
            return ("BOBOMB");
        case BULLET:
            return ("BULLET");
        case WARSHIP:
            return ("WARSHIP");
        case YETI:
            return ("YETI");
        case FLYER:
            return ("FLYER");
        default:
            return ("Unknown type (" + fortype + ")");
        }
    }

    void changePageIfNecessary() {
        if (playerPosition.x == LEFTEDGE)
            return;
        if ((playerPosition.x + SPACE_FROM_X_TO_MARIO) > (page * PAGEWIDTH + RIGHTEDGE - XAMOUNT)) {
            // System.out.println("Change page up: (" + playerPosition.x+ "+" +
            // SPACE_FROM_X_TO_MARIO+ ") > (" + page+ "*" + PAGEWIDTH + "+" + RIGHTEDGE +
            // "-" + XAMOUNT + ")");
            page++;
        } else if ((playerPosition.x + SPACE_FROM_X_TO_MARIO) - (page * PAGEWIDTH) < (LEFTEDGE + XAMOUNT)) {
            // System.out.println("Change page down: (" + playerPosition.x+ "+" +
            // SPACE_FROM_X_TO_MARIO+ ")-(" + page+ "*" + PAGEWIDTH + ") < (" + LEFTEDGE+
            // "+" + XAMOUNT+")");
            page--;
        }
    }
}
