    protected synchronized void attachScreenShot() {
        String[] formatNames = ImageIO.getReaderFormatNames();
        try {
            trayIcon.minimizeAll();
            GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
            int scrnNum = 1;
            for (GraphicsDevice screen : screens) {
                Robot robot = new Robot(screen);
                BufferedImage shot = robot.createScreenCapture(new Rectangle(0, 0, screen.getDisplayMode().getWidth(), screen.getDisplayMode().getHeight()));
                File shotFile = File.createTempFile("shot_" + scrnNum + "_", ".jpg");
                shotFile.deleteOnExit();
                ImageIO.write(shot, "jpg", shotFile);
                addFile(new AttachFile(shotFile, null, true));
                scrnNum++;
            }
            trayIcon.showAll();
            revalidate();
            repaint();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }
