public class Cavern implements CaveInterface {
    CaveGame myParent;
    CaveArea myArea;
    
    public Cavern (CaveGame parent, CaveArea area) {
        myParent = parent;
        myArea = area;
    }
    
    public void display() {
        myArea.currentImage = myArea.cavern;
        myParent.showStatus("In the Cavern");
        if (myArea.currentMusic != null) myArea.currentMusic.stop();
        myArea.currentMusic = myArea.ACcavern;
        if (myArea.currentMusic != null) myArea.currentMusic.loop();
        myArea.repaint();
    }
    
    public CaveInterface processA() {
        return(new HotRoom(myParent, myArea));
    }
    
    public CaveInterface processB() {
        return(new Hole(myParent, myArea));
    }
    
    public CaveInterface processC() {
        return(new Rope(myParent, myArea));
    }
}