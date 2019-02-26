package Zombie;

import java.awt.*;

public class ZombieArea extends Panel {
    ZombieGame myApplet = null;
    Image zombieImage;
    Image personImage;
    Image pitImage;
    private Image explosion1, explosion2, explosion3;
    
    public ZombieArea(ZombieGame parent) {
        myApplet = parent;
        zombieImage = parent.getImage(myApplet.getCodeBase(), "Assets\\" + "zombie.gif");
        personImage = parent.getImage(myApplet.getCodeBase(), "Assets\\" + "person.gif");
        pitImage    = parent.getImage(myApplet.getCodeBase(), "Assets\\" + "pit.gif");
        explosion1 = parent.getImage(myApplet.getCodeBase(), "Assets\\" + "explosion1.gif");
        explosion2 = parent.getImage(myApplet.getCodeBase(), "Assets\\" + "explosion2.gif");
        explosion3 = parent.getImage(myApplet.getCodeBase(), "Assets\\" + "explosion3.gif");
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
            g.drawImage(personImage, myApplet.playerPosition.x, myApplet.playerPosition.y,Color.gray, null);

            // show the pits
            for (int i=0; i < myApplet.pitPositions.length; i++) {
                g.drawImage(pitImage, myApplet.pitPositions[i].x, myApplet.pitPositions[i].y,Color.gray, null);
            }
        
            // show the zombies
            for (int i=0; i < myApplet.zombiePositions.length; i++) {
                if (myApplet.zombiePositions[i].x > -1) 
                    g.drawImage(zombieImage, myApplet.zombiePositions[i].x, myApplet.zombiePositions[i].y,null);
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
