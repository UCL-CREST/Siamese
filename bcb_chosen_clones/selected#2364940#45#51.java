    public static File capture(Video video) throws Exception {
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(video.getBounds());
        File file = File.createTempFile("jfx_screen_capture", ".jpg");
        ImageIO.write(image, "jpg", file);
        return file;
    }
