    private static void deleteFiles(File[] files) {
        for (int n = 0, i = files != null ? files.length : 0; n < i; n++) {
            if (files[n].exists()) {
                if (files[n].isFile()) {
                    if (!files[n].delete()) {
                        throw new CommonException(Language.translateStatic("ERROR_COULDNOTDELETEFILE").replace("$1", files[n].toString()));
                    }
                } else if (files[n].isDirectory()) {
                    deleteFiles(files[n].listFiles());
                    if (!files[n].delete()) {
                        throw new CommonException(Language.translateStatic("ERROR_COULDNOTDELETEFILE").replace("$1", files[n].toString()));
                    }
                }
            }
        }
    }
