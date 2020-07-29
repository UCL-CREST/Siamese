    private void sendScreenshot() {
        try {
            robot.delay(300);
            output.flush();
            image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            File file = new File(".VNCserver.gif");
            file.createNewFile();
            FileOutputStream fileout = new FileOutputStream(file);
            ImageIO.write(image, "gif", fileout);
            fileout.flush();
            fileout.close();
            byte[] imageBytes = new byte[(int) file.length()];
            FileInputStream filein = new FileInputStream(file);
            filein.read(imageBytes);
            output.writeObject(imageBytes);
            output.flush();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
