    public void screenCapture() throws Exception {
        BufferedImage screencapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        File file = new File(report.getCurrentTestFolder(), "screencapture.jpg");
        ImageIO.write(screencapture, "jpg", file);
        report.addLink("screen capture", "screencapture.jpg");
    }
