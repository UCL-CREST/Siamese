    public static void compressFromWeb(Vector urlList, String zipFileName) throws IOException {
        if (urlList == null || urlList.size() == 0) return;
        FileOutputStream fos = new FileOutputStream(zipFileName);
        ZipOutputStream zos = new ZipOutputStream(fos);
        Iterator iter = urlList.iterator();
        while (iter.hasNext()) {
            String fileUrl = (String) iter.next();
            if (fileUrl == null || fileUrl.trim().equals("")) continue;
            int ind = Math.max(fileUrl.lastIndexOf('/'), fileUrl.lastIndexOf('\\'));
            String shortName = "unknown";
            if (ind < fileUrl.length() - 1) shortName = fileUrl.substring(ind + 1);
            zos.putNextEntry(new ZipEntry(shortName));
            InputStream is = null;
            URL url = null;
            try {
                url = new URL(fileUrl);
                is = url.openStream();
            } catch (Exception e) {
                zos.closeEntry();
                if (is != null) is.close();
                continue;
            }
            byte[] buf = new byte[10000];
            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) zos.write(buf, 0, bytesRead);
            is.close();
            zos.closeEntry();
        }
        zos.close();
    }
