    protected void takeScreenShot() {
        try {
            Robot robot = new Robot();
            Rectangle theRectOnScreen = new Rectangle(cv.getLocationOnScreen().x, cv.getLocationOnScreen().y, cv.getWidth(), cv.getHeight());
            BufferedImage bi = robot.createScreenCapture(theRectOnScreen);
            File f = new File(".\\screenshot.jpg");
            if (f.exists()) {
                f.delete();
            }
            try {
                if (f.createNewFile()) {
                    FileOutputStream fos = new FileOutputStream(f);
                    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
                    encoder.encode(bi);
                    fos.flush();
                    fos.close();
                }
            } catch (IOException ioex) {
                status.setText("");
                status.append("Error writing JPEG");
            }
        } catch (AWTException aex) {
            status.setText("");
            status.append("Error getting Screen content");
        }
    }
