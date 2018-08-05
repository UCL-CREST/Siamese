    public static void takeScreenshot(String fn) {
        try {
            System.out.println("about to take screenshot");
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRect);
            System.out.println("done, now writing to disk");
            ImageIO.write(image, "jpg", new File(fn));
            System.out.println("all done");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
