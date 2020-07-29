    public static void main(String[] args) throws AWTException, IOException {
        Robot robot = new Robot();
        BufferedImage capture = robot.createScreenCapture(new Rectangle(new Point(0, 0), Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(capture, "png", new File("capture.png"));
    }
