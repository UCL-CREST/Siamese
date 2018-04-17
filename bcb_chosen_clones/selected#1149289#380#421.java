    private String processCSSPath(String text, Host host, String extension, String delimiter1, String delimiter2, String endDelimiter, String url) throws DotHibernateException {
        Pattern p = Pattern.compile(delimiter1 + "[^" + delimiter1 + "]*\\." + extension + delimiter2);
        Matcher m = p.matcher(text);
        StringBuilder sb = new StringBuilder();
        int prevIndex = 0;
        while (m.find()) {
            String match = m.group();
            match = match.substring(1, match.length() - 1);
            sb.append(text.substring(prevIndex, m.start() + 1));
            prevIndex = m.end();
            if (match.toLowerCase().startsWith("http://") || match.toLowerCase().startsWith("https://")) {
                sb.append(match + endDelimiter);
                sb.append(text.substring(prevIndex));
                return sb.toString();
            }
            if (!match.startsWith("/")) {
                match = url + match;
                match = cleanPath(match);
            }
            String relativePath = LiveCache.getPathFromCache(match, host);
            File f = new File(FileFactory.getRealAssetsRootPath() + relativePath);
            if (!f.exists()) {
                Logger.warn(this, "Invalid path passed: path = " + relativePath + ", file doesn't exists.");
                f = new File(context.getRealPath(match));
                if (f.exists()) {
                    sb.append("file:///" + f.getAbsolutePath() + endDelimiter);
                }
            } else {
                String inode = UtilMethods.getFileName(f.getName());
                com.dotmarketing.portlets.files.model.File file = FileCache.getFileByInode(inode);
                Identifier identifier = IdentifierCache.getIdentifierFromIdentifierCache(file);
                if (!perAPI.doesUserHavePermission(identifier, PERMISSION_READ, user)) {
                    Logger.warn(this, "Not authorized: path = " + relativePath);
                } else {
                    String path = f.getAbsolutePath();
                    sb.append("file:///" + path + endDelimiter);
                }
            }
        }
        sb.append(text.substring(prevIndex));
        return sb.toString();
    }
