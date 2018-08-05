        public synchronized void addFile(File file) {
            try {
                if (file == null || !file.exists() || file.isDirectory()) {
                    return;
                }
                filesAdded++;
                String s = file.getCanonicalPath().substring(baseDirLength + 1).replace(File.separatorChar, '/');
                ZipEntry zipentry = new ZipEntry(s);
                zipentry.setTime(file.lastModified());
                try {
                    zipOutStream.putNextEntry(zipentry);
                } catch (Exception exception1) {
                    System.out.println(exception1);
                }
                FileInputStream fileinputstream = new FileInputStream(file);
                do {
                    int i = fileinputstream.read(buffer, 0, buffer.length);
                    if (i <= 0) {
                        break;
                    }
                    zipOutStream.write(buffer, 0, i);
                } while (true);
                fileinputstream.close();
            } catch (Exception exception) {
                System.out.println("Error in adding " + file + ":" + exception);
            }
        }
