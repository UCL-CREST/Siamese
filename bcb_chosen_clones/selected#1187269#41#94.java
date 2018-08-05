    private void initialize() {
        StringBuffer license = new StringBuffer();
        URL url;
        InputStreamReader in;
        BufferedReader reader;
        String str;
        JTextArea textArea;
        JButton button;
        GridBagConstraints c;
        setTitle("mibible License");
        setSize(600, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        url = getClass().getClassLoader().getResource("LICENSE.txt");
        if (url == null) {
            license.append("Couldn't locate license file (LICENSE.txt).");
        } else {
            try {
                in = new InputStreamReader(url.openStream());
                reader = new BufferedReader(in);
                while ((str = reader.readLine()) != null) {
                    if (!str.equals("")) {
                        license.append(str);
                    }
                    license.append("\n");
                }
                reader.close();
            } catch (IOException e) {
                license.append("Error reading license file ");
                license.append("(LICENSE.txt):\n\n");
                license.append(e.getMessage());
            }
        }
        textArea = new JTextArea(license.toString());
        textArea.setEditable(false);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0d;
        c.weighty = 1.0d;
        c.insets = new Insets(4, 5, 4, 5);
        getContentPane().add(new JScrollPane(textArea), c);
        button = new JButton("Close");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        c = new GridBagConstraints();
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(button, c);
    }
