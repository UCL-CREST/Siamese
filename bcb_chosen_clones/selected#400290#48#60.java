    public static void SaveScreenToJPG(String fileFullName) {
        try {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            BufferedImage screenshot = (new Robot()).createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
            String name = fileFullName;
            File f = new File(name);
            System.out.print("Save File " + name);
            ImageIO.write(screenshot, "jpg", f);
            System.out.print("..Finished!\n");
        } catch (Exception ex) {
            throw new RuntimeException("Can not save screen to file.");
        }
    }
