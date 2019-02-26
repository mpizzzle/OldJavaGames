package Pong;

import java.awt.*;

public class PongArea extends Panel {
    PongGame myApplet = null;
    Image paddle;
    Color[] colours = new Color[] {Color.white, Color.blue, Color.green, Color.red, Color.yellow};
    int colourno=0;
    
    public PongArea(PongGame parent) {
        myApplet = parent;
        paddle    = parent.getImage(myApplet.getCodeBase(), "Pong\\" + "paddle.gif");
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
            // show the player's paddle
            //g.drawString("|", myApplet.playerPosition.x, myApplet.playerPosition.y);
            g.drawImage(paddle, myApplet.playerPosition.x, myApplet.playerPosition.y,Color.gray, null);

            // show the ball
            colourno=0;//<-------remove this for confusion mode!
            for (int i=0; i < myApplet.ballPositions.length; i++) {
                if (myApplet.ballPositions[i].x > -1) 
                    //g.drawImage(pongImage, myApplet.ballPositions[i].x, myApplet.ballPositions[i].y,null);
                    g.setColor(colours[colourno++]);
                    if (colourno >= colours.length) colourno = 0;
                    g.drawString("O", myApplet.ballPositions[i].x, myApplet.ballPositions[i].y);
            }
        }
    }
}