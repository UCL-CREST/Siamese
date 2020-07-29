    public static File copyFile(File file, String dirName) {
        File destDir = new File(dirName);
        if (!destDir.exists() || !destDir.isDirectory()) {
            destDir.mkdirs();
        }
        File src = file;
        File dest = new File(dirName, src.getName());
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            FileChannel source = new FileInputStream(src).getChannel();
            FileChannel destination = new FileOutputStream(dest).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }
