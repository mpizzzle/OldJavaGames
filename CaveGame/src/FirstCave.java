public class FirstCave implements CaveInterface {
    CaveGame myParent;
    CaveArea myArea;
    
    public FirstCave (CaveGame parent, CaveArea area) {
        myParent = parent;
        myArea = area;
    }
    
    public void display() {
        myArea.currentImage = myArea.firstCave;
        if (myArea.currentMusic != null) myArea.currentMusic.stop();
        myArea.currentMusic = myArea.ACfirstCave;
        if (myArea.currentMusic != null) myArea.currentMusic.loop();
        myParent.showStatus("In the First Cave");
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