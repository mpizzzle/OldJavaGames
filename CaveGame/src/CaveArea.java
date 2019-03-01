import java.awt.*;
import java.applet.AudioClip;
import java.net.*;

public class CaveArea extends Panel {
    
    CaveGame myApplet = null;
    Image title[] = new Image[10];
    MediaTracker mt=null;
    Image firstRoom, leftDoor, firstCave;
    Image river, bridge;
    Image cavern, hole, rope;
    Image hotRoom, secondCave, gameComplete;
    Image currentImage;
    
    AudioClip ACfirstRoom, ACleftDoor, ACfirstCave;
    AudioClip ACriver, ACbridge;
    AudioClip ACcavern, AChole, ACrope;
    AudioClip AChotRoom, ACsecondCave, ACgameComplete;
    AudioClip currentMusic;
    
    public CaveArea(CaveGame parent) {
        mt=new MediaTracker(parent);
        myApplet = parent;
        
        firstRoom = load(parent,  "1ST ROOM.gif");
        leftDoor = load(parent,  "GAMER PICKS A.gif");
        firstCave = load(parent,  "GAMER PICKS C.gif");
        river = load(parent,  "GAMER PICKS B.gif");
        bridge = load(parent,  "GAMER PICKS B THEN A.gif");
        cavern = load(parent,  "GAMER PICKS B THEN B.gif");
        hole  = load(parent,  "GAMER PICKS B THEN B THEN B.gif");
        rope = load(parent,  "GAMER PICKS B THEN B THEN C.gif");
        hotRoom  = load(parent,  "GAMER PICKS B THEN B THEN A.gif");
        secondCave  = load(parent,  "GAMER PICKS B THEN B THEN A THEN B.gif");
        gameComplete = load(parent,  "GAME COMPLETE.gif");
        
        ACfirstRoom = loadSound(parent,  "The Prophecy.mp3");
        ACleftDoor = loadSound(parent,  "The Black Rider.mp3");
        ACfirstCave = loadSound(parent,  "The Shadow of the Past.mp3");
        ACriver = loadSound(parent,  "The Great River.mp3");
        ACbridge = loadSound(parent,  "Flight to the Ford.mp3");
        ACcavern = loadSound(parent,  "A Journey in the Dark.mp3");
        AChole  = loadSound(parent,  "A Knife in the Dark.mp3");
        ACrope = loadSound(parent,  "Amon Hen.mp3");
        AChotRoom  = loadSound(parent,  "The Bridge of Khazad Dhum.mp3");
        ACsecondCave  = loadSound(parent,  "The Treason of Isengard.mp3");
        ACgameComplete = loadSound(parent,  "Concerning Hobbits.mp3");
    
	}
    
    Image load(CaveGame parent, String picture) {
        Image im    = parent.getImage(myApplet.getCodeBase(), "CaveGame\\" + picture);
        checkImage(im, picture);
        return(im);
    }
    
    AudioClip loadSound(CaveGame parent, String soundfile) {
        AudioClip s = null;
        s = parent.getAudioClip(myApplet.getCodeBase(), "CaveGame\\"+soundfile);
        checkAudio(s, soundfile);
        return (s);
    }
    
    void checkAudio(AudioClip ac, String name) {
        if (ac == null) System.out.println("Audio Clip Not found: "+name.toString());
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
        if (myApplet == null) return;
        final int w = getBounds().width;
        final int h = getBounds().height;
        g.setColor(Color.white);
        FontMetrics fm = getFontMetrics(getFont());
        
        g.setColor(Color.black);
        int asc = fm.getAscent() + 4;

        g.drawImage(currentImage, 0, 0,Color.white, null);
    
    }
    
    public void showTitle(Graphics g) {
        for (int i = 0; i < 10; i++) {
            g.drawImage(title[i], myApplet.getBounds().width/2, getBounds().height/2,Color.white, null);
            try {Thread.sleep(50);}
            catch (Exception e) {}
        }
    }

}