    public void saveProjectFile(File aFile) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        File destDir = new File(theProjectsDirectory, sdf.format(Calendar.getInstance().getTime()));
        if (destDir.mkdirs()) {
            File outFile = new File(destDir, "project.xml");
            try {
                FileChannel sourceChannel = new FileInputStream(aFile).getChannel();
                FileChannel destinationChannel = new FileOutputStream(outFile).getChannel();
                sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
                sourceChannel.close();
                destinationChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                aFile.delete();
            }
        }
    }
