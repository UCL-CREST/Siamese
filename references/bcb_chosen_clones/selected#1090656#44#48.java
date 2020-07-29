    public static void captureBounds(Rectangle bounds, String fileName) throws Exception {
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(bounds);
        writeImage(image, fileName);
    }
