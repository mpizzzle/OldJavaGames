import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ZombieArea extends JPanel implements ActionListener {
    private static final long serialVersionUID = 8492549939914174796L;
    ZombieGame myApplet = null;
    Image zombieImage;
    Image personImage;
    Image pitImage;
    private Image explosion1, explosion2, explosion3;
    
    public ZombieArea(ZombieGame parent) {
        myApplet = parent;

        try {
            zombieImage= ImageIO.read(new File("../Assets/zombie.gif"));
            personImage = ImageIO.read(new File("../Assets/person.gif"));
            pitImage = ImageIO.read(new File("../Assets/pit.gif"));
            explosion1 = ImageIO.read(new File("../Assets/explosion1.gif"));
            explosion2 = ImageIO.read(new File("../Assets/explosion2.gif"));
            explosion3 = ImageIO.read(new File("../Assets/explosion3.gif"));
        } catch (IOException ex) {
            Logger.getLogger(ZombieArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if (myApplet == null) return;
        final int w = getBounds().width;
        final int h = getBounds().height;
        g.setColor(Color.gray);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);

        if (ZombieGame.playerPosition != null) {
            // show the player
            //g.drawString("#", myApplet.playerPosition.x, myApplet.playerPosition.y);
            g.drawImage(personImage, ZombieGame.playerPosition.x, ZombieGame.playerPosition.y,Color.gray, null);

            // show the pits
            for (int i=0; i < ZombieGame.pitPositions.length; i++) {
                g.drawImage(pitImage, ZombieGame.pitPositions[i].x, ZombieGame.pitPositions[i].y,Color.gray, null);
            }
        
            // show the zombies
            for (int i=0; i < ZombieGame.zombiePositions.length; i++) {
                if (ZombieGame.zombiePositions[i].x > -1) 
                    g.drawImage(zombieImage, ZombieGame.zombiePositions[i].x, ZombieGame.zombiePositions[i].y,null);
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

    @Override
    public void actionPerformed(ActionEvent arg0) {
        repaint();
    }
}
