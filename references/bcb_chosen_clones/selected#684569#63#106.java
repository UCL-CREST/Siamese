    @SuppressWarnings("unchecked")
    public static void zip(String input, OutputStream out) {
        File file = new File(input);
        ZipOutputStream zip = null;
        FileInputStream in = null;
        try {
            if (file.exists()) {
                Collection<File> items = new ArrayList();
                if (file.isDirectory()) {
                    items = FileUtils.listFiles(file, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
                    zip = new ZipOutputStream(out);
                    zip.putNextEntry(new ZipEntry(file.getName() + "/"));
                    Iterator iter = items.iterator();
                    while (iter.hasNext()) {
                        File item = (File) iter.next();
                        in = new FileInputStream(item);
                        zip.putNextEntry(new ZipEntry(file.getName() + "/" + item.getName()));
                        IOUtils.copy(in, zip);
                        IOUtils.closeQuietly(in);
                        zip.closeEntry();
                    }
                    IOUtils.closeQuietly(zip);
                }
            } else {
                log.info("-->>���ļ���û���ļ�");
            }
        } catch (Exception e) {
            log.error("����ѹ��" + input + "�������", e);
            throw new RuntimeException("����ѹ��" + input + "�������", e);
        } finally {
            try {
                if (null != zip) {
                    zip.close();
                    zip = null;
                }
                if (null != in) {
                    in.close();
                    in = null;
                }
            } catch (Exception e) {
                log.error("�ر��ļ�������");
            }
        }
    }
