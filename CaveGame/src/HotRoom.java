public class HotRoom implements CaveInterface {
    CaveGame myParent;
    CaveArea myArea;

    public HotRoom(CaveGame parent, CaveArea area) {
        myParent = parent;
        myArea = area;
    }

    public void display() {
        myArea.currentImage = myArea.hotRoom;
        /*
         * if (myArea.currentMusic != null) myArea.currentMusic.stop();
         * myArea.currentMusic = myArea.AChotRoom; if (myArea.currentMusic != null)
         * myArea.currentMusic.loop();
         */
        System.out.println("In the Hot Room");
        myArea.repaint();
    }

    public CaveInterface processA() {
        return (new WayOut(myParent, myArea));
    }

    public CaveInterface processB() {
        return (new Cave2(myParent, myArea));
    }

    public CaveInterface processC() {
        // do nothing
        return (this);
    }
}