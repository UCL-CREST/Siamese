    public static void writeZipFile(String location, Map<String, String> files) {
        Set<Entry<String, String>> entrySet = files.entrySet();
        ZipOutputStream zo = null;
        try {
            zo = new ZipOutputStream(new FileOutputStream(location));
            for (Iterator<Entry<String, String>> iter = entrySet.iterator(); iter.hasNext(); ) {
                Map.Entry<String, String> entry = iter.next();
                String zipFileName = entry.getKey();
                String zipFileContent = entry.getValue();
                ZipEntry zEntry = new ZipEntry(zipFileName);
                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(zipFileContent.getBytes()));
                int bytesRead;
                byte[] buffer = new byte[1024];
                zEntry.setMethod(ZipEntry.DEFLATED);
                zo.putNextEntry(zEntry);
                while ((bytesRead = bis.read(buffer)) != -1) {
                    zo.write(buffer, 0, bytesRead);
                }
                bis.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                zo.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
