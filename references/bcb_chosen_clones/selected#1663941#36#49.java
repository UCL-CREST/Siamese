    public String captureAndEncodeSystemScreenshot() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        final ByteArrayOutputStream outStream;
        final BufferedImage bufferedImage;
        final Rectangle captureSize;
        final byte[] encodedData;
        final Robot robot;
        robot = RobotRetriever.getRobot();
        captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        bufferedImage = robot.createScreenCapture(captureSize);
        outStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outStream);
        encodedData = Base64.encodeBase64(outStream.toByteArray());
        return new String(encodedData);
    }
