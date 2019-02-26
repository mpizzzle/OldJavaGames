package CaveGame;

public class Hole implements CaveInterface {
    CaveGame myParent;
    CaveArea myArea;
    
    public Hole (CaveGame parent, CaveArea area) {
        myParent = parent;
        myArea = area;
    }
    
    public void display() {
        myArea.currentImage = myArea.hole;
        if (myArea.currentMusic != null) myArea.currentMusic.stop();
        myArea.currentMusic = myArea.AChole;
        if (myArea.currentMusic != null) myArea.currentMusic.loop();
        myParent.showStatus("In the Hole");
        myArea.repaint();
    }
    
    public CaveInterface processA() {
        // do nothing
        return(this);
    }
    
    public CaveInterface processB() {
        // do nothing
        return(this);
    }
    
    public CaveInterface processC() {
        // do nothing
        return(this);
    }
}