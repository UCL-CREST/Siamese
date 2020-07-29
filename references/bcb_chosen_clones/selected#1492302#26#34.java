    private static void captureScreen() throws AWTException {
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(0, 0, 500, 500));
        File f = new File("image.jpg");
        try {
            ImageIO.write(image, "jpg", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
