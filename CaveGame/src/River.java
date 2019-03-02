public class River implements CaveInterface {
    CaveGame myParent;
    CaveArea myArea;

    public River(CaveGame parent, CaveArea area) {
        myParent = parent;
        myArea = area;
    }

    public void display() {
        myArea.currentImage = myArea.river;
        /*
         * if (myArea.currentMusic != null) myArea.currentMusic.stop();
         * myArea.currentMusic = myArea.ACriver; if (myArea.currentMusic != null)
         * myArea.currentMusic.loop();
         */
        System.out.println("By the River");
        myArea.repaint();
    }

    public CaveInterface processA() {
        return (new Bridge(myParent, myArea));
    }

    public CaveInterface processB() {
        return (new Cavern(myParent, myArea));
    }

    public CaveInterface processC() {
        // do nothing
        return (this);
    }
}