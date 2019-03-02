public class Bridge implements CaveInterface {
    CaveGame myParent;
    CaveArea myArea;

    public Bridge(CaveGame parent, CaveArea area) {
        myParent = parent;
        myArea = area;
    }

    public void display() {
        myArea.currentImage = myArea.bridge;
        System.out.println("On the Bridge");
        /*
         * if (myarea.currentmusic != null) myarea.currentmusic.stop();
         * myarea.currentmusic = myarea.acbridge; if (myarea.currentmusic != null)
         * myarea.currentmusic.loop();
         */
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