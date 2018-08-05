    public boolean takeScreenshot(int screenNo, String filename) {
        File f = new File(folderpath);
        if (!f.isDirectory() && !f.mkdir()) {
            return false;
        }
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] screens = ge.getScreenDevices();
            Robot robot = new Robot(screens[screenNo - 1]);
            Rectangle screenRectangle = screens[screenNo - 1].getDefaultConfiguration().getBounds();
            screenRectangle.x = 0;
            screenRectangle.y = 0;
            BufferedImage image = robot.createScreenCapture(screenRectangle);
            int height = (int) (screenRectangle.height * 0.86);
            BufferedImage dest = image.getSubimage(0, (int) (screenRectangle.height * 0.135), screenRectangle.width, height);
            return ImageIO.write(dest, "png", new File(folderpath + filename));
        } catch (Exception ex) {
            Logger.getLogger(Screenshot.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
