    public Converter(String input, String output) {
        try {
            FileInputStream fis = new FileInputStream(new File(input));
            BufferedReader in = new BufferedReader(new InputStreamReader(fis, "SJIS"));
            FileOutputStream fos = new FileOutputStream(new File(output));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos, "UTF8"));
            int len = 80;
            char buf[] = new char[len];
            int numRead;
            while ((numRead = in.read(buf, 0, len)) != -1) out.write(buf, 0, numRead);
            out.close();
            in.close();
        } catch (IOException e) {
            System.out.println("An I/O Exception Occurred: " + e);
        }
    }
