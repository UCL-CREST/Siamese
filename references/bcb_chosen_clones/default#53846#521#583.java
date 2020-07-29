    private void loadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        ExampleFileFilter filter = new ExampleFileFilter(new String[] { "qc" }, "quantum circuits");
        fileChooser.addChoosableFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            ObjectInputStream input = null;
            try {
                input = new ObjectInputStream(new FileInputStream(file));
                try {
                    int[] size = (int[]) input.readObject();
                    circuitPanel.setWires(size[0], size[1]);
                    circuitPanel.gates = (ArrayList) input.readObject();
                    initialQubits = ((QuantumGate) circuitPanel.gates.get(0)).qubits;
                    xRegisterSize = size[0];
                    yRegisterSize = size[1];
                    xRegister = new Register(xRegisterSize);
                    yRegister = new Register(yRegisterSize);
                    int initialState = 0;
                    for (int i = 0; i < xRegisterSize; i++) {
                        int k = xRegisterSize - i - 1;
                        initialState += initialQubits[k] * (1 << i);
                    }
                    double[] tmp = new double[1 << xRegisterSize];
                    tmp[initialState] = 1;
                    xRegister.real = tmp;
                    initialState = 0;
                    for (int i = 0; i < yRegisterSize; i++) {
                        int k = xRegisterSize + yRegisterSize - i - 1;
                        initialState += initialQubits[k] * (1 << i);
                    }
                    tmp = new double[1 << yRegisterSize];
                    tmp[initialState] = 1;
                    yRegister.real = tmp;
                    circuitPanel.repaint();
                    xPanel.setLengthColoring(lengthColoring);
                    yPanel.setLengthColoring(lengthColoring);
                    xPanel.setQubitStates(xRegister.real, xRegister.imaginary);
                    yPanel.setQubitStates(yRegister.real, yRegister.imaginary);
                } catch (ClassNotFoundException cnf) {
                    String title = "File Error";
                    String message = "The file " + file.getName() + " does not contain a circuit!";
                    JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
                } catch (ClassCastException cce) {
                    String title = "File Error";
                    String message = "The file " + file.getName() + " does not contain a circuit or has the wrong format!";
                    JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
                } catch (EOFException eof) {
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if (input != null) input.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
