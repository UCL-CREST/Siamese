    private static void captureScreenshot() {
        try {
            Robot robot = new Robot();
            Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = robot.createScreenCapture(screenSize);
            String fileName = String.valueOf(System.nanoTime()) + ".png";
            File screenshot = new File(System.getProperty("user.dir") + File.separator + "screenshots", fileName);
            ImageIO.write(capture, "png", screenshot);
            System.out.println("Screenshot image captured: " + fileName);
        } catch (Exception exception) {
            System.out.println("Could not create screenshot image: " + exception);
        }
    }
