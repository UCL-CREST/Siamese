    public static void createScreenShot(long sleeptime, String outFileName, JFrame frame) throws Exception {
        outFileName = outFileName + (counter++) + ".png";
        try {
            long time = sleeptime * 1000L;
            System.out.println("Waiting " + (time / 1000L) + " second(s)...");
            Thread.sleep(time);
        } catch (NumberFormatException nfe) {
            System.err.println(sleeptime + " does not seem to be a " + "valid number of seconds.");
            System.exit(1);
        }
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Rectangle screenRect = frame.getBounds();
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRect);
        ImageIO.write(image, "png", new File(outFileName));
        System.out.println("Saved screen shot (" + image.getWidth() + " x " + image.getHeight() + " pixels) to file \"" + outFileName + "\".");
    }
