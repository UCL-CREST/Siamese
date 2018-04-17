    public void actionPerformed(ActionEvent at) {
        if (at.getSource() == bt1) {
            try {
                robot = new Robot();
            } catch (Exception ex) {
                System.out.println(ex);
            }
            image = robot.createScreenCapture(rect);
            g = jp1.getGraphics();
            g.drawImage(image, 0, 0, jp1.getWidth(), jp1.getHeight(), jp1);
        }
        if (at.getSource() == bt2) {
            File file = new File("Screen001.png");
            try {
                ImageIO.write(image, "png", file);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
