    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {
        String username = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());
        String password = null;
        Preferences p = NbPreferences.root().node("/org/chartsy/register");
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(new String(txtPassword.getPassword()).getBytes());
            BigInteger hash = new BigInteger(1, md5.digest());
            password = hash.toString(16);
        } catch (NoSuchAlgorithmException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        try {
            lblMessage.setText("Checking registration ...");
            btnRegister.setEnabled(false);
            btnRemind.setEnabled(false);
            HttpClient client = ProxyManager.getDefault().getHttpClient();
            GetMethod method = new GetMethod(NbBundle.getMessage(RegisterForm.class, "CheckReg_URL"));
            method.setQueryString(new NameValuePair[] { new NameValuePair("username", username), new NameValuePair("password", password) });
            client.executeMethod(method);
            BufferedReader br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
            boolean chartsyRegistred = false;
            String name = null;
            if (br != null) {
                String firstLine = br.readLine();
                if (firstLine.equals("OK")) {
                    chartsyRegistred = true;
                    name = br.readLine();
                    p.putBoolean("registred", true);
                    p.put("name", name);
                    p.put("date", String.valueOf(new Date().getTime()));
                    p.put("username", username);
                    p.put("password", pass);
                }
            }
            int userId = checkStockScanPRORegistration();
            boolean stockScanPRORegistred = userId != 0;
            boolean mrSwingRegistred = stockScanPRORegistred ? true : checkMrSwingRegistration();
            p.putBoolean("mrswingregistred", mrSwingRegistred);
            Preferences prefs = NbPreferences.root().node("/org/chartsy/stockscanpro");
            prefs.putBoolean("stockscanproregistred", stockScanPRORegistred);
            prefs.putInt("stockscanprouser", userId);
            FeaturesPanel.getDefault().hideBanners();
            if (chartsyRegistred) {
                if (name != null) {
                    lblMessage.setText(name + ", thank you for the registration.");
                } else {
                    lblMessage.setText("Thank you for the registration.");
                }
                btnRegister.setVisible(false);
                btnRemind.setText("Close");
                btnRemind.setEnabled(true);
            } else {
                lblMessage.setText("Error, could not register. Check your username and password.");
                btnRegister.setEnabled(true);
                btnRemind.setEnabled(true);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
