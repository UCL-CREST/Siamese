    private void grabScreenShot(String testName) throws AWTException, IOException {
        String dir = "c:/screenshots";
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(screenSize));
        File file = new File(dir + "/" + testName + (screenshotCounter++) + ".jpeg");
        new File(dir).mkdirs();
        file.createNewFile();
        ImageIO.write(image, "jpeg", file);
        System.out.println("Screenshot saved: " + file.getName() + ". Directory: " + file.getParentFile().getAbsolutePath() + ".");
    }
