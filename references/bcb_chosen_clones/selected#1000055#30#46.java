    public static void copyAssetFile(Context ctx, String srcFileName, String targetFilePath) {
        AssetManager assetManager = ctx.getAssets();
        try {
            InputStream is = assetManager.open(srcFileName);
            File out = new File(targetFilePath);
            if (!out.exists()) {
                out.getParentFile().mkdirs();
                out.createNewFile();
            }
            OutputStream os = new FileOutputStream(out);
            IOUtils.copy(is, os);
            is.close();
            os.close();
        } catch (IOException e) {
            AIOUtils.log("error when copyAssetFile", e);
        }
    }
