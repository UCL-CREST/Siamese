    public void save(File f, AudioFileFormat.Type t) throws IOException {
        if (t.getExtension().equals("raw")) {
            IOUtils.copy(makeInputStream(), new FileOutputStream(f));
        } else {
            AudioSystem.write(makeStream(), t, f);
        }
    }
