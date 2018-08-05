    public ServiceInfo[] findServices(String name) {
        Vector results = new Vector();
        String service_file = ServiceDiscovery.SERVICE_HOME + name;
        for (int loader_count = 0; loader_count < class_loaders_.size(); loader_count++) {
            ClassLoader loader = (ClassLoader) class_loaders_.elementAt(loader_count);
            Enumeration enumeration = null;
            try {
                enumeration = loader.getResources(service_file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (enumeration == null) continue;
            while (enumeration.hasMoreElements()) {
                try {
                    URL url = (URL) enumeration.nextElement();
                    InputStream is = url.openStream();
                    if (is != null) {
                        try {
                            BufferedReader rd;
                            try {
                                rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                            } catch (java.io.UnsupportedEncodingException e) {
                                rd = new BufferedReader(new InputStreamReader(is));
                            }
                            try {
                                String service_class_name;
                                while ((service_class_name = rd.readLine()) != null) {
                                    service_class_name.trim();
                                    if ("".equals(service_class_name)) continue;
                                    if (service_class_name.startsWith("#")) continue;
                                    ServiceInfo sinfo = new ServiceInfo();
                                    sinfo.setClassName(service_class_name);
                                    sinfo.setLoader(loader);
                                    sinfo.setURL(url);
                                    results.add(sinfo);
                                }
                            } finally {
                                rd.close();
                            }
                        } finally {
                            is.close();
                        }
                    }
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException ioe) {
                    ;
                }
            }
        }
        ServiceInfo result_array[] = new ServiceInfo[results.size()];
        results.copyInto(result_array);
        return (result_array);
    }
