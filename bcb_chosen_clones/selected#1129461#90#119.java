    private static BundleInfo[] getBundleInfoArray(String location) throws IOException {
        URL url = new URL(location + BUNDLE_LIST_FILE);
        BufferedReader br = null;
        List<BundleInfo> list = new ArrayList<BundleInfo>();
        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                int pos1 = line.indexOf('=');
                if (pos1 < 0) {
                    continue;
                }
                BundleInfo info = new BundleInfo();
                info.bundleSymbolicName = line.substring(0, pos1);
                info.location = line.substring(pos1 + 1);
                list.add(info);
            }
            if (!setBundleInfoName(location + BUNDLE_NAME_LIST_FILE + "_" + Locale.getDefault().getLanguage(), list)) {
                setBundleInfoName(location + BUNDLE_NAME_LIST_FILE, list);
            }
            return list.toArray(BUNDLE_INFO_EMPTY_ARRAY);
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
