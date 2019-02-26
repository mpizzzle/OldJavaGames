package Link;

import java.awt.*;

public class LinkArea extends Panel {
    
    static final int FLOORLEVEL = 587;
    static final int FLOORWIDTH = 150;
    LinkGame myApplet = null;
    Image yellowgem;
    Image spider;
    Image obstacle;
    Image redgem;
    Image octopellets;
    Image octo;
    Image madblob;
    Image heroswordright;
    Image heroswordleft;
    Image herostand;
    Image hero1;
    Image hero2;
    Image herostandleft;
    Image hero1left;
    Image hero2left;
    Image heroshieldright;
    Image heroshieldleft;
    Image greengem;
    Image bluegem;
    Image goldskullata;
    Image gemstandgems;
    Image gemstandempty;
    Image blob;
    MediaTracker mt=null;
    
    public LinkArea(LinkGame parent) {
        mt=new MediaTracker(parent);
        myApplet = parent;
        yellowgem      = load(myApplet, "yellowgem.gif");
        spider      = load(myApplet, "spider.gif");
        obstacle      = load(myApplet, "rock.gif");
        redgem      = load(myApplet, "redgem.gif");
        octopellets     = load(myApplet, "octopellets.gif");
        octo     = load(myApplet, "octo.gif");
        madblob     = load(myApplet, "madblob.gif");
        heroswordright     = load(myApplet, "linkswordright.gif");
        heroswordleft     = load(myApplet, "linkswordleft.gif");
        herostand     = load(myApplet, "linkstandright.gif");
        hero1     = load(myApplet, "linkstandright.gif");
        hero2     = load(myApplet, "linkstandright2.gif");
        herostandleft     = load(myApplet, "linkstandleft.gif");
        hero1left     = load(myApplet, "linkstandleft.gif");
        hero2left     = load(myApplet, "linkstandleft2.gif");
        heroshieldright     = load(myApplet, "linkshieldright.gif");
        heroshieldleft     = load(myApplet, "linkshieldleft.gif");
        greengem     = load(myApplet, "greengem.gif");
        bluegem     = load(myApplet, "bluegem.gif");
        goldskullata     = load(myApplet, "goldskullata.gif");
        gemstandgems     = load(myApplet, "gemstand(with gems).gif");
        gemstandempty     = load(myApplet, "gemstand(without gems).gif");
        blob     = load(myApplet, "blob.gif");
    }
    void checkImage(Image image, String name) {
        if (mt != null) {
            mt.addImage(image,0);
            try {
                mt.waitForID(0,5000);
            }
            catch (InterruptedException ie) {
                // nothing to do
            }
            if (mt.isErrorID(0)) System.out.println("Image Not found: "+name.toString());
        }
    }
    
    Image load(LinkGame parent, String picture) {
        Image im    = parent.getImage(myApplet.getCodeBase(), "Link\\" + picture);
        checkImage(im, picture);
        return(im);
    }
    
    public synchronized void paint (Graphics g) {
        Image obstacleimage;
        if (myApplet == null) return;
        final int w = getBounds().width;
        final int h = getBounds().height;
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        FontMetrics fm = getFontMetrics(getFont());
        
        g.setColor(Color.black);
        int asc = fm.getAscent() + 4;
        /*
        // paint the floor
        for (int pos=0; pos < w; pos+=FLOORWIDTH) {
            g.drawImage(floor,pos, FLOORLEVEL,Color.black, null);
        }
        */

        if (myApplet.obstaclePositions != null)  {
            // show the obstacles
            for (int i=0; i < myApplet.obstaclePositions.length; i++) {
                obstacleimage = myApplet.getObstacleImage(i);
                g.drawImage(obstacleimage, myApplet.obstaclePositions[myApplet.level-1][i].x, myApplet.obstaclePositions[myApplet.level-1][i].y-4,Color.white, null);
            }
        }

        /*
            // show the enemies
            for (int i=0; i < myApplet.enemyPositions.length; i++) {
                if (myApplet.enemyPositions[i].x > -1) 
                    g.drawImage(enemyImage, myApplet.enemyPositions[i].x, myApplet.enemyPositions[i].y,null);
            }
        }
        */
        // draw Hero (on top  of preceding images)
        if (myApplet.playerPosition != null) {
            // show the player
            Image herosimage = myApplet.getHeroImage();
            /*
            if (myApplet.herocrouching) { 
                if (myApplet.heroleft) 
                    herosimage = herocrouchleft;
                else 
                    herosimage = herocrouch;
            }
            if (myApplet.jumping) { 
                if (myApplet.heroleft) 
                    herosimage = herojumpleft;
                else 
                    herosimage = herojump;
            }
            */
            g.drawImage(herosimage, myApplet.playerPosition.x, myApplet.playerPosition.y,/*Color.white,*/ null);
        }
        /*
        // draw warp image (on top of Hero)
        if (myApplet.warpzone != null) {
            // show the warp zone at the end of the level
            g.drawImage(herowarp, myApplet.warpzone.x, myApplet.warpzone.y,Color.white, null);
        }
        */
        
    }
}