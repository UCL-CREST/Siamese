    public static void main(String[] args) {
        FileUtils file = new FileUtils(Resource.getResourceStream("aboutMe.txt"));
        try {
            ZipOutputStream zos = file.getZipOputStream(new File("a.zip"));
            zos.putNextEntry(new ZipEntry("aboutMe.txt"));
            zos.write(file.readText().getBytes());
            zos.finish();
            zos.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
