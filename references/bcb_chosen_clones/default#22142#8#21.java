    public static void main(String[] args) throws Exception {
        for (int i = 0; i <= 20; i++) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRect);
            ImageIO.write(image, "jpg", new File("test.jpg"));
            try {
                Thread.sleep(2000);
            } catch (NumberFormatException nfe) {
            }
        }
    }
