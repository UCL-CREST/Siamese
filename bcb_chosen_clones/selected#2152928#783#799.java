    public void copyImage(String from, String to) {
        File inputFile = new File(from);
        File outputFile = new File(to);
        try {
            if (inputFile.canRead()) {
                FileInputStream in = new FileInputStream(inputFile);
                FileOutputStream out = new FileOutputStream(outputFile);
                byte[] buf = new byte[65536];
                int c;
                while ((c = in.read(buf)) > 0) out.write(buf, 0, c);
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
