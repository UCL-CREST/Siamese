        public void run() {
            final String basename = FilenameUtils.removeExtension(file.getName());
            final File compressed = new File(logDirectory, basename + ".gz");
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new FileInputStream(file);
                out = new GZIPOutputStream(new FileOutputStream(compressed));
                IOUtils.copy(in, out);
                in.close();
                out.close();
            } catch (IOException e) {
                reportError("Error compressing olg log file after file rotation", e, ErrorManager.GENERIC_FAILURE);
            } finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }
            Collections.replaceAll(files, file, compressed);
        }
