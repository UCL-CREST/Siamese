    private void perform(String[] files, String path, String offset) {
        byte[] buf = new byte[4096];
        for (int i = 0; i < files.length; i++) {
            try {
                File f = new File(path + offset + files[i]);
                BufferedInputStream in = null;
                if (f.exists() && !f.isDirectory()) {
                    in = new BufferedInputStream(new FileInputStream(path + offset + files[i]));
                } else if (f.exists()) {
                    if (!files[i].endsWith("/")) {
                        files[i] = files[i] + "/";
                    }
                    perform(f.list(), path, offset + files[i]);
                }
                ZipEntry tmp = new ZipEntry(offset + files[i]);
                z.putNextEntry(tmp);
                int len = 0;
                while ((in != null) && (len != StreamTokenizer.TT_EOF)) {
                    len = in.read(buf);
                    if (len == StreamTokenizer.TT_EOF) {
                        break;
                    }
                    z.write(buf, 0, len);
                }
                z.closeEntry();
            } catch (Exception ex) {
                Log.debug("Skipping a file (no permission?)");
            }
        }
    }
