    @SuppressWarnings("unchecked")
    public static void unzip(String input, String output) {
        try {
            if (!output.endsWith("/")) output = output + "/";
            ZipFile zip = new ZipFile(input);
            Enumeration entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    FileUtils.forceMkdir(new File(output + entry.getName()));
                } else {
                    FileOutputStream out = new FileOutputStream(output + entry.getName());
                    IOUtils.copy(zip.getInputStream(entry), out);
                    IOUtils.closeQuietly(out);
                }
            }
        } catch (Exception e) {
            log.error("�����ҵ��ļ�:" + output, e);
            throw new RuntimeException("�����ҵ��ļ�:" + output, e);
        }
    }
