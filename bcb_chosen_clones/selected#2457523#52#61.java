    public void captureSystemScreenshot() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        final BufferedImage bufferedImage;
        final Rectangle captureSize;
        final Robot robot;
        robot = RobotRetriever.getRobot();
        captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        bufferedImage = robot.createScreenCapture(captureSize);
        createNecessaryDirectories();
        ImageIO.write(bufferedImage, "png", this.file);
    }
