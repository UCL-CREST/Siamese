    public boolean writeZip(TreeMap<Integer, Sprite> sprites, File file) {
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(file.toString().replaceAll(".pak", "_NEW.pak"))));
            out.setLevel(9);
            for (Entry<Integer, Sprite> e : sprites.entrySet()) {
                String name = String.valueOf(e.getKey());
                Sprite sprite = e.getValue();
                out.putNextEntry(new ZipEntry(name));
                out.write(sprite.pack().array());
                out.closeEntry();
            }
            out.close();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }
