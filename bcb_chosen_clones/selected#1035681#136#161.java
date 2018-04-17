    private void copyFile(File sourcefile, File targetfile) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(sourcefile));
            out = new BufferedOutputStream(new FileOutputStream(targetfile));
            byte[] buffer = new byte[4096];
            int bytesread = 0;
            while ((bytesread = in.read(buffer)) >= 0) {
                out.write(buffer, 0, bytesread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
