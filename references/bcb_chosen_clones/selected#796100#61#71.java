    public void saveImage(String path) {
        try {
            Rectangle rectangle = new Rectangle(this.getBounds());
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(rectangle);
            File file = new File(path);
            ImageIO.write(image, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
