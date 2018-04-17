    public void takeScreenshot(String title) throws Exception {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String time = sdf.format(cal.getTime());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        File f = new File(report.getCurrentTestFolder(), "screenshot_" + time + ".png");
        f.createNewFile();
        ImageIO.write(image, "png", f);
        report.addLink(getTitle("Screenshot"), "file:///" + f.getAbsolutePath());
    }
