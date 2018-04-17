        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ImageIO.write(new Robot().createScreenCapture(Main.instance.getBounds()), "png", new File("ss_" + new Date().getTime() + ".png"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
