    public static void copyTemplateDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            String[] children = srcDir.list();
            for (int i = 0; i < children.length; i++) {
                if (children[i].equals("CVS") || children[i].equals(".svn")) {
                    continue;
                }
                copyTemplateDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]));
            }
        } else {
            Pattern pattern = Pattern.compile("(\\\\fragment\\\\.*?\\.vm)|(/fragment/.*?\\.vm)|(\\.[\\w\\d]+\\.vm)|(\\..*?\\.groovy)|(\\..*?\\.ftl)|(.*?\\.zip)");
            Matcher matcher = pattern.matcher(srcDir.getPath());
            if (!matcher.find()) {
                copyFile(srcDir, dstDir);
            }
        }
    }
