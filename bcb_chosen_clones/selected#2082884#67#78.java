    public void captureScreen() {
        try {
            Rectangle screenRectangle = new Rectangle(p, screenSize);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRectangle);
            ImageIO.write(image, tipo, new File(path + "." + tipo));
        } catch (IOException ex) {
            Logger.getLogger(Captura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AWTException ex) {
            Logger.getLogger(Captura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
