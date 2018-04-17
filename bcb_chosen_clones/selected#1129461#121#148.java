    private static boolean setBundleInfoName(String location, List<BundleInfo> list) {
        try {
            URL url = new URL(location);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                int pos1 = line.indexOf('=');
                if (pos1 < 0) {
                    continue;
                }
                String bundleSymbolicName = line.substring(0, pos1);
                String bundleName = line.substring(pos1 + 1);
                for (BundleInfo info : list) {
                    if (info.bundleSymbolicName.equals(bundleSymbolicName)) {
                        info.bundleName = bundleName;
                        break;
                    }
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
