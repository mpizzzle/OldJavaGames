public class Bridge implements CaveInterface {
    CaveGame myParent;
    CaveArea myArea;
    
    public Bridge (CaveGame parent, CaveArea area) {
        myParent = parent;
        myArea = area;
    }
    
    public void display() {
        myArea.currentImage = myArea.bridge;
        myParent.showStatus("On the Bridge");
        if (myArea.currentMusic != null) myArea.currentMusic.stop();
        myArea.currentMusic = myArea.ACbridge;
        if (myArea.currentMusic != null) myArea.currentMusic.loop();
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