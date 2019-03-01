public class WayOut implements CaveInterface {
    CaveGame myParent;
    CaveArea myArea;
    
    public WayOut (CaveGame parent, CaveArea area) {
        myParent = parent;
        myArea = area;
    }
    
    public void display() {
        myArea.currentImage = myArea.gameComplete;
        if (myArea.currentMusic != null) myArea.currentMusic.stop();
        myArea.currentMusic = myArea.ACgameComplete;
        if (myArea.currentMusic != null) myArea.currentMusic.loop();
        myParent.showStatus("At the Exit");
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