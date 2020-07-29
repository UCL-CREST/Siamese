        public void actionPerformed(ActionEvent e) {
            try {
                Rectangle screenRect = m_browserFrame.getScreenLocation();
                Robot robot = new Robot();
                BufferedImage image = robot.createScreenCapture(screenRect);
                ImageIO.write(image, "png", new File("screen.png"));
                K13DPrinter printer = new K13DPrinter("3D", Configuration.inst().getLogoImageFileName(), image);
                printer.start();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
