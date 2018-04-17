    public static void loadPages() throws IOException {
        ClassLoader ldr = Thread.currentThread().getContextClassLoader();
        Collection<Class<? extends BasePage>> pages = new ArrayList<Class<? extends BasePage>>();
        Enumeration<URL> e = ldr.getResources("META-INF/services/" + Page.class.getName());
        while (e.hasMoreElements()) {
            URL url = e.nextElement();
            InputStream is = url.openStream();
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while (true) {
                    String line = r.readLine();
                    if (line == null) break;
                    int comment = line.indexOf('#');
                    if (comment >= 0) line = line.substring(0, comment);
                    String name = line.trim();
                    if (name.length() == 0) continue;
                    Class<?> clz = Class.forName(name, true, ldr);
                    if (BasePage.class.isAssignableFrom(clz)) {
                        pages.add(clz.asSubclass(BasePage.class));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (Exception ex) {
                }
            }
        }
        pageTypes = pages;
    }
