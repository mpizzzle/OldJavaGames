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
    Zombie myApplet = null;
    Image zombieImage;
    Image personImage;
    Image pitImage;
    private Image explosion1, explosion2, explosion3;
    
    public ZombieArea(Zombie parent) {
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
        super.paintComponent(g);

        if (myApplet == null) return;
        final int w = getBounds().width;
        final int h = getBounds().height;
        g.setColor(Color.gray);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);

        if (Zombie.playerPosition != null) {
            // show the player
            //g.drawString("#", myApplet.playerPosition.x, myApplet.playerPosition.y);
            g.drawImage(personImage, Zombie.playerPosition.x, Zombie.playerPosition.y,Color.gray, null);

            // show the pits
            for (int i=0; i < Zombie.pitPositions.length; i++) {
                g.drawImage(pitImage, Zombie.pitPositions[i].x, Zombie.pitPositions[i].y,Color.gray, null);
            }
        
            // show the zombies
            for (int i=0; i < Zombie.zombiePositions.length; i++) {
                if (Zombie.zombiePositions[i].x > -1) 
                    g.drawImage(zombieImage, Zombie.zombiePositions[i].x, Zombie.zombiePositions[i].y,null);
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
