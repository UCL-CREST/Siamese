    @Override
    public void actionPerformed(ActionEvent pE) {
        Robot robot;
        try {
            robot = new Robot();
            robot.setAutoDelay(1000);
            BufferedImage prscrn = robot.createScreenCapture(new Rectangle(1280, 1024));
            ImageIO.write(prscrn, IMG_TYPE, new File("C:\\prscrn_" + new Date().toString().replace(":", "-") + "." + IMG_TYPE));
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
