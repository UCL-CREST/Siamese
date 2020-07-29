        protected void copyFile(File src, File dest) throws Exception {
            FileChannel srcChannel = new FileInputStream(src).getChannel();
            FileChannel destChannel = new FileOutputStream(dest).getChannel();
            long transferred = destChannel.transferFrom(srcChannel, 0, srcChannel.size());
            if (transferred != srcChannel.size()) throw new Exception("Could not transfer entire file");
            srcChannel.close();
            destChannel.close();
        }
