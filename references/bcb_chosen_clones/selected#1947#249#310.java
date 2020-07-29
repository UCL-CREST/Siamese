    public static boolean saveToTempFile(Context context, String filePath, DirType dirType, String tempSavePath, boolean secrete) {
        FileOutputStream fos = null;
        InputStream in = null;
        byte[] buffer = new byte[1024];
        int readLength = 0;
        boolean result = false;
        try {
            try {
                File f = new File(context.getFilesDir().getAbsolutePath() + File.separator + tempSavePath);
                if (f.exists()) {
                    context.deleteFile(tempSavePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            fos = context.openFileOutput(tempSavePath, Context.MODE_WORLD_READABLE);
            logger.error("tempfile:" + tempSavePath);
            if (dirType == DirType.assets) {
                AssetManager assetManager = context.getAssets();
                in = assetManager.open(filePath);
            } else if (dirType == DirType.file && Constant.getUpdateDataPath() != null) {
                in = new FileInputStream(Constant.getUpdateDataPath() + File.separator + filePath);
            } else if (dirType == DirType.sd && Constant.getSdPath() != null) {
                in = new FileInputStream(Constant.getSdPath() + File.separator + filePath);
            } else if (dirType == DirType.extSd && Constant.getExtSdPath() != null) {
                in = new FileInputStream(Constant.getExtSdPath() + File.separator + filePath);
            }
            if (in == null) {
                return false;
            }
            readLength = in.read(buffer);
            if (readLength >= ZipToFile.encrypLength && secrete) {
                byte[] encrypByte = new byte[ZipToFile.encrypLength];
                System.arraycopy(buffer, 0, encrypByte, 0, ZipToFile.encrypLength);
                byte[] temp = CryptionControl.getInstance().decryptECB(encrypByte, ZipToFile.rootKey);
                System.arraycopy(temp, 0, buffer, 0, ZipToFile.encrypLength);
            }
            while (readLength > 0) {
                fos.write(buffer, 0, readLength);
                fos.flush();
                readLength = in.read(buffer);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        return result;
    }
