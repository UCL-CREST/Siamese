    private void addFiles(final File work, final ZipOutputStream zipStream) throws IOException {
        File[] files = work.listFiles();
        int cut = runnerDir.getAbsolutePath().length() - runnerDir.getName().length();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                addFiles(f, zipStream);
            } else if (!f.isHidden()) {
                String fileName = f.getAbsolutePath().substring(cut);
                zipStream.putNextEntry(new ZipEntry(fileName));
                InputStream input = new FileInputStream(f);
                byte[] buffer = new byte[1024];
                int b;
                while ((b = input.read(buffer)) != -1) zipStream.write(buffer, 0, b);
                input.close();
            }
        }
    }
