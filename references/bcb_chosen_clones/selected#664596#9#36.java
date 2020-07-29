    public boolean copy(String file, String path) {
        try {
            File file_in = new File(file);
            String tmp1, tmp2;
            tmp1 = file;
            tmp2 = path;
            while (tmp2.contains("\\")) {
                tmp2 = tmp2.substring(tmp2.indexOf("\\") + 1);
                tmp1 = tmp1.substring(tmp1.indexOf("\\") + 1);
            }
            tmp1 = file.substring(0, file.length() - tmp1.length()) + tmp2 + tmp1.substring(tmp1.indexOf("\\"));
            File file_out = new File(tmp1);
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
