    private static void userAuth(String challenge) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("BrokenMD4");
            String passwd = null;
            if (System.getProperty("jarsync.password.gui") != null) {
                JPasswordField pass = new JPasswordField();
                JPanel panel = new JPanel(new GridLayout(2, 1));
                panel.add(new JLabel("Password:"));
                panel.add(pass);
                JOptionPane.showMessageDialog(null, panel, remoteUser + '@' + remoteHost + "'s Password", JOptionPane.QUESTION_MESSAGE);
                passwd = new String(pass.getPassword());
            } else {
                System.out.print(remoteUser + '@' + remoteHost + "'s password: ");
                passwd = Util.readLine(System.in);
                System.out.println();
            }
            md.update(new byte[4]);
            md.update(passwd.getBytes("US-ASCII"));
            md.update(challenge.getBytes("US-ASCII"));
            byte[] response = md.digest();
            Util.writeASCII(out, remoteUser + " " + Util.base64(response) + '\n');
            out.flush();
        } catch (NoSuchAlgorithmException nsae) {
            throw new IOException("could not create message digest.");
        }
    }
