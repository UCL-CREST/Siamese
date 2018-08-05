    private static void output(String filename, ZipOutputStream os) {
        if (filename != null && !filename.equals("")) {
            FileInputStream fis = null;
            try {
                File f = new File(filename);
                if (!f.exists()) return;
                os.putNextEntry(new ZipEntry(f.getName()));
                fis = new FileInputStream(filename);
                int t;
                while ((t = fis.read()) != -1) {
                    os.write(t);
                }
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) fis.close();
                } catch (IOException e) {
                }
            }
        }
    }
