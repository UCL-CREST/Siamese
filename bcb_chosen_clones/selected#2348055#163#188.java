    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java Screenshot " + "WAITSECONDS OUTFILE.png");
            System.exit(1);
        }
        String outFileName = args[1];
        if (!outFileName.toLowerCase().endsWith(".png")) {
            System.err.println("Error: output file name must " + "end with \".png\".");
            System.exit(1);
        }
        try {
            long time = Long.parseLong(args[0]) * 1000L;
            System.out.println("Waiting " + (time / 1000L) + " second(s)...");
            Thread.sleep(time);
        } catch (NumberFormatException nfe) {
            System.err.println(args[0] + " does not seem to be a " + "valid number of seconds.");
            System.exit(1);
        }
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRect);
        ImageIO.write(image, "png", new File(outFileName));
        System.out.println("Saved screen shot (" + image.getWidth() + " x " + image.getHeight() + " pixels) to file \"" + outFileName + "\".");
    }
