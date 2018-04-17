    public static void copyFile(String file1, String file2) {
        File filedata1 = new java.io.File(file1);
        if (filedata1.exists()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file2));
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(file1));
                try {
                    int read;
                    while ((read = in.read()) != -1) {
                        out.write(read);
                    }
                    out.flush();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                } finally {
                    out.close();
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
