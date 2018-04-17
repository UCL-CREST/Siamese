        private void addRecursivly(ZipOutputStream out, File path, String removePath) throws Exception {
            String[] children = path.list();
            byte[] buf = new byte[1024];
            for (int i = 0; i < children.length; i++) {
                File f = new File(path, children[i]);
                if (f.isDirectory()) {
                    addRecursivly(out, f, removePath);
                    continue;
                }
                String filename = f.getAbsolutePath().replace(removePath, "");
                if (filename.indexOf("/") == 0) filename = filename.substring(1);
                if (filename.indexOf(File.separatorChar) == 0) filename = filename.substring(1);
                out.putNextEntry(new java.util.zip.ZipEntry(filename.replace("\\", "/")));
                int len;
                FileInputStream zin = new FileInputStream(f);
                while ((len = zin.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                zin.close();
            }
        }
