    public static void decoupe(String input_file_path) {
        final int BUFFER_SIZE = 2000000;
        try {
            FileInputStream fr = new FileInputStream(input_file_path);
            byte[] cbuf = new byte[BUFFER_SIZE];
            int n_read = 0;
            int i = 0;
            boolean bContinue = true;
            while (bContinue) {
                n_read = fr.read(cbuf, 0, BUFFER_SIZE);
                if (n_read == -1) break;
                FileOutputStream fo = new FileOutputStream("f_" + ++i);
                fo.write(cbuf, 0, n_read);
                fo.close();
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
