    public void saveImage() {
        String title = makeTitle();
        if (title == null) {
            cancelSaveOperations();
            return;
        }
        Rectangle screenRect = this.getBounds();
        int topAdjust = 23;
        int y = screenRect.y;
        screenRect.y += topAdjust;
        int height = screenRect.height;
        screenRect.height -= topAdjust;
        Robot robot = null;
        BufferedImage image = null;
        if (iUseRobot) {
            try {
                robot = new Robot();
            } catch (AWTException e) {
                println("EXCEPTION -- NO ROBOT -- NOT SAVING");
                iSaveInProcess = false;
                iSaveImage = false;
                iAceTree.iAceMenuBar.resetSaveState();
                return;
            }
            image = robot.createScreenCapture(screenRect);
        } else {
            image = BufferedImageCreator.create((ColorProcessor) iImgPlus.getProcessor());
        }
        try {
            ImageIO.write(image, "jpeg", new File(title));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("file: " + title + " written");
        iSaveInProcess = false;
    }
