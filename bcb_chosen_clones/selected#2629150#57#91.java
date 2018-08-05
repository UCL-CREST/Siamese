    private void unzip(File filename) throws ZipException, IOException {
        ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(filename)));
        ZipEntry entry = null;
        boolean first_entry = true;
        while ((entry = in.getNextEntry()) != null) {
            if (first_entry) {
                if (!entry.isDirectory()) {
                    File subdir = new File(dir + File.separator + filename.getName().substring(0, filename.getName().length() - SUFFIX_ZIP.length()));
                    if (!subdir.exists()) {
                        subdir.mkdir();
                        dir = subdir;
                    }
                }
                first_entry = false;
            }
            if (entry.isDirectory()) {
                FileUtils.forceMkdir(new File(dir + File.separator + entry.getName()));
            } else {
                File outfile = new File(dir + File.separator + entry.getName());
                File outdir = new File(outfile.getAbsolutePath().substring(0, outfile.getAbsolutePath().length() - outfile.getName().length()));
                if (!outdir.exists()) FileUtils.forceMkdir(outdir);
                FileOutputStream fo = new FileOutputStream(outfile);
                BufferedOutputStream bos = new BufferedOutputStream(fo, BUFFER);
                int read;
                byte data[] = new byte[BUFFER];
                while ((read = in.read(data, 0, BUFFER)) != -1) {
                    read_position++;
                    bos.write(data, 0, read);
                }
                bos.flush();
                bos.close();
            }
        }
        in.close();
    }
