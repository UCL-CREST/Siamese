    public static boolean copyInternal(File src, File dest, byte[] buf) {
        boolean result = true;
        String files[] = null;
        if (src.isDirectory()) {
            files = src.list();
            result = dest.mkdir();
        } else {
            files = new String[1];
            files[0] = "";
        }
        if (files == null) {
            files = new String[0];
        }
        for (int i = 0; (i < files.length) && result; i++) {
            File fileSrc = new File(src, files[i]);
            File fileDest = new File(dest, files[i]);
            if (fileSrc.isDirectory()) {
                result = copyInternal(fileSrc, fileDest, buf);
            } else {
                FileInputStream is = null;
                FileOutputStream os = null;
                try {
                    is = new FileInputStream(fileSrc);
                    os = new FileOutputStream(fileDest);
                    int len = 0;
                    while (true) {
                        len = is.read(buf);
                        if (len == -1) break;
                        os.write(buf, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    result = false;
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                        }
                    }
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
        return result;
    }
