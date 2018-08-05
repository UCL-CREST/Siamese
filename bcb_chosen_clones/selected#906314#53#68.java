    public void copy(File source, File dest) throws IOException {
        System.out.println("copy " + source + " -> " + dest);
        FileInputStream in = new FileInputStream(source);
        try {
            FileOutputStream out = new FileOutputStream(dest);
            try {
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
