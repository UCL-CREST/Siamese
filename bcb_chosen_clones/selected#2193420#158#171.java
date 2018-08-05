    private void saveScreenAsImage() {
        BufferedImage screencapture;
        try {
            screencapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            File file = new File("screencapture.gif");
            ImageIO.write(screencapture, "gif", file);
        } catch (HeadlessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
