    public void screenCapture(String fullyQualifiedFileName, String format) throws Exception {
        maximize();
        Robot robot = new Robot();
        Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage bufferedImage = robot.createScreenCapture(captureSize);
        ImageIO.write(bufferedImage, format, new File(fullyQualifiedFileName));
        restore();
    }
