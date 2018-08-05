    public void transformFile(File f, File targetDir) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(f);
        try {
            int c = 0;
            while (c != -1) {
                c = fis.read();
                if (c != -1) {
                    bos.write((byte) c);
                }
            }
            bos.flush();
        } finally {
            fis.close();
        }
        File target = new File(targetDir, f.getName());
        FileOutputStream fos = new FileOutputStream(target);
        try {
            byte[] transd = transformer.transformClass(bos.toByteArray());
            fos.write(transd);
            fos.flush();
        } finally {
            fos.close();
        }
    }
