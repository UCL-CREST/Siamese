    public void capture(int isSingleSnap) {
        setFilename(isSingleSnap);
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRect);
            ImageIO.write(image, fileType, new File(filePath));
            oLogger.log("Screen Shot Captured and written to :" + filePath);
            SCREENSHOT_TAKEN = true;
        } catch (Exception ex) {
            System.out.println("capture : " + ex);
            oLogger.log("Failed to write Screen Shot to :" + filePath + " because : \n" + ex);
            SCREENSHOT_TAKEN = false;
        }
    }
