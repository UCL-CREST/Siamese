    public boolean backupLastAuditSchema(File lastAuditSchema) {
        boolean isBkupFileOK = false;
        String writeTimestamp = DateFormatUtils.format(new java.util.Date(), configFile.getTimestampPattern());
        File target = new File(configFile.getAuditSchemaFileDir() + File.separator + configFile.getAuditSchemaFileName() + ".bkup_" + writeTimestamp);
        FileChannel sourceChannel = null;
        FileChannel targetChannel = null;
        try {
            sourceChannel = new FileInputStream(lastAuditSchema).getChannel();
            targetChannel = new FileOutputStream(target).getChannel();
            targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception occurred while copying file", e);
        } finally {
            if ((target != null) && (target.exists()) && (target.length() > 0)) {
                isBkupFileOK = true;
            }
            try {
                if (sourceChannel != null) {
                    sourceChannel.close();
                }
                if (targetChannel != null) {
                    targetChannel.close();
                }
            } catch (IOException e) {
                logger.warning("closing channels failed");
            }
        }
        return isBkupFileOK;
    }
