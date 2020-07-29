    @Override
    public void run() {
        try {
            Robot robot = new Robot();
            BufferedImage bufferedImage = robot.createScreenCapture(screenRect);
            ImageIO.write(bufferedImage, "png", theImageFile);
            Log.infoLog("Created image file : " + theImageFile.getAbsolutePath() + "\n");
            robot = null;
        } catch (Exception exception) {
            Log.throwable(exception.getMessage(), exception);
        }
    }
