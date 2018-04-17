    private String processFileUploadOperation(boolean isH264File) {
        String fileType = this.uploadFileFileName.substring(this.uploadFileFileName.lastIndexOf('.'));
        int uniqueHashCode = UUID.randomUUID().toString().hashCode();
        if (uniqueHashCode < 0) {
            uniqueHashCode *= -1;
        }
        String randomFileName = uniqueHashCode + fileType;
        String fileName = (isH264File) ? getproperty("videoDraftPath") : getproperty("videoDraftPathForNonH264") + randomFileName;
        File targetVideoPath = new File(fileName + randomFileName);
        System.out.println("Path: " + targetVideoPath.getAbsolutePath());
        try {
            targetVideoPath.createNewFile();
            FileChannel outStreamChannel = new FileOutputStream(targetVideoPath).getChannel();
            FileChannel inStreamChannel = new FileInputStream(this.uploadFile).getChannel();
            inStreamChannel.transferTo(0, inStreamChannel.size(), outStreamChannel);
            outStreamChannel.close();
            inStreamChannel.close();
            return randomFileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
