    public FileReader(String filePath, Configuration aConfiguration) throws IOException {
        file = new File(URLDecoder.decode(filePath, "UTF-8")).getCanonicalFile();
        readerConf = aConfiguration;
        if (file.isDirectory()) {
            File indexFile = new File(file, "index.php");
            File indexFile_1 = new File(file, "index.html");
            if (indexFile.exists() && !indexFile.isDirectory()) {
                file = indexFile;
            } else if (indexFile_1.exists() && !indexFile_1.isDirectory()) {
                file = indexFile_1;
            } else {
                if (!readerConf.getOption("showFolders").equals("Yes")) {
                    makeErrorPage(503, "Permision denied");
                } else {
                    FileOutputStream out = new FileOutputStream(readerConf.getOption("wwwPath") + "/temp/temp.php");
                    File[] files = file.listFiles();
                    makeHeader(200, -1, new Date(System.currentTimeMillis()).toString(), "text/html");
                    String title = "Index of " + file;
                    out.write(("<html><head><title>" + title + "</title></head><body><h3>Index of " + file + "</h3><p>\n").getBytes());
                    for (int i = 0; i < files.length; i++) {
                        file = files[i];
                        String filename = file.getName();
                        String description = "";
                        if (file.isDirectory()) {
                            description = "&lt;DIR&gt;";
                        }
                        out.write(("<a href=\"" + file.getPath().substring(readerConf.getOption("wwwPath").length()) + "\">" + filename + "</a> " + description + "<br>\n").getBytes());
                    }
                    out.write(("</p><hr><p>yawwwserwer</p></body><html>").getBytes());
                    file = new File(URLDecoder.decode(readerConf.getOption("wwwPath") + "/temp/temp.php", "UTF-8")).getCanonicalFile();
                }
            }
        } else if (!file.exists()) {
            makeErrorPage(404, "File Not Found.");
        } else if (getExtension() == ".exe" || getExtension().contains(".py")) {
            FileOutputStream out = new FileOutputStream(readerConf.getOption("wwwPath") + "/temp/temp.php");
            out.write((runCommand(filePath)).getBytes());
            file = new File(URLDecoder.decode(readerConf.getOption("wwwPath") + "/temp/temp.php", "UTF-8")).getCanonicalFile();
        } else {
            System.out.println(getExtension());
            makeHeader(200, file.length(), new Date(file.lastModified()).toString(), TYPES.get(getExtension()).toString());
        }
        System.out.println(file);
    }
