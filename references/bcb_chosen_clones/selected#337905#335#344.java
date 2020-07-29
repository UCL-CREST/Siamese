    public void screenShot(File file) {
        try {
            final Rectangle rectangle = mainFrame.getBounds();
            final Robot robot = new Robot();
            final BufferedImage image = robot.createScreenCapture(rectangle);
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
