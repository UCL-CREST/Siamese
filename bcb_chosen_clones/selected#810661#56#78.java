    private static void process(File webRoot, File dir, String template, ZipOutputStream os) throws IOException {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                process(webRoot, file, template, os);
                continue;
            }
            Matcher htmlFileMatcher = Generators.HTML_EXT_MATCH.matcher(file.getName());
            if (htmlFileMatcher.matches()) {
                String fileRelativePath = LightBoundUtil.removePrefix(file.getCanonicalPath(), webRoot.getCanonicalPath());
                while (fileRelativePath.startsWith("/")) {
                    fileRelativePath = fileRelativePath.substring(1);
                }
                String jspContents = template.replace(TEMPLATE_PAGE_PATH, fileRelativePath);
                Matcher relativePathMatcher = Generators.HTML_EXT_MATCH.matcher(fileRelativePath);
                relativePathMatcher.matches();
                String jspFilename = relativePathMatcher.group(Generators.HTML_FILENAME_GROUP) + ".jsp";
                System.out.println("Generated JSP file: '" + jspFilename + "' which " + "refers to '" + fileRelativePath + "'");
                os.putNextEntry(new ZipEntry(jspFilename));
                os.write(jspContents.getBytes(JSP_FILE_CHARSET));
                os.closeEntry();
            }
        }
    }
