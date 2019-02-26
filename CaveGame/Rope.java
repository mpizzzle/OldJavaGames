package CaveGame;

public class Rope implements CaveInterface {
    CaveGame myParent;
    CaveArea myArea;
    
    public Rope (CaveGame parent, CaveArea area) {
        myParent = parent;
        myArea = area;
    }
    
    public void display() {
        myArea.currentImage = myArea.rope;
        if (myArea.currentMusic != null) myArea.currentMusic.stop();
        myArea.currentMusic = myArea.ACrope;
        if (myArea.currentMusic != null) myArea.currentMusic.loop();
        myParent.showStatus("Down the Rope");
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