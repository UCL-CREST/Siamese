    public void snapShot() {
        try {
            BufferedImage screenshot = (new Robot()).createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
            serialNum++;
            String name = fileName + String.valueOf(serialNum) + "." + imageFormat;
            File f = new File(name);
            System.out.print("Save File " + name);
            ImageIO.write(screenshot, imageFormat, f);
            System.out.print("..Finished!\n");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
