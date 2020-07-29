    public void insertStringInFile(String file, String textToInsert, long fromByte, long toByte) throws Exception {
        String tmpFile = file + ".tmp";
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        long byteCount = 0;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(file)));
            out = new BufferedOutputStream(new FileOutputStream(tmpFile));
            long size = fromByte;
            byte[] buf = null;
            if (size == 0) {
            } else {
                buf = new byte[(int) size];
                int length = -1;
                if ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                    byteCount = byteCount + length;
                } else {
                    String msg = "Failed to read the first '" + size + "' bytes of file '" + file + "'. This might be a programming error.";
                    logger.warning(msg);
                    throw new Exception(msg);
                }
            }
            buf = textToInsert.getBytes();
            int length = buf.length;
            out.write(buf, 0, length);
            byteCount = byteCount + length;
            long skipLength = toByte - fromByte;
            long skippedBytes = in.skip(skipLength);
            if (skippedBytes == -1) {
            } else {
                buf = new byte[4096];
                length = -1;
                while ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                    byteCount = byteCount + length;
                }
            }
            in.close();
            in = null;
            out.close();
            out = null;
            File fileToDelete = new File(file);
            boolean wasDeleted = fileToDelete.delete();
            if (!wasDeleted) {
                String msg = "Failed to delete the original file '" + file + "' to replace it with the modified file after text insertion.";
                logger.warning(msg);
                throw new Exception(msg);
            }
            File fileToRename = new File(tmpFile);
            boolean wasRenamed = fileToRename.renameTo(fileToDelete);
            if (!wasRenamed) {
                String msg = "Failed to rename tmp file '" + tmpFile + "' to the name of the original file '" + file + "'";
                logger.warning(msg);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to read/write file '" + file + "'.", e);
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.log(Level.FINEST, "Ignoring error closing input file '" + file + "'.", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.log(Level.FINEST, "Ignoring error closing output file '" + tmpFile + "'.", e);
                }
            }
        }
    }
