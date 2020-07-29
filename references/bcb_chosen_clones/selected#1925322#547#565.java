    private void createImageArchive() throws Exception {
        imageArchive = new File(resoutFolder, "images.CrAr");
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(imageArchive)));
        out.writeInt(toNativeEndian(imageFiles.size()));
        for (int i = 0; i < imageFiles.size(); i++) {
            File f = imageFiles.get(i);
            out.writeLong(toNativeEndian(f.length()));
            out.writeLong(toNativeEndian(new File(resFolder, f.getName().substring(0, f.getName().length() - 5)).length()));
        }
        for (int i = 0; i < imageFiles.size(); i++) {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(imageFiles.get(i)));
            int read;
            while ((read = in.read()) != -1) {
                out.write(read);
            }
            in.close();
        }
        out.close();
    }
