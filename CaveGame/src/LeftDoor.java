public class LeftDoor implements CaveInterface {
    CaveGame myParent;
    CaveArea myArea;

    public LeftDoor(CaveGame parent, CaveArea area) {
        myParent = parent;
        myArea = area;
    }

    public void display() {
        myArea.currentImage = myArea.leftDoor;
        /*
         * if (myArea.currentMusic != null) myArea.currentMusic.stop();
         * myArea.currentMusic = myArea.ACleftDoor; if (myArea.currentMusic != null)
         * myArea.currentMusic.loop();
         */
        System.out.println("Through the Left Door");
        myArea.repaint();
    }

    public CaveInterface processA() {
        // do nothing
        return (this);
    }

    public CaveInterface processB() {
        // do nothing
        return (this);
    }

    public CaveInterface processC() {
        // do nothing
        return (this);
    }
}