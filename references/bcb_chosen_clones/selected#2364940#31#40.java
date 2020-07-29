    public static File capture() throws Exception {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRect);
        File file = File.createTempFile("jfx_screen_capture", ".jpg");
        ImageIO.write(image, "jpg", file);
        return file;
    }
