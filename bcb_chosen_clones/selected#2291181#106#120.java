    private void backupPageStore(ZipOutputStream out, String fileName, PageStore store) throws SQLException, IOException {
        Database db = session.getDatabase();
        fileName = FileUtils.getFileName(fileName);
        out.putNextEntry(new ZipEntry(fileName));
        int max = store.getPageCount();
        int pos = 0;
        while (true) {
            pos = store.copyDirect(pos, out);
            if (pos < 0) {
                break;
            }
            db.setProgress(DatabaseEventListener.STATE_BACKUP_FILE, fileName, pos, max);
        }
        out.closeEntry();
    }
