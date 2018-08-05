    public static boolean copyAllTo(java.io.File origin, java.io.File destiny) {
        boolean copied = true;
        if (!origin.exists()) {
            return false;
        }
        try {
            if (origin.isDirectory()) {
                for (java.io.File childOrigin : origin.listFiles()) {
                    if (childOrigin.isDirectory()) {
                        File childDestiny = new File(destiny.getAbsolutePath(), childOrigin.getName() + "/");
                        copied &= copyAllTo(childOrigin, childDestiny);
                    } else {
                        File childDestiny = new File(destiny.getAbsolutePath(), childOrigin.getName());
                        boolean copiedAux = copyTo(childOrigin, childDestiny);
                        copied &= copiedAux;
                        if (!copiedAux) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            copied = false;
        }
        return copied;
    }
