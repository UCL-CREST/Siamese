    private long buildBookmarkFile(FileOutputStream outfstream) throws IOException {
        CRC32 crc = new CRC32();
        ByteArrayOutputStream bufstream = new ByteArrayOutputStream(512);
        DataOutputStream bout = new DataOutputStream(bufstream);
        Cursor cursor = getContentResolver().query(Browser.BOOKMARKS_URI, new String[] { BookmarkColumns.URL, BookmarkColumns.VISITS, BookmarkColumns.DATE, BookmarkColumns.CREATED, BookmarkColumns.TITLE }, BookmarkColumns.BOOKMARK + " == 1 ", null, null);
        int count = cursor.getCount();
        if (DEBUG) Log.v(TAG, "Backing up " + count + " bookmarks");
        bout.writeInt(count);
        byte[] record = bufstream.toByteArray();
        crc.update(record);
        outfstream.write(record);
        for (int i = 0; i < count; i++) {
            cursor.moveToNext();
            String url = cursor.getString(0);
            int visits = cursor.getInt(1);
            long date = cursor.getLong(2);
            long created = cursor.getLong(3);
            String title = cursor.getString(4);
            bufstream.reset();
            bout.writeUTF(url);
            bout.writeInt(visits);
            bout.writeLong(date);
            bout.writeLong(created);
            bout.writeUTF(title);
            record = bufstream.toByteArray();
            crc.update(record);
            outfstream.write(record);
            if (DEBUG) Log.v(TAG, "   wrote url " + url);
        }
        cursor.close();
        return crc.getValue();
    }
