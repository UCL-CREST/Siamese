    public void screenShot(File file, Insets inset) throws Exception {
        if (inset == null) {
            inset = new Insets(0, 0, 0, 0);
        }
        Robot robot = new Robot();
        robot.setAutoWaitForIdle(true);
        getFrame().refresh();
        robot.waitForIdle();
        Point xy = getFrame().getForm().getLocation();
        Dimension wh = getFrame().getForm().getSize();
        SwingUtilities.convertPointToScreen(xy, getFrame().getForm().getCtrl());
        if (getFrame().getForm().getWindow().getCtrl() instanceof Window) {
            xy = getFrame().getForm().getWindow().getLocation();
            wh = getFrame().getForm().getWindow().getSize();
        }
        Rectangle r = new Rectangle(xy.x - inset.left, xy.y - inset.top, wh.width + inset.right + inset.left, wh.height + inset.bottom + inset.right);
        if (r.width == 0) {
            r.width = 10;
        }
        if (r.height == 0) {
            r.height = 10;
        }
        BufferedImage image = robot.createScreenCapture(r);
        file.getParentFile().mkdirs();
        ImageIO.write(image, "png", file);
    }
