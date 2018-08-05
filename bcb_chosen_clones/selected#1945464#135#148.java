    private ScreenShot captureScreen(String suffix) {
        File screenShotFile = new File(ResultsWriter.getInstance().getResultsFolder() + File.separatorChar + new Date().getTime() + "_" + suffix + ".png");
        try {
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "png", screenShotFile);
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScreenShot screenShot = new ScreenShot(suffix + " Screen-Shot", screenShotFile.getName());
        screenShotList.add(screenShot);
        return screenShot;
    }
