    public void startFileConversion() {
        FileManagement.startTime = System.currentTimeMillis();
        FileManagement.fetchAllFiles(new File(sourceDir), javaFileList);
        Collections.sort(javaFileList);
        for (JavaFileBean javaFile : javaFileList) {
            performConversion(javaFile);
        }
        htmlManagementIndex = new HTMLManagementIndexFile();
        htmlManagementIndex.createIndexHtmlFile(javaFileList, destinationDir);
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            URI uri = null;
            try {
                uri = new URI("file://" + destinationDir.replace('\\', '/') + "/index.html");
                desktop.browse(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
