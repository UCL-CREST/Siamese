    private void download(File archive, File timestamp, URL url, IProgressMonitor monitor) throws IOException {
        monitor.subTask("download " + url.toString());
        InputStream in = null;
        FileOutputStream out = null;
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            Writer writer = null;
            try {
                Date date = new Date(conn.getLastModified());
                writer = new FileWriter(timestamp);
                writer.write(this.FORMAT.format(date));
            } catch (IOException e) {
                timestamp.delete();
            } finally {
                IOUtils.closeQuietly(writer);
            }
            in = conn.getInputStream();
            out = new FileOutputStream(archive);
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
