    public static String[] listFilesInJar(String resourcesLstName, String dirPath, String ext) {
        try {
            dirPath = Tools.subString(dirPath, "\\", "/");
            if (!dirPath.endsWith("/")) {
                dirPath = dirPath + "/";
            }
            if (dirPath.startsWith("/")) {
                dirPath = dirPath.substring(1, dirPath.length());
            }
            URL url = ResourceLookup.getClassResourceUrl(Tools.class, resourcesLstName);
            if (url == null) {
                String msg = "File not found " + resourcesLstName;
                Debug.signal(Debug.ERROR, null, msg);
                return new String[0];
            }
            InputStream is = url.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String name = in.readLine();
            HashSet<String> list = new HashSet<String>(10);
            while (name != null) {
                name = in.readLine();
                if (name == null) {
                    continue;
                }
                if (ext != null && !name.endsWith(ext)) {
                    continue;
                }
                if (name.indexOf('.') == -1 && !name.endsWith("/")) {
                    name = name + "/";
                }
                int index = name.indexOf(dirPath);
                if (index < 0) {
                    continue;
                }
                index += dirPath.length();
                if (index >= name.length() - 1) {
                    continue;
                }
                index = name.indexOf("/", index);
                if (ext != null && (name.endsWith("/") || index >= 0)) {
                    continue;
                } else if (ext == null && (index < 0 || index < name.length() - 1)) {
                    continue;
                }
                list.add("/" + name);
            }
            is.close();
            String[] toReturn = {};
            return list.toArray(toReturn);
        } catch (IOException ioe) {
            String msg = "Error reading file " + resourcesLstName + " caused by " + ioe;
            Debug.signal(Debug.ERROR, null, msg);
            return new String[0];
        }
    }
