    public void saveImage() {
        String saveDir = iAceTree.iImgWin.getSaveImageDirectory();
        if (saveDir == null) {
            iAceTree.iImgWin.cancelSaveOperations();
            iSaveImage = false;
            return;
        }
        Rectangle screenRect = iFrame.getBounds();
        int topAdjust = 23;
        int y = screenRect.y;
        screenRect.y += topAdjust;
        int height = screenRect.height;
        screenRect.height -= topAdjust;
        String title = saveDir + "/";
        Robot robot = null;
        try {
            robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRect);
            title += iTitle + "." + IMAGETYPE;
            ImageIO.write(image, IMAGETYPE, new File(title));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("file: " + title + " written");
        iSaveInProcess = false;
    }
