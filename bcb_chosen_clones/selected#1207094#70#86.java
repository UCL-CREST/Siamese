    public static boolean copyfile(String file0, String file1) {
        try {
            File f0 = new File(file0);
            File f1 = new File(file1);
            FileInputStream in = new FileInputStream(f0);
            FileOutputStream out = new FileOutputStream(f1);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
            in = null;
            out = null;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
