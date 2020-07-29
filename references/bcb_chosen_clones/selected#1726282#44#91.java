    public Main(String[] args) {
        boolean encrypt = false;
        if (args[0].compareTo("-e") == 0) {
            encrypt = true;
        } else if (args[0].compareTo("-d") == 0) {
            encrypt = false;
        } else {
            System.out.println("first argument is invalid");
            System.exit(-2);
        }
        char[] password = new char[args[2].length()];
        for (int i = 0; i < args[2].length(); i++) {
            password[i] = (char) args[2].getBytes()[i];
        }
        try {
            InitializeCipher(encrypt, password);
        } catch (Exception e) {
            System.out.println("error initializing cipher");
            System.exit(-3);
        }
        try {
            InputStream is = new FileInputStream(args[1]);
            OutputStream os;
            int read, max = 10;
            byte[] buffer = new byte[max];
            if (encrypt) {
                os = new FileOutputStream(args[1] + ".enc");
                os = new CipherOutputStream(os, cipher);
            } else {
                os = new FileOutputStream(args[1] + ".dec");
                is = new CipherInputStream(is, cipher);
            }
            read = is.read(buffer);
            while (read != -1) {
                os.write(buffer, 0, read);
                read = is.read(buffer);
            }
            while (read == max) ;
            os.close();
            is.close();
            System.out.println(new String(buffer));
        } catch (Exception e) {
            System.out.println("error encrypting/decrypting message:");
            e.printStackTrace();
            System.exit(-4);
        }
        System.out.println("done");
    }
