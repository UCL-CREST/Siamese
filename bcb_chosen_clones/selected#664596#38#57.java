    public boolean copy(String file, String target, int tag) {
        try {
            File file_in = new File(file);
            File file_out = new File(target);
            File parent = file_out.getParentFile();
            parent.mkdirs();
            FileInputStream in1 = new FileInputStream(file_in);
            FileOutputStream out1 = new FileOutputStream(file_out);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = in1.read(bytes)) != -1) out1.write(bytes, 0, c);
            in1.close();
            out1.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error!");
            return false;
        }
    }
