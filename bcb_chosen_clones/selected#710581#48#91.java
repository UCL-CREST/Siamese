    public String downloadToSdCard(String localFileName, String suffixFromHeader, String extension) {
        InputStream in = null;
        FileOutputStream fos = null;
        String absolutePath = null;
        try {
            Log.i(TAG, "Opening URL: " + url);
            StreamAndHeader inAndHeader = HTTPUtils.openWithHeader(url, suffixFromHeader);
            if (inAndHeader == null || inAndHeader.mStream == null) {
                return null;
            }
            in = inAndHeader.mStream;
            String sdcardpath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            String headerValue = suffixFromHeader == null || inAndHeader.mHeaderValue == null ? "" : inAndHeader.mHeaderValue;
            headerValue = headerValue.replaceAll("[-:]*\\s*", "");
            String filename = sdcardpath + "/" + localFileName + headerValue + (extension == null ? "" : extension);
            mSize = in.available();
            Log.i(TAG, "Downloading " + filename + ", size: " + mSize);
            fos = new FileOutputStream(new File(filename));
            int buffersize = 1024;
            byte[] buffer = new byte[buffersize];
            int readsize = buffersize;
            mCount = 0;
            while (readsize != -1) {
                readsize = in.read(buffer, 0, buffersize);
                if (readsize > 0) {
                    Log.i(TAG, "Read " + readsize + " bytes...");
                    fos.write(buffer, 0, readsize);
                    mCount += readsize;
                }
            }
            fos.flush();
            fos.close();
            FileInputStream controlIn = new FileInputStream(filename);
            mSavedSize = controlIn.available();
            Log.v(TAG, "saved size: " + mSavedSize);
            mAbsolutePath = filename;
            done();
        } catch (Exception e) {
            Log.e(TAG, "LoadingWorker.run", e);
        } finally {
            HTTPUtils.close(in);
        }
        return mAbsolutePath;
    }
