    @Override
    protected void writeFile() {
        super.writeFile();
        try {
            String tagListFilePath = file.toURI().toASCIIString();
            tagListFilePath = tagListFilePath.substring(0, tagListFilePath.lastIndexOf(FileManager.GLIPS_VIEW_EXTENSION)) + FileManager.TAG_LIST_FILE_EXTENSION;
            File tagListFile = new File(new URI(tagListFilePath));
            StringBuffer buffer = new StringBuffer("");
            for (String tagName : tags) {
                buffer.append(tagName + "\n");
            }
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer.toString().getBytes("UTF-8"));
            FileOutputStream out = new FileOutputStream(tagListFile);
            FileChannel channel = out.getChannel();
            channel.write(byteBuffer);
            channel.close();
        } catch (Exception ex) {
        }
        try {
            String parentPath = file.getParentFile().toURI().toASCIIString();
            if (!parentPath.endsWith("/")) {
                parentPath += "/";
            }
            File srcFile = null, destFile = null;
            byte[] tab = new byte[1000];
            int nb = 0;
            InputStream in = null;
            OutputStream out = null;
            for (String destinationName : dataBaseFiles.keySet()) {
                srcFile = dataBaseFiles.get(destinationName);
                if (srcFile != null) {
                    destFile = new File(new URI(parentPath + destinationName));
                    in = new BufferedInputStream(new FileInputStream(srcFile));
                    out = new BufferedOutputStream(new FileOutputStream(destFile));
                    while (in.available() > 0) {
                        nb = in.read(tab);
                        if (nb > 0) {
                            out.write(tab, 0, nb);
                        }
                    }
                    in.close();
                    out.flush();
                    out.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
