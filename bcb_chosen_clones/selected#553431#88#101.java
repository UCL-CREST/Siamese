        public void compress(File f, String name) throws IOException {
            if (!f.isFile()) return;
            ZipEntry entry = new ZipEntry(name);
            entry.setTime(f.lastModified());
            FileInputStream in = new FileInputStream(f);
            byte[] buf = new byte[1024];
            int readed = 0;
            dest.putNextEntry(entry);
            while ((readed = in.read(buf)) > 0) {
                dest.write(buf, 0, readed);
            }
            in.close();
            dest.closeEntry();
        }
