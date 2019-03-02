import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MarioArea extends JPanel implements ActionListener {
    private static final long serialVersionUID = 5208087574284410378L;
    static int FLOORLEVEL = 589;
    static final int FLOORWIDTH = 150;
    static int FLOORDIFF = 605 - FLOORLEVEL;
    Mario myApplet = null;
    Image mariotitle;
    Image icefloor;
    Image floor2;
    Image floor3;
    Image floor4;
    Image floor5;
    Image floor6;
    Image floor7;
    Image floor8;
    Image floor9;
    Image mariograss;
    Image upmushroom;
    Image goomba;
    Image goombaleft;
    Image goomba2;
    Image goomba2left;
    Image kooparedf;
    Image koopared;
    Image redshell;
    Image kooparedfleft;
    Image kooparedleft;
    Image koopagreen;
    Image koopagreenleft;
    Image greenshell;
    Image hero1;
    Image hero1left;
    Image hero2;
    Image hero2left;
    Image herocrouch;
    Image herocrouchleft;
    Image herojump;
    Image herojumpleft;
    Image obstacle;
    Image tallobstacle;
    Image herostand;
    Image herostandleft;
    Image piranha1;
    Image piranha2;
    Image piranhapeep;
    Image piranhapeepo;
    Image piranhapeepy;
    Image poison;
    Image Luigi1;
    Image Luigi1left;
    Image Luigi2;
    Image Luigi2left;
    Image Luigicrouch;
    Image Luigiccrouchleft;
    Image Luigijump;
    Image Luigijumpleft;
    Image Luigistand;
    Image Luigistandleft;
    Image herowarp;
    Image levelcomplete;
    Image herodead;
    Image squishedgoomba;
    Image Banzai;
    Image Bullet;
    Image Wiglerleft;
    Image Wiglerright;
    Image Wiglerleft2;
    Image Wiglerright2;
    Image Wiglerleft1;
    Image Wiglerright1;
    Image Boo1;
    Image Boo2;
    Image Fireboo1;
    Image Fireboo2;
    Image Bobomb;
    Image Bobomb2;
    Image Bobombf;
    Image Bobombf2;
    Image Warship1;
    Image Warship2;
    Image Yetil1;
    Image Yetil2;
    Image Yetir1;
    Image Yetir2;
    Image p1fl;
    Image p2fl;
    Image p1fr;
    Image p2fr;
    Image cube;
    Image robot;
    Image chainchomp1;
    Image chainchomp2;
    Image chainchompdead;
    Image Atom1;
    Image Atom2;
    Image Floor10;
    MediaTracker mt = null;
    boolean showingMessage = false;

    public MarioArea(Mario parent) {
        mt = new MediaTracker(parent);
        myApplet = parent;
        upmushroom = load(parent, "1up!mushroom.gif");
        goomba = load(parent, "goomba.gif");
        goombaleft = load(parent, "goombaleft.gif");
        goomba2 = load(parent, "goomba2.gif");
        goomba2left = load(parent, "goombaleft2.gif");
        squishedgoomba = load(parent, "goombasquished.gif");

        kooparedf = load(parent, "koopaparatroopa1.gif");
        koopared = load(parent, "koopaparatroopa2.gif");
        redshell = load(parent, "koopaparatroopashell.gif");

        kooparedfleft = load(parent, "koopaparatroopa1left.gif");
        kooparedleft = load(parent, "koopaparatroopa2left.gif");
        koopagreenleft = load(parent, "koopatroopa.gif");

        koopagreen = load(parent, "koopatroopaleft.gif");
        greenshell = load(parent, "koopatroopashell.gif");

        hero1 = load(parent, "mario1.gif");
        hero1left = load(parent, "mario1left.gif");
        hero2 = load(parent, "mario2.gif");
        hero2left = load(parent, "mario2left.gif");
        herocrouch = load(parent, "mariocrouch.gif");
        herocrouchleft = load(parent, "mariocrouchleft.gif");
        herojump = load(parent, "mariojump.gif");
        herojumpleft = load(parent, "mariojumpleft.gif");
        obstacle = load(parent, "mariopipe.gif");
        tallobstacle = load(parent, "mariopipe2.gif");
        herostand = load(parent, "mariostand.gif");
        herostandleft = load(parent, "mariostandleft.gif");

        piranhapeep = load(parent, "piranhaplanthugepeep.gif");
        piranhapeepo = load(parent, "piranhaplanthugepeepo.gif");
        piranhapeepy = load(parent, "piranhaplanthugepeepy.gif");

        piranha1 = load(parent, "piranhaplant1huge.gif");
        piranha2 = load(parent, "piranhaplant2huge.gif");

        poison = load(parent, "poisonmushroom.gif");
        herowarp = load(parent, "mariowarp.gif");
        icefloor = load(parent, "mariofloor.gif");
        floor2 = load(parent, "mariofloor2.gif");
        floor3 = load(parent, "mariofloor3.gif");
        floor4 = load(parent, "mariofloor4.gif");
        floor5 = load(parent, "mariofloor5.gif");
        floor6 = load(parent, "mariofloor6.gif");
        floor7 = load(parent, "mariofloor7.gif");
        floor8 = load(parent, "mariofloor8.gif");
        floor9 = load(parent, "mariofloor9.gif");
        mariograss = load(parent, "mariograss.gif");
        p1fl = load(parent, "piranhaflying1.gif");
        p2fl = load(parent, "piranhaflying2.gif");
        p1fr = load(parent, "piranharightflying1.gif");
        p2fr = load(parent, "piranharightflying2.gif");

        levelcomplete = load(parent, "missioncomplete.gif");
        herodead = load(parent, "mariodead.gif");

        Banzai = load(parent, "Banzai Bill.gif");
        Bullet = load(parent, "Bullet Bill.gif");

        Wiglerleft = load(parent, "Wigler1Left.gif");
        Wiglerright = load(parent, "Wigler1Right.gif");

        Wiglerleft2 = load(parent, "Wigler2Left.gif");
        Wiglerright2 = load(parent, "Wigler2Right.gif");

        Wiglerleft1 = load(parent, "Wigler1.5Left.gif");
        Wiglerright1 = load(parent, "Wigler1.5Right.gif");

        Boo1 = load(parent, "boo1.gif");
        Boo2 = load(parent, "boo2.gif");
        Fireboo1 = load(parent, "fireboo1.gif");
        Fireboo2 = load(parent, "fireboo2.gif");
        Bobomb = load(parent, "bobomb.gif");
        Bobomb2 = load(parent, "bobomb2.gif");
        Bobombf = load(parent, "bobombr.gif");
        Bobombf2 = load(parent, "bobombf2.gif");
        Warship1 = load(parent, "warship1.gif");
        Warship2 = load(parent, "warship2.gif");

        Yetil1 = load(parent, "yetileft1.gif");
        Yetil2 = load(parent, "yetileft2.gif");
        Yetir1 = load(parent, "yetiright1.gif");
        Yetir2 = load(parent, "yetiright2.gif");
        mariotitle = load(parent, "mariotitle.gif");
        cube = load(parent, "cube.gif");
        chainchomp1 = load(parent, "Chain Chomp!!!.gif");
        chainchomp2 = load(parent, "Chain Chomp2!!!.gif");
        robot = load(parent, "Robot.gif");
        chainchompdead = load(parent, "Chain Chomp Dead!!!.gif");

        Atom1 = load(parent, "Atomic Atom1.gif");
        Atom2 = load(parent, "Atomic Atom2.gif");

        Floor10 = load(parent, "mariofloor10.gif");
    }

    Image load(Mario parent, String picture) {
        try {
            Image im = ImageIO.read(new File("../Assets/" + picture));
            checkImage(im, picture);
            return (im);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    void checkImage(Image image, String name) {
        if (mt != null) {
            mt.addImage(image, 0);
            try {
                mt.waitForID(0, 5000);
            } catch (InterruptedException ie) {
                // nothing to do
            }
            if (mt.isErrorID(0))
                System.out.println("Image Not found: " + name.toString());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image obstacleimage, enemyimage;
        if (myApplet == null || Fireboo2 == null)
            return;
        final int w = getBounds().width;
        //final int h = getBounds().height;
        g.setColor(Color.white);
        g.fillRect(0, 0, w, FLOORLEVEL); // don't white wash the floor!

        g.setColor(Color.black);

        // paint the floor
        if (Mario.floors[Mario.level - 1] != null) {
            for (int pos = 0; pos < w; pos += FLOORWIDTH) {
                g.drawImage(Mario.floors[Mario.level - 1], pos, FLOORLEVEL, Color.black, null);
            }
        }
        if (!Mario.juststarted) {
            if (Mario.obstaclePositions != null) {
                myApplet.changePageIfNecessary();
                // show the obstacles from right to left (because pipes have white space to
                // their left but not their right)
                for (int i = Mario.obstacleNumber - 1; i >= 0; i--) {
                    obstacleimage = myApplet.getObstacleImage(i);
                    if (Mario.isTall[i])
                        obstacleimage = tallobstacle; // only one image for this for now
                    g.drawImage(obstacleimage,
                            Mario.obstaclePositions[i].x - (Mario.page * Mario.PAGEWIDTH),
                            Mario.obstaclePositions[i].y, Color.white, null);
                }
            }

            // show the enemies
            if (Mario.enemyPositions != null) {
                for (int i = 0; i < Mario.enemyNumber; i++) {
                    // if (!myApplet.enemyDead[i]) {
                    enemyimage = myApplet.getEnemyImage(i);
                    g.drawImage(enemyimage, Mario.enemyPositions[i].x - (Mario.page * Mario.PAGEWIDTH),
                            Mario.enemyPositions[i].y, null);
                    // }
                }
            }

            // draw Hero (on top of preceding images)
            if (Mario.playerPosition != null) {
                // show the player
                Image herosimage = myApplet.getHeroImage();
                if (Mario.herocrouching) {
                    if (Mario.heroleft)
                        herosimage = herocrouchleft;
                    else
                        herosimage = herocrouch;
                }
                if (Mario.jumping) {
                    if (Mario.heroleft)
                        herosimage = herojumpleft;
                    else
                        herosimage = herojump;
                }
                g.drawImage(herosimage, Mario.playerPosition.x - (Mario.page * Mario.PAGEWIDTH),
                        Mario.playerPosition.y, /* Color.white, */ null);
            }
        }

        // draw warp image (on top of Hero)
        if (Mario.warpzone != null) {
            // show the warp zone at the end of the level
            g.drawImage(herowarp, Mario.warpzone.x - (Mario.page * Mario.PAGEWIDTH), Mario.warpzone.y,
                    Color.white, null);
        }

        if (Mario.ingame == false && !Mario.juststarted) {
            if (Mario.dead) {
                // Hero is dead
                g.drawImage(herodead, myApplet.getBounds().width / 2, getBounds().height / 2, Color.white, null);
            } else {
                // level complete
                g.drawImage(levelcomplete, myApplet.getBounds().width / 2, getBounds().height / 2, Color.white, null);
            }
            showingMessage = true;
        } else
            showingMessage = false;

        myApplet.setSomethingChangedSinceRepaint(false);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        repaint();
    }
}
