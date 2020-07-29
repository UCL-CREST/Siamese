    public static List getUrlData(URL url) throws IOException {
        List beans = new ArrayList(256);
        System.out.println("Retreiving content for: " + url);
        StringBuffer content = new StringBuffer(4096);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String str;
        while ((str = in.readLine()) != null) {
            if (str.startsWith("#")) {
                continue;
            }
            StringTokenizer stringTokenizer = new StringTokenizer(str, "\t");
            String InteractionAc = stringTokenizer.nextToken();
            String reactomeId = stringTokenizer.nextToken();
            ReactomeBean reactomeBean = new ReactomeBean();
            reactomeBean.setReactomeID(reactomeId);
            reactomeBean.setInteractionAC(InteractionAc);
            beans.add(reactomeBean);
        }
        in.close();
        return beans;
    }
