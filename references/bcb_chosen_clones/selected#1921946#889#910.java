    public static String CopyFile(String sourcefile, String destfile) throws FileNotFoundException, IOException {
        int last = destfile.lastIndexOf('/');
        if (last < 0) {
            DrxWriteError("CopyFile", "Destination filepath " + destfile + " doesn't contain /");
            throw new java.io.FileNotFoundException(destfile);
        }
        String parent = destfile.substring(0, last);
        if (parent.length() > 0) {
            File f = new File(parent);
            if (!f.isDirectory()) {
                if (!f.mkdirs()) {
                    DrxWriteError("CopyFile", "Folder " + parent + " doesn't exist, cannot create");
                }
            }
        }
        FileChannel srcChannel = new FileInputStream(sourcefile).getChannel();
        FileChannel dstChannel = new FileOutputStream(destfile).getChannel();
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();
        dstChannel.close();
        return destfile;
    }
