package CaveGame;

public class FirstRoom implements CaveInterface{
    CaveGame myParent;
    CaveArea myArea;
    
    public FirstRoom(CaveGame parent, CaveArea area) {
        myParent = parent;
        myArea = area;
    }
    
    public void display() {
        myArea.currentImage = myArea.firstRoom;
        if (myArea.currentMusic != null) myArea.currentMusic.stop();
        myArea.currentMusic = myArea.ACfirstRoom;
        if (myArea.currentMusic != null) myArea.currentMusic.loop();
        myParent.showStatus("Just Inside the Entrance");
        myArea.repaint();
    }
    
    public CaveInterface processA() {
        return new LeftDoor(myParent, myArea);
    }
    
    public CaveInterface processB() {
        return new River(myParent, myArea);
    }
    
    public CaveInterface processC() {
        return new FirstCave(myParent, myArea);
    }
}