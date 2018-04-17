    public static void main(String[] args) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        if (args[0].equals("-e") && args[2].equals("-l") && args[4].equals("-p")) {
            System.out.println("Start encoding in " + args[1]);
            out = new DataOutputStream(new FileOutputStream(new File(args[1])));
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encryptedLogin = cipher.doFinal(args[3].getBytes());
            byte[] encryptedPassword = cipher.doFinal(args[5].getBytes());
            out.write(encryptedLogin.length);
            out.write(encryptedPassword.length);
            out.write(encryptedLogin);
            out.write(encryptedPassword);
            out.flush();
            out.close();
            System.out.println("Done!");
        } else if (args[0].equals("-d") && args[1] != null) {
            in = new DataInputStream(new FileInputStream(new File(args[1])));
            int logLength = in.readByte();
            int passLength = in.readByte();
            byte[] encryptedLogin = new byte[logLength];
            for (int i = 0; i < logLength; ++i) {
                encryptedLogin[i] = in.readByte();
            }
            byte[] encryptedPass = new byte[passLength];
            for (int i = 0; i < passLength; ++i) {
                encryptedPass[i] = in.readByte();
            }
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] originalLogin = cipher.doFinal(encryptedLogin);
            byte[] originalPass = cipher.doFinal(encryptedPass);
            System.out.println("Login: " + new String(originalLogin));
            System.out.println("Password: " + new String(originalPass));
        }
    }
