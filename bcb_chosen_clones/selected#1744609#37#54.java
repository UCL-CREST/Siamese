    private void jbInit() throws Exception {
        getContentPane().setLayout(borderLayout1);
        this.setTitle("�ϥλ���");
        jTextPane1.setEditable(false);
        this.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jScrollPane1.getViewport().add(jTextPane1);
        this.setSize(400, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        URL url = ReadmeFrame.class.getResource("readme.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder strBuilder = new StringBuilder();
        while (reader.ready()) {
            strBuilder.append(reader.readLine());
            strBuilder.append('\n');
        }
        reader.close();
        jTextPane1.setText(strBuilder.toString());
    }
