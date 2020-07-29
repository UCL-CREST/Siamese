    private static List<InputMethodDescriptor> loadIMDescriptors() {
        String nm = SERVICES + InputMethodDescriptor.class.getName();
        Enumeration<URL> en;
        LinkedList<InputMethodDescriptor> imdList = new LinkedList<InputMethodDescriptor>();
        NativeIM nativeIM = ContextStorage.getNativeIM();
        imdList.add(nativeIM);
        try {
            en = ClassLoader.getSystemResources(nm);
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            while (en.hasMoreElements()) {
                URL url = en.nextElement();
                InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String str = br.readLine();
                while (str != null) {
                    str = str.trim();
                    int comPos = str.indexOf("#");
                    if (comPos >= 0) {
                        str = str.substring(0, comPos);
                    }
                    if (str.length() > 0) {
                        imdList.add((InputMethodDescriptor) cl.loadClass(str).newInstance());
                    }
                    str = br.readLine();
                }
            }
        } catch (Exception e) {
        }
        return imdList;
    }
