    public ScreenShotTest() {
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRect);
            ImageIO.write(image, "jpg", new File("out\\out.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(ScreenShotTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AWTException ex) {
            Logger.getLogger(ScreenShotTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
