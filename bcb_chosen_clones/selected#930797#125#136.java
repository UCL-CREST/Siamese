    public static void captureScreen(Component component, File file) throws IOException, AWTException {
        int width = component.getWidth();
        int height = component.getHeight();
        int x = component.getX();
        int y = component.getY();
        for (component = component.getParent(); component != null; component = component.getParent()) {
            x += component.getX();
            y += component.getY();
        }
        BufferedImage screencapture = new Robot().createScreenCapture(new Rectangle(x, y, width, height));
        ImageIO.write(screencapture, "jpg", file);
    }
