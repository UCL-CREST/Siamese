    public static File capture(Rectangle rect) throws Exception {
        Robot robot = new Robot();
        BufferedImage img = robot.createScreenCapture(rect);
        File file = File.createTempFile("jfx_screen_capture_retouched", ".jpg");
        ImageIO.write(img, "jpg", file);
        return file;
    }
