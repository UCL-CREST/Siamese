    private void packFile(final File file, final ZipOutputStream out, final String name, final FileFilter filter) throws IOException {
        if (filter != null && !filter.accept(file)) return;
        if (file.isDirectory()) {
            final File[] list = file.listFiles();
            if (list == null) return;
            for (final File element : list) if (name == null) packFile(element, out, file.getName(), filter); else packFile(element, out, name + "/" + file.getName(), filter);
        } else {
            ZipEntry entry = null;
            if (name == null) entry = new ZipEntry(file.getName()); else entry = new ZipEntry(name + "/" + file.getName());
            try {
                out.putNextEntry(entry);
            } catch (final ZipException e) {
                throw new C4JRuntimeException(format("Could not pack file ‘%s’.", file.getPath()), e);
            }
            InputStream fileIn = null;
            try {
                fileIn = new FileInputStream(file);
                use_filetools().copyStream2Stream(fileIn, out);
            } finally {
                if (fileIn != null) fileIn.close();
            }
            out.closeEntry();
        }
    }
