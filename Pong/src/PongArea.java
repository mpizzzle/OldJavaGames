import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PongArea extends JPanel implements ActionListener {
    private static final long serialVersionUID = -842988339431434549L;
    Pong myApplet = null;
    Image paddle;
    Color[] colours = new Color[] { Color.white, Color.blue, Color.green, Color.red, Color.yellow };
    int colourno = 0;

    public PongArea(Pong parent) {
        myApplet = parent;
        try {
            paddle = ImageIO.read(new File("../Assets/paddle.gif"));
        } catch (IOException ex) {
            Logger.getLogger(PongArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (myApplet == null)
            return;

        final int w = getBounds().width;
        final int h = getBounds().height;
        g.setColor(Color.gray);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);

        if (Pong.playerPosition != null) {
            // show the player's paddle
            // g.drawString("|", myApplet.playerPosition.x, myApplet.playerPosition.y);
            g.drawImage(paddle, Pong.playerPosition.x, Pong.playerPosition.y, Color.black, null);

            // show the ball
            colourno = 0;// <-------remove this for confusion mode!
            for (int i = 0; i < Pong.ballPositions.length; i++) {
                if (Pong.ballPositions[i].x > -1)
                    // g.drawImage(pongImage, myApplet.ballPositions[i].x,
                    // myApplet.ballPositions[i].y,null);
                    g.setColor(colours[colourno++]);
                if (colourno >= colours.length)
                    colourno = 0;

                g.drawString("O", Pong.ballPositions[i].x, Pong.ballPositions[i].y);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
