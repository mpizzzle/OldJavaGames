package Reaper;

import java.awt.*;

public class ReaperArea extends Panel {
    
    static int FLOORLEVEL = 589;
    static final int FLOORWIDTH = 150;
    static int FLOORDIFF = 605-FLOORLEVEL;
    ReaperGame myApplet = null;
    Image reaperwarp;
    Image reaper1;
    Image reaper1left;
    Image reaper2;
    Image reaper2left;
    Image reapercrouch;
    Image reapercrouchleft;
    Image reaperjump;
    Image reaperjumpleft;
    Image obstacle;
    Image tallobstacle;
    Image reaperstand;
    Image reaperstandleft;
    Image levelcomplete;
    Image reaperdead;
    Image squishedrunner;
    Image runner, runner2, runnerleft, runner2left;
    Image title[] = new Image[10];
    MediaTracker mt=null;
    boolean showingMessage = false;
    boolean starting=true;
    
    public ReaperArea(ReaperGame parent) {
        mt=new MediaTracker(parent);
        myApplet = parent;
        
        runner = load(parent,  "run1.gif");
        runnerleft = load(parent,  "run1left.gif");
        runner2 = load(parent,  "run2.gif");
        runner2left = load(parent,  "run2left.gif");
        
        squishedrunner = load(parent,  "run1.gif");
                
        reaper1 = load(parent,  "grimreaper.gif");
        reaper1left = load(parent,  "grimreaper.gif");
        reaper2 = load(parent,  "grimreaper.gif");
        reaper2left = load(parent,  "grimreaper.gif");
        reapercrouch = load(parent,  "grimreaper.gif");
        reapercrouchleft = load(parent,  "grimreaper.gif");
        reaperjump = load(parent, "grimreaper.gif");
        reaperjumpleft = load(parent,  "grimreaper.gif");
        
        obstacle = load(parent,  "fire hydrant.gif");
        tallobstacle = load(parent,  "bus.gif");
        reaperstand = load(parent,  "grimreaper.gif");
        reaperstandleft = load(parent,  "grimreaper.gif");
        
        levelcomplete = load(parent,  "missioncomplete.gif");
        reaperdead = load(parent,  "reaperdead.gif");
        reaperwarp = load(parent,  "warp.gif");

        title[0] = load(parent,  "g.gif");
        title[1] = load(parent,  "gr.gif");
        title[2] = load(parent,  "gri.gif");
        title[3] = load(parent,  "grim.gif");
        title[4] = load(parent,  "grimr.gif");
        title[5] = load(parent,  "grimre.gif");
        title[6] = load(parent,  "grimrea.gif");
        title[7] = load(parent,  "grimreap.gif");
        title[8] = load(parent,  "grimreape.gif");
        title[9] = load(parent,  "grimreaperwriting.gif");
        
    }
    
    Image load(ReaperGame parent, String picture) {
        Image im    = parent.getImage(myApplet.getCodeBase(), "Reaper\\" + picture);
        checkImage(im, picture);
        return(im);
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
    
    public synchronized void paint (Graphics g) {
        Image obstacleimage, preyimage;
        if (myApplet == null) return;
        final int w = getBounds().width;
        final int h = getBounds().height;
        g.setColor(Color.white);
        g.fillRect(0, 0, w, FLOORLEVEL); // don't white wash the floor!
        FontMetrics fm = getFontMetrics(getFont());
        
        g.setColor(Color.black);
        int asc = fm.getAscent() + 4;

        // paint the floor
        if (myApplet.floors[myApplet.level-1] != null) {
            for (int pos=0; pos < w; pos+=FLOORWIDTH) {
                g.drawImage(myApplet.floors[myApplet.level-1],pos, FLOORLEVEL,Color.black, null);
            }
        }
        if (!myApplet.juststarted) {
            if (myApplet.obstaclePositions != null)  {
                myApplet.changePageIfNecessary();
                // show the obstacles from right to left (because obstacles have white space to their left but not their right)
                for (int i=myApplet.obstacleNumber-1; i >= 0; i--) {
                    obstacleimage = myApplet.getObstacleImage(i);
                    if (myApplet.isTall[i]) obstacleimage = tallobstacle; // only one image for this for now
                    g.drawImage(obstacleimage, myApplet.obstaclePositions[i].x-(myApplet.page*myApplet.PAGEWIDTH), myApplet.obstaclePositions[i].y,Color.white, null);
                }
            }

            // show the prey
            if (myApplet.preyPositions != null)  {
                for (int i=0; i < myApplet.preyNumber; i++) {
                    //if (!myApplet.preyDead[i]) {
                        preyimage = myApplet.getEnemyImage(i);
                        g.drawImage(preyimage, myApplet.preyPositions[i].x-(myApplet.page*myApplet.PAGEWIDTH), myApplet.preyPositions[i].y,null);
                    //}
                }
            }

            // draw Hero (on top  of preceding images)
            if (myApplet.playerPosition != null) {
                // show the player
                Image reapersimage = myApplet.getHeroImage();
                if (myApplet.reapercrouching) { 
                    if (myApplet.reaperleft) 
                        reapersimage = reapercrouchleft;
                    else 
                        reapersimage = reapercrouch;
                }
                if (myApplet.jumping) { 
                    if (myApplet.reaperleft) 
                        reapersimage = reaperjumpleft;
                    else 
                        reapersimage = reaperjump;
                }
                g.drawImage(reapersimage, myApplet.playerPosition.x-(myApplet.page*myApplet.PAGEWIDTH), myApplet.playerPosition.y,/*Color.white,*/ null);
            }
        }
        
        // draw warp image (on top of Hero)
        if (myApplet.warpzone != null) {
            // show the warp zone at the end of the level
            g.drawImage(reaperwarp, myApplet.warpzone.x-(myApplet.page*myApplet.PAGEWIDTH), myApplet.warpzone.y,Color.white, null);
        }
        
        if (myApplet.ingame == false && !myApplet.juststarted) {
            if (myApplet.dead) {
                // Reaper is dead (?!)
                g.drawImage(reaperdead, myApplet.getBounds().width/2, getBounds().height/2,Color.white, null);
            }
            else {
                // level complete
                g.drawImage(levelcomplete, myApplet.getBounds().width/2, getBounds().height/2,Color.white, null);
            }
            showingMessage=true;
        }
        else
            showingMessage = false;
    if (starting) {
        showTitle(g);
        starting=false;
    }
    
    myApplet.setSomethingChangedSinceRepaint(false);
    }
    
    public void showTitle(Graphics g) {
        for (int i = 0; i < 10; i++) {
            g.drawImage(title[i], myApplet.getBounds().width/2, getBounds().height/2,Color.white, null);
            try {Thread.sleep(50);}
            catch (Exception e) {}
        }
    }
}