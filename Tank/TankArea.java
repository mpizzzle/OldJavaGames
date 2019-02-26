package Tank;

import java.awt.*;

public class TankArea extends Panel {
    TankGame myApplet = null;

Image badtankdown2;
Image badtankdownshoot;
Image badtankdownshoot2;
Image badtankdown;
Image badtankleft2;
Image badtankleftshoot2;
Image badtankleftshoot;
Image badtankleft ;
Image badtankright2;
Image badtankrightshoot2;
Image badtankrightshoot;
Image badtankright;
Image badtankup2;
Image badtankupshoot2;
Image badtankupshoot;
Image badtankup;
Image tankdown2;
Image tankdownshoot2;
Image tankdownshoot;
Image tankdown;
Image tankleft2;
Image tankleftshoot2;
Image tankleftshoot;
Image tankleft;
Image tankright2;
Image tankrightshoot2;
Image tankrightshoot;
Image tankright;
Image tankup2;
Image tankupshoot2;
Image tankupshoot;
Image tankup; 
Image enemyImage;
Image personImage;
Image pitImage;
    
    private Image explosion1, explosion2, explosion3;
    MediaTracker mt=null;
    
    public TankArea(TankGame parent) {
        myApplet = parent;
        enemyImage = load(myApplet, "bad-tank-up.gif");
        personImage = load(myApplet, "tank-up.gif");
        pitImage    = load(myApplet, "pit.gif");
        explosion1 = load(myApplet, "explosion1.gif");
        explosion2 = load(myApplet, "explosion2.gif");
        explosion3 = load(myApplet, "explosion3.gif");
        badtankdown2 = load(myApplet, "bad-tank-down-2.gif");
        badtankdownshoot = load(myApplet, "bad-tank-down-shoot.gif");
        badtankdownshoot2 = load(myApplet, "bad-tank-down-shoot-2.gif");
        badtankdown = load(myApplet, " bad-tank-down.gif");
        badtankleft2 = load(myApplet, "bad-tank-left-2.gif");
        badtankleftshoot2 = load(myApplet, "bad-tank-left-shoot-2.gif");
        badtankleftshoot = load(myApplet, "bad-tank-left-shoot.gif");
        badtankleft = load(myApplet, "bad-tank-left.gif");
        badtankright2 = load(myApplet, "bad-tank-right-2.gif");
        badtankrightshoot2 = load(myApplet, "bad-tank-right-shoot-2.gif");
        badtankrightshoot = load(myApplet, "bad-tank-right-shoot.gif");
        badtankright = load(myApplet, "bad-tank-right.gif");
        badtankup2 = load(myApplet, "bad-tank-up-2.gif");
        badtankupshoot2 = load(myApplet, "bad-tank-up-shoot-2.gif");
        badtankupshoot = load(myApplet, "bad-tank-up-shoot.gif");
        badtankup = load(myApplet, "bad-tank-up.gif");
        tankdown2 = load(myApplet, "tank-down-2.gif");
        tankdownshoot2 = load(myApplet, "tank-down-shoot-2.gif");
        tankdownshoot = load(myApplet, "tank-down-shoot.gif");
        tankdown = load(myApplet, "tank-down.gif");
        tankleft2= load(myApplet, "tank-left-2.gif");
        tankleftshoot2 = load(myApplet, "tank-left-shoot-2.gif");
        tankleftshoot = load(myApplet, "tank-left-shoot.gif");
        tankleft = load(myApplet, "tank-left.gif");
        tankright2 = load(myApplet, "tank-right-2.gif");
        tankrightshoot2 = load(myApplet, "tankright-shoot-2.gif");
        tankrightshoot = load(myApplet, "tank-right-shoot.gif");
        tankright = load(myApplet, "tank-right.gif");
        tankup2 = load(myApplet, "tank-up-2.gif");
        tankupshoot2 = load(myApplet, "tank-up-shoot-2.gif");
        tankupshoot = load(myApplet, "tank-up-shoot.gif");
        tankup = load(myApplet, "tank-up.gif");
    }
    
    Image load(TankGame parent, String picture) {
        Image im    = parent.getImage(myApplet.getCodeBase(), "Tank\\" + picture);
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
    
    public void paint (Graphics g) {
        if (myApplet == null) return;
        final int w = getBounds().width;
        final int h = getBounds().height;
        g.setColor(Color.gray);
        g.fillRect(0, 0, w, h);
        FontMetrics fm = getFontMetrics(getFont());
        
        g.setColor(Color.black);
        int asc = fm.getAscent() + 4;

        if (myApplet.playerPosition != null) {
            // show the player
            //g.drawString("#", myApplet.playerPosition.x, myApplet.playerPosition.y);
            int elem = myApplet.shooting ? myApplet.SHOOTING : myApplet.NOTSHOOTING;
            g.drawImage(myApplet.goodImages[myApplet.goodImageNo][elem], myApplet.playerPosition.x, myApplet.playerPosition.y,Color.gray, null);

            // show the pits
            for (int i=0; i < myApplet.pitPositions.length; i++) {
                g.drawImage(pitImage, myApplet.pitPositions[i].x, myApplet.pitPositions[i].y,Color.gray, null);
            }
        
            // show the enemies
            for (int i=0; i < myApplet.enemyPositions.length; i++) {
                if (myApplet.enemyPositions[i].x > -1) {
                    int eelem = myApplet.enemyShooting[i] ? myApplet.SHOOTING : myApplet.NOTSHOOTING;
                    g.drawImage(myApplet.badImages[myApplet.enemyImageNo[i]][eelem], myApplet.enemyPositions[i].x, myApplet.enemyPositions[i].y,null);
                }
            }
            if (myApplet.explosionx != -1) {
                g.drawImage(explosion1, myApplet.explosionx, myApplet.explosiony,Color.gray, null);
                try {
                    Thread.sleep(80);
                    g.drawImage(explosion2, myApplet.explosionx, myApplet.explosiony,Color.gray, null);
                    Thread.sleep(80);
                    g.drawImage(explosion3, myApplet.explosionx, myApplet.explosiony,Color.gray, null);
                }
                catch (Exception e) {}
                myApplet.explosionx = -1;
                myApplet.explosiony=-1;
            }
        }
    }
}