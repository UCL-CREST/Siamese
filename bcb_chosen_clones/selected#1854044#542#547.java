    private static File cacheFromURLCore(String url, Context context) {
        CRC32 crc32 = new CRC32();
        crc32.update(url.getBytes());
        String ret = Long.toHexString(crc32.getValue());
        return new File(context.getCacheDir().getPath() + "/" + crc32_prefix_array[ret.length()] + ret);
    }
