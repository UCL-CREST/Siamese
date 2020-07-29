    private void saveFullScreenAsImage() {
        BufferedImage fullScreenImage = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        try {
            String value = action.getPara("value");
            checkSaveAsPath(value);
            ImageIO.write(fullScreenImage, "JPEG", new File(value));
        } catch (Exception e) {
            Util.error(e);
        }
    }
