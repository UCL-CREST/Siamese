    public static void reliability() {
        final String fileName = "Temp.txt";
        reliabilityFrame = new JFrame("System Reliability");
        JLabel sysAlgebraLabel = new JLabel("Enter System Algebra");
        sysAlgebraLabel.setBounds(170, 70, 200, 25);
        sysAlgebra = new JTextField(100);
        sysAlgebra.setBounds(350, 70, 250, 25);
        getFromFile = new JButton("From File");
        getFromFile.setBounds(610, 70, 100, 20);
        getFromFile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    final JFileChooser fc = new JFileChooser();
                    fc.showOpenDialog(reliabilityFrame);
                    try {
                        File file = fc.getSelectedFile();
                        if (file != null) {
                            FileReader reader = new FileReader(file);
                            BufferedReader breader = new BufferedReader(reader);
                            String line = breader.readLine();
                            if (line != null) {
                                line = line.replaceAll("X", "*");
                                line = line.replaceAll(" ", "");
                                sysAlgebra.setText(line);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        JButton genButton = new JButton("Enter");
        genButton.setBounds(350, 130, 200, 25);
        genButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                getRel.setVisible(true);
                generateAlgebra();
            }
        });
        finalReliability = new JLabel("System Reliability = ");
        finalReliability.setBounds(200, 500, 150, 30);
        finalReliability.setVisible(false);
        reliabilityFrame.getContentPane().add(finalReliability);
        finalReliabilityValue = new JTextField(100);
        finalReliabilityValue.setBounds(310, 500, 150, 30);
        finalReliabilityValue.setEditable(false);
        finalReliabilityValue.setVisible(false);
        reliabilityFrame.getContentPane().add(finalReliabilityValue);
        reliabilityFrame.getContentPane().add(getFromFile);
        reliabilityFrame.getContentPane().add(sysAlgebra);
        reliabilityFrame.getContentPane().add(sysAlgebraLabel);
        reliabilityFrame.getContentPane().add(genButton);
        getRel = new JButton("Get Reliability");
        getRel.setBounds(620, 170, 130, 30);
        getRel.setVisible(false);
        reliabilityFrame.getContentPane().add(getRel);
        reliabilityFrame.setSize(800, 600);
        reliabilityFrame.setLayout(null);
        reliabilityFrame.setVisible(true);
    }
