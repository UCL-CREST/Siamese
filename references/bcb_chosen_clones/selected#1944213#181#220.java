    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == submitButton) {
            SubmissionProfile profile = (SubmissionProfile) destinationCombo.getSelectedItem();
            String uri = profile.endpoint;
            String authPoint = profile.authenticationPoint;
            String user = userIDField.getText();
            String passwd = new String(passwordField.getPassword());
            try {
                URL url = new URL(authPoint + "?username=" + user + "&password=" + passwd);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = "";
                String text = "";
                while ((line = reader.readLine()) != null) {
                    text = text + line;
                }
                reader.close();
                submit(uri, user);
                JOptionPane.showMessageDialog(null, "Submission accepted", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.setVisible(false);
                this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                if (ex instanceof java.io.IOException) {
                    String msg = ex.getMessage();
                    if (msg.indexOf("HTTP response code: 401") != -1) JOptionPane.showMessageDialog(null, "Invalid Username/Password", "Invalid Username/Password", JOptionPane.ERROR_MESSAGE); else if (msg.indexOf("HTTP response code: 404") != -1) {
                        try {
                            submit(uri, user);
                            JOptionPane.showMessageDialog(null, "Submission accepted", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                }
            }
        } else if (src == cancelButton) {
            this.setVisible(false);
            this.dispose();
        }
    }
