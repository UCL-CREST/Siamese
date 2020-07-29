        public void flush() throws IOException {
            Log.print(this, "flush(): " + mCurrentPosition + " bytes");
            ZipEntry entry = new ZipEntry(Integer.toString(this.getNextZipEntryID()));
            mZipOutputStream.putNextEntry(entry);
            mZipOutputStream.write(mByteBuffer, 0, mCurrentPosition);
            mZipOutputStream.closeEntry();
            mZipOutputStream.flush();
            mCurrentPosition = 0;
        }
