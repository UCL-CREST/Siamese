    public void screenShot() {
        try {
            File f = new File("screenshot");
            f.mkdir();
            Rectangle rectangle = frame.getBounds();
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(rectangle);
            File file = getPossiblePNG();
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
