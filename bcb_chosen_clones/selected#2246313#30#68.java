    public static void copyFile(String source, String destination, TimeSlotTracker timeSlotTracker) {
        LOG.info("copying [" + source + "] to [" + destination + "]");
        BufferedInputStream sourceStream = null;
        BufferedOutputStream destStream = null;
        try {
            File destinationFile = new File(destination);
            if (destinationFile.exists()) {
                destinationFile.delete();
            }
            sourceStream = new BufferedInputStream(new FileInputStream(source));
            destStream = new BufferedOutputStream(new FileOutputStream(destinationFile));
            int readByte;
            while ((readByte = sourceStream.read()) > 0) {
                destStream.write(readByte);
            }
            Object[] arg = { destinationFile.getName() };
            String msg = timeSlotTracker.getString("datasource.xml.copyFile.copied", arg);
            LOG.fine(msg);
        } catch (Exception e) {
            Object[] expArgs = { e.getMessage() };
            String expMsg = timeSlotTracker.getString("datasource.xml.copyFile.exception", expArgs);
            timeSlotTracker.errorLog(expMsg);
            timeSlotTracker.errorLog(e);
        } finally {
            try {
                if (destStream != null) {
                    destStream.close();
                }
                if (sourceStream != null) {
                    sourceStream.close();
                }
            } catch (Exception e) {
                Object[] expArgs = { e.getMessage() };
                String expMsg = timeSlotTracker.getString("datasource.xml.copyFile.exception", expArgs);
                timeSlotTracker.errorLog(expMsg);
                timeSlotTracker.errorLog(e);
            }
        }
    }
