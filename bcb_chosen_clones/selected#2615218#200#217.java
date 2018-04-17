    private void copyFileToPhotoFolder(File photo, String personId) {
        try {
            FileChannel in = new FileInputStream(photo).getChannel();
            File dirServer = new File(Constants.PHOTO_DIR);
            if (!dirServer.exists()) {
                dirServer.mkdirs();
            }
            File fileServer = new File(Constants.PHOTO_DIR + personId + ".jpg");
            if (!fileServer.exists()) {
                fileServer.createNewFile();
            }
            in.transferTo(0, in.size(), new FileOutputStream(fileServer).getChannel());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
