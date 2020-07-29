    public static BufferedImage createImage(Rectangle region, String fileName) throws AWTException, IOException {
        BufferedImage image = new Robot().createScreenCapture(region);
        ScreenImage.writeImage(image, fileName);
        return image;
    }
