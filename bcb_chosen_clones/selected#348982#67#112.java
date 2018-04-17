    private String[] read(String path) throws Exception {
        final String[] names = { "index.txt", "", "index.html", "index.htm" };
        String[] list = null;
        for (int i = 0; i < names.length; i++) {
            URL url = new URL(path + names[i]);
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuffer sb = new StringBuffer();
                String s = null;
                while ((s = in.readLine()) != null) {
                    s = s.trim();
                    if (s.length() > 0) {
                        sb.append(s + "\n");
                    }
                }
                in.close();
                if (sb.indexOf("<") != -1 && sb.indexOf(">") != -1) {
                    List links = LinkExtractor.scan(url, sb.toString());
                    HashSet set = new HashSet();
                    int prefixLen = path.length();
                    for (Iterator it = links.iterator(); it.hasNext(); ) {
                        String link = it.next().toString();
                        if (!link.startsWith(path)) {
                            continue;
                        }
                        link = link.substring(prefixLen);
                        int idx = link.indexOf("/");
                        int idxq = link.indexOf("?");
                        if (idx > 0 && (idxq == -1 || idx < idxq)) {
                            set.add(link.substring(0, idx + 1));
                        } else {
                            set.add(link);
                        }
                    }
                    list = (String[]) set.toArray(new String[0]);
                } else {
                    list = sb.toString().split("\n");
                }
                return list;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                continue;
            }
        }
        return new String[0];
    }
