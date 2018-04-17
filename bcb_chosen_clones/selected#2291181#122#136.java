    private void backupDiskFile(ZipOutputStream out, String fileName, DiskFile file) throws SQLException, IOException {
        Database db = session.getDatabase();
        fileName = FileUtils.getFileName(fileName);
        out.putNextEntry(new ZipEntry(fileName));
        int pos = -1;
        int max = file.getReadCount();
        while (true) {
            pos = file.copyDirect(pos, out);
            if (pos < 0) {
                break;
            }
            db.setProgress(DatabaseEventListener.STATE_BACKUP_FILE, fileName, pos, max);
        }
        out.closeEntry();
    }
