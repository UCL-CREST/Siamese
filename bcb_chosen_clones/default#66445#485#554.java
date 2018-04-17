    public static boolean changeCredentials() {
        boolean passed = false;
        boolean credentials = false;
        HashMap info = null;
        Debug.log("Main.changeCredentials", "show dialog for userinfo");
        info = getUserInfo();
        if ((Boolean) info.get("submit")) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(info.get("password").toString().getBytes());
                String passHash = new BigInteger(1, md5.digest()).toString(16);
                Debug.log("Main.changeCredentials", "validate credentials with the database");
                passed = xmlRpcC.checkUser(info.get("username").toString(), passHash);
                Debug.log("Main.changeCredentials", "write the credentials to file");
                xmlC.writeUserdata(userdataFile, info.get("username").toString(), passHash);
                credentials = passed;
                testVar = true;
            } catch (Exception ex) {
                System.out.println(ex.toString());
                if (ex.getMessage().toLowerCase().contains("unable")) {
                    JOptionPane.showMessageDialog(null, "Database problem occured, please try again later", "Error", JOptionPane.ERROR_MESSAGE);
                    passed = true;
                    testVar = false;
                } else {
                    passed = Boolean.parseBoolean(ex.getMessage());
                    JOptionPane.showMessageDialog(null, "Invallid userdata, try again", "Invallid userdata", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            if (new File(userdataFile).exists()) {
                testVar = true;
                credentials = true;
            } else {
                testVar = false;
                JOptionPane.showMessageDialog(null, "No userdata was entered\nNo tests will be executed until you enter them ", "Warning", JOptionPane.ERROR_MESSAGE);
            }
            passed = true;
        }
        while (!passed) {
            Debug.log("Main.changeCredentials", "show dialog for userinfo");
            info = getUserInfo();
            if ((Boolean) info.get("submit")) {
                try {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    md5.update(info.get("password").toString().getBytes());
                    String passHash = new BigInteger(1, md5.digest()).toString(16);
                    Debug.log("Main.changeCredentials", "validate credentials with the database");
                    passed = xmlRpcC.checkUser(info.get("username").toString(), passHash);
                    Debug.log("Main.changeCredentials", "write credentials to local xml file");
                    xmlC.writeUserdata(userdataFile, info.get("username").toString(), passHash);
                    credentials = passed;
                    testVar = true;
                } catch (Exception ex) {
                    Debug.log("Main.changeCredentials", "credential validation failed");
                    passed = Boolean.parseBoolean(ex.getMessage());
                    JOptionPane.showMessageDialog(null, "Invallid userdata, try again", "Invallid userdata", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (new File(userdataFile).exists()) {
                    testVar = true;
                    credentials = true;
                } else {
                    testVar = false;
                    JOptionPane.showMessageDialog(null, "No userdata was entered\nNo tests will be executed untill u enter them ", "Warning", JOptionPane.ERROR_MESSAGE);
                }
                passed = true;
            }
        }
        return credentials;
    }
