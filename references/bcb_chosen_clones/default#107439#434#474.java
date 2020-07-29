        @Override
        public void actionPerformed(ActionEvent arg0) {
            if (InstructorUI.this.mapPanel.getMap() != null) {
                int choice = JOptionPane.showConfirmDialog(null, "Do you want to save?", "Save Project Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    SaveListener thisIsABadIdea = new SaveListener();
                    thisIsABadIdea.actionPerformed(null);
                }
            }
            try {
                InstructorUI.this.contextHelp.setText("Please select syllabus file to open");
                FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Syllabus Files", "is");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter(fileFilter);
                int userChoice = fileChooser.showOpenDialog(InstructorUI.this.window);
                if (userChoice == JFileChooser.APPROVE_OPTION) {
                    File tempFile = fileChooser.getSelectedFile();
                    FileInputStream fis = new FileInputStream(tempFile);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    InstructorUI.this.mapPanel = (MapPanel) ois.readObject();
                    if (InstructorUI.this.mapPanel.getHelpHandler() == null) {
                        InstructorUI.this.mapPanel.createHelpHandler();
                    }
                    this.scrollPanel.getViewport().add(InstructorUI.this.mapPanel);
                    InstructorUI.this.outerMapPanel.remove(InstructorUI.this.slider);
                    InstructorUI.this.slider = InstructorUI.this.mapPanel.getSlider();
                    InstructorUI.this.outerMapPanel.add(InstructorUI.this.slider);
                    InstructorUI.this.outerMapPanel.repaint();
                    InstructorUI.this.syllabusName = fileChooser.getSelectedFile().getAbsolutePath();
                    ois.close();
                    fis.close();
                }
                InstructorUI.this.contextHelp.setText("");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not open the specified file", "Open Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Could not open the specified file", "Open Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
