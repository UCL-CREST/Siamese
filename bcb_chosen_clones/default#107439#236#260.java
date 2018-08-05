        @Override
        public void actionPerformed(ActionEvent arg0) {
            InstructorUI.this.contextHelp.setText("Please select an image file for the new map");
            FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("JPEGs", "jpg", "jpeg", "gif", "png");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(fileFilter);
            int userChoice = fileChooser.showOpenDialog(InstructorUI.this.window);
            if (userChoice == JFileChooser.APPROVE_OPTION) {
                try {
                    Image image = ImageIO.read(fileChooser.getSelectedFile());
                    if (image == null) {
                        JOptionPane.showMessageDialog(null, "Please select an image file", "Open Error", JOptionPane.ERROR_MESSAGE);
                        this.actionPerformed(arg0);
                    } else {
                        InstructorUI.this.mapPanel.addMapToPanel(image);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "There was an error opening the image file", "Open Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                InstructorUI.this.mapPanel.revalidate();
                InstructorUI.this.mapPanel.repaint();
            }
            InstructorUI.this.contextHelp.setText("");
        }
