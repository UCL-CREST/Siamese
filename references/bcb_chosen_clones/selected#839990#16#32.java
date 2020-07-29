    public void zipFile(final File childfile, final ZipOutputStream out, final byte[] data) {
        System.out.println("Adding: " + childfile.getName());
        FileInputStream fi;
        try {
            fi = new FileInputStream(childfile);
            final BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
            final ZipEntry entry = new ZipEntry(childfile.getName());
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) out.write(data, 0, count);
            origin.close();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
