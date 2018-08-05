    public void capture() {
        filePath = filePath + "." + fileType;
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRect);
            ImageIO.write(image, fileType, new File(filePath));
        } catch (Exception ex) {
            System.out.println("ERROR : " + ex);
        }
    }
