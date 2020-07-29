    public boolean export(String project, File zipFile) {
        final byte[] buf = new byte[1024];
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            final List<String> filenames = new ArrayList<String>();
            filenames.add(this.getOperatorIndicatorFilename(project));
            filenames.add(this.getJHotDrawFilename(project));
            for (String filename : filenames) {
                FileInputStream in = null;
                try {
                    in = new FileInputStream(filename);
                    out.putNextEntry(new ZipEntry(new File(filename).getName()));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } finally {
                    Utils.closeEntry(out);
                    Utils.close(in);
                }
            }
        } catch (IOException ex) {
            log.error(null, ex);
            return false;
        } finally {
            Utils.close(out);
        }
        return true;
    }
