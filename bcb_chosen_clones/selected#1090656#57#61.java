    public static void captureFrame(JFrame frame, String fileName) throws Exception {
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(frame.getBounds());
        writeImage(image, fileName);
    }
