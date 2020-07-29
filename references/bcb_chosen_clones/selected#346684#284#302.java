    boolean createSessionArchive(String archiveFilename) {
        byte[] buffer = new byte[1024];
        try {
            ZipOutputStream archive = new ZipOutputStream(new FileOutputStream(archiveFilename));
            for (mAnnotationsCursor.moveToFirst(); !mAnnotationsCursor.isAfterLast(); mAnnotationsCursor.moveToNext()) {
                FileInputStream in = new FileInputStream(mAnnotationsCursor.getString(ANNOTATIONS_FILE_NAME));
                archive.putNextEntry(new ZipEntry("audio" + (mAnnotationsCursor.getPosition() + 1) + ".3gpp"));
                int length;
                while ((length = in.read(buffer)) > 0) archive.write(buffer, 0, length);
                archive.closeEntry();
                in.close();
            }
            archive.close();
        } catch (IOException e) {
            Toast.makeText(mActivity, mActivity.getString(R.string.error_zip) + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
