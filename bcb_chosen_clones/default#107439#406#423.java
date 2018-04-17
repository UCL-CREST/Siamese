        @Override
        public void actionPerformed(ActionEvent arg0) {
            try {
                InstructorUI.this.contextHelp.setText("Please enter a file name under which the syllabus will be saved");
                JFileChooser fileChooser = new JFileChooser();
                int userChoice = fileChooser.showSaveDialog(InstructorUI.this.window);
                if (userChoice == JFileChooser.APPROVE_OPTION) {
                    FileOutputStream fos = new FileOutputStream(fileChooser.getSelectedFile().getAbsolutePath() + ".is");
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(InstructorUI.this.mapPanel);
                    oos.close();
                    fos.close();
                }
                InstructorUI.this.contextHelp.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
