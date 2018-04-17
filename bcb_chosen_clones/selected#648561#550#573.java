    public void zipFile(FileEntry fileEntry) {
        String zipfilename = fileEntry.getPath();
        if (fileEntry.isDir()) {
            zipfilename += ".zip";
        } else {
            zipfilename = zipfilename.substring(0, zipfilename.length() - 4) + ".zip";
        }
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipfilename)));
            out.setLevel(Deflater.DEFAULT_COMPRESSION);
            byte[] data = new byte[1000];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileEntry.getPath()));
            int count;
            out.putNextEntry(new ZipEntry(fileEntry.getName()));
            while ((count = in.read(data, 0, 1000)) != -1) {
                out.write(data, 0, count);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
