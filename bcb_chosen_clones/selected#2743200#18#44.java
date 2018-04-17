            public void run() {
                try {
                    FileOutputStream fos = new FileOutputStream(file + ".zip");
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    ZipOutputStream zos = new ZipOutputStream(bos);
                    File inFile = new File(file);
                    FileInputStream fis = new FileInputStream(inFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    ZipEntry zipEntry = new ZipEntry(inFile.getName());
                    zipEntry.setMethod(ZipEntry.DEFLATED);
                    zos.putNextEntry(zipEntry);
                    byte[] buffer = new byte[BUFFER];
                    int length;
                    while ((length = bis.read(buffer)) != -1) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    zos.close();
                    bis.close();
                    if (new File(file).exists()) {
                        boolean isDel = new File(file).delete();
                        if (isDel) _LOG(file + " 파일을 삭제 했습니다."); else _LOG(file + " 파일을 삭제하지 못 했습니다.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
