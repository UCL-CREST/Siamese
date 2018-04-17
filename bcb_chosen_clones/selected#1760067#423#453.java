    public static void copy(File source, File dest) throws BuildException {
        dest = new File(dest, source.getName());
        if (source.isFile()) {
            byte[] buffer = new byte[4096];
            FileInputStream fin = null;
            FileOutputStream fout = null;
            try {
                fin = new FileInputStream(source);
                fout = new FileOutputStream(dest);
                int count = 0;
                while ((count = fin.read(buffer)) > 0) fout.write(buffer, 0, count);
                fin.close();
                fout.close();
            } catch (IOException ex) {
                throw new BuildException(ex);
            } finally {
                try {
                    if (fin != null) fin.close();
                } catch (IOException ex) {
                }
                try {
                    if (fout != null) fout.close();
                } catch (IOException ex) {
                }
            }
        } else {
            dest.mkdirs();
            File[] children = source.listFiles();
            for (File child : children) copy(child, dest);
        }
    }
