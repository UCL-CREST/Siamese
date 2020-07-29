    private static void makeBody(File directory, StringBuffer buffer) throws IOException {
        File[] files = directory.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(".svg");
            }
        });
        ZipOutputStream zipOut = new ZipOutputStream(new java.io.FileOutputStream(new File(directory, "svgsigns.zip")));
        Arrays.sort(files, new Comparator() {

            public int compare(Object o1, Object o2) {
                File f1 = (File) o1;
                File f2 = (File) o2;
                String s1 = GardinerCode.getCodeForFileName(f1.getName());
                if (s1 == null) s1 = f1.getName();
                String s2 = GardinerCode.getCodeForFileName(f2.getName());
                if (s2 == null) s2 = f2.getName();
                return GardinerCode.compareCodes(s1, s2);
            }
        });
        buffer.append("<ul>\n");
        for (int i = 0; i < files.length; i++) {
            File imageFile = FileUtils.buildFileWithExtension(files[i], "png");
            try {
                buildImageFile(files[i], imageFile);
                buffer.append("<li>");
                buffer.append(files[i].getName());
                if (buildLinks) {
                    buffer.append("<a href=\"");
                    buffer.append(files[i].getName());
                    buffer.append("\"> ");
                }
                buffer.append("<img src=\"");
                buffer.append(imageFile.getName());
                File commentFile = FileUtils.buildFileWithExtension(files[i], "txt");
                buffer.append("\"> ");
                if (buildLinks) buffer.append("</a>\n");
                buffer.append(extractXMLInfo(files[i]));
                if (commentFile.exists()) {
                    Reader commentReader = new FileReader(commentFile);
                    int c;
                    while ((c = commentReader.read()) != -1) {
                        buffer.append((char) c);
                    }
                    commentReader.close();
                }
                java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry("svgsigns/" + files[i].getName());
                zipOut.putNextEntry(entry);
                java.io.FileInputStream svgin = new java.io.FileInputStream(files[i]);
                byte[] bytes = new byte[(int) files[i].length()];
                svgin.read(bytes);
                svgin.close();
                zipOut.write(bytes);
                zipOut.closeEntry();
            } catch (Exception e) {
                System.err.println("Could not build " + imageFile);
                e.printStackTrace();
            }
        }
        buffer.append("</ul>\n");
        zipOut.close();
        buffer.append("<b> directory content as <a href=\"svgsigns.zip\"> zip file </a>");
    }
