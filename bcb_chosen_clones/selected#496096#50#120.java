    public DeployToServerDialog(JFrame parentFrame, WithProperties withProperties) {
        super(parentFrame);
        this.setTitle("Deploy to server");
        this.setSize(320, 150);
        this.setLocationRelativeTo(getParent());
        this.getContentPane().setLayout(new GridLayout(5, 2));
        this.add(new JLabel("Server: "));
        server.setPreferredSize(new Dimension(240, 25));
        server.setText("http://localhost:8080/wfms");
        this.add(server);
        this.add(new JLabel("User:"));
        username.setPreferredSize(new Dimension(240, 25));
        this.add(username);
        this.add(new JLabel("Password:"));
        password.setPreferredSize(new Dimension(240, 25));
        this.add(password);
        this.add(new JLabel("Datamodel"));
        browseDataModel.addActionListener(openAction);
        this.add(browseDataModel);
        this.add(button);
        this.setVisible(true);
        final DeployToServerDialog dialog = this;
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                URL url = null;
                URLConnection urlConn = null;
                DataOutputStream printout;
                try {
                    url = new URL(dialog.server.getText() + "/UploadServlet");
                    urlConn = url.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    ByteArrayOutputStream xmlOutput = new ByteArrayOutputStream();
                    Serializer serializer = new Persister();
                    serializer.write(new XmlDocument(PNEditor.getInstance().getDocument()), xmlOutput);
                    printout = new DataOutputStream(urlConn.getOutputStream());
                    printout.writeBytes("username=" + dialog.username.getText() + "&password=" + dialog.password.getText() + "&xml=" + URLEncoder.encode(xmlOutput.toString("UTF-8"), "UTF-8") + "&xmldatamodel=" + URLEncoder.encode(dialog.openAction.getOutput(), "UTF-8"));
                    printout.flush();
                    printout.close();
                    BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    String str = input.readLine();
                    System.out.println(str);
                    if (str.equalsIgnoreCase("Authentication failed!")) {
                        JOptionPane.showMessageDialog(rootPane, str);
                        return;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(rootPane, ex.getMessage());
                }
                if (!java.awt.Desktop.isDesktopSupported()) {
                    JOptionPane.showMessageDialog(rootPane, "Desktop is not supported (fatal)");
                    return;
                }
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    JOptionPane.showMessageDialog(rootPane, "Desktop doesn't support the browse action (fatal)");
                    return;
                }
                try {
                    java.net.URI uri = new java.net.URI(dialog.server.getText());
                    desktop.browse(uri);
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(rootPane, exp.getMessage());
                }
                dialog.setVisible(false);
            }
        });
    }
