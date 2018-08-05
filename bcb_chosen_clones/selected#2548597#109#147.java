    public static void zip(Collection files, File zipFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFile);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            try {
                ZipOutputStream zos = new ZipOutputStream(bos);
                try {
                    for (Iterator i = files.iterator(); i.hasNext(); ) {
                        File file = (File) i.next();
                        zos.putNextEntry(new ZipEntry(file.getName()));
                        FileInputStream fis = new FileInputStream(file);
                        try {
                            BufferedInputStream bis = new BufferedInputStream(fis);
                            try {
                                while (true) {
                                    int j = bis.read();
                                    if (j == -1) {
                                        break;
                                    }
                                    zos.write(j);
                                }
                            } finally {
                                bis.close();
                            }
                        } finally {
                            fis.close();
                            zos.closeEntry();
                        }
                    }
                } finally {
                    zos.close();
                }
            } finally {
                bos.close();
            }
        } finally {
            fos.close();
        }
    }
