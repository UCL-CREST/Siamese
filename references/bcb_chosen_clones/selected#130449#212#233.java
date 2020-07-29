    public Bitmap getImage() throws IOException {
        int recordBegin = 78 + 8 * mCount;
        Bitmap result = null;
        FileChannel channel = new FileInputStream(mFile).getChannel();
        channel.position(mRecodeOffset[mPage]);
        ByteBuffer bodyBuffer;
        if (mPage + 1 < mCount) {
            int length = mRecodeOffset[mPage + 1] - mRecodeOffset[mPage];
            bodyBuffer = channel.map(MapMode.READ_ONLY, mRecodeOffset[mPage], length);
            byte[] tmpCache = new byte[bodyBuffer.capacity()];
            bodyBuffer.get(tmpCache);
            FileOutputStream o = new FileOutputStream("/sdcard/test.bmp");
            o.write(tmpCache);
            o.flush();
            o.getFD().sync();
            o.close();
            result = BitmapFactory.decodeByteArray(tmpCache, 0, length);
        } else {
        }
        channel.close();
        return result;
    }
