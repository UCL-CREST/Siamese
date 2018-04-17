            @Override
            public void actionPerformed(ActionEvent e) {
                String rowsS = JOptionPane.showInputDialog(getMainWindow(), "Number of matrix rows:", "5000");
                String colsS = JOptionPane.showInputDialog(getMainWindow(), "Number of matrix columns:", "5000");
                int rows, cols;
                try {
                    rows = Integer.parseInt(rowsS);
                    cols = Integer.parseInt(colsS);
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(getMainWindow(), "Invalid matrix size: " + rowsS + " x " + colsS);
                    return;
                }
                Matrix matrix = calculator.genRandom(rows, cols);
                if (fileChooser.showSaveDialog(getMainWindow()) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    resetOutput();
                    try {
                        calculator.save(file, matrix);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(getMainWindow(), "Unable to save generated matrix into file: " + file.getName());
                    }
                    model.add(file.getName(), matrix);
                }
            }
