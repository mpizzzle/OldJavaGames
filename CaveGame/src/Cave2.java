public class Cave2 implements CaveInterface {
    CaveGame myParent;
    CaveArea myArea;
    
    public Cave2 (CaveGame parent, CaveArea area) {
        myParent = parent;
        myArea = area;
    }
    
    public void display() {
        myArea.currentImage = myArea.secondCave;
        myParent.showStatus("In the second Cave");
        if (myArea.currentMusic != null) myArea.currentMusic.stop();
        myArea.currentMusic = myArea.ACsecondCave;
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