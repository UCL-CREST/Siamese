    public static Vector<Service> getServices(JTree projectTree) {
        Vector<Service> ret = new Vector<Service>();
        ProjectTreeModel model = (ProjectTreeModel) projectTree.getModel();
        Document doc = model.getDocument();
        NodeList services = doc.getElementsByTagName("services");
        services = ((Element) services.item(0)).getElementsByTagName("service");
        Element serviceElement = doc.getDocumentElement();
        for (int i = 0; i < services.getLength(); i++) {
            serviceElement = (Element) services.item(i);
            Element name = (Element) serviceElement.getElementsByTagName("name").item(0);
            String serviceName = name.getTextContent();
            String paneName = "org.compas." + serviceName + "." + serviceName + "ParameterPane";
            String runName = "org.compas." + serviceName + ".Run" + serviceName + "Service";
            try {
                Class paneClass = Class.forName(paneName);
                Class[] paramClasses = { projectTree.getClass() };
                Object[] params = { projectTree };
                ParameterPane pane = (ParameterPane) paneClass.getConstructor(paramClasses).newInstance(params);
                Class runClass = Class.forName(runName);
                RunService run = (RunService) runClass.getConstructor(paramClasses).newInstance(params);
                Service service = new Service(pane, run, serviceName, projectTree);
                ret.add(service);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Project file malformed. Check for correct syntax etc.", "Error during XML Parsing", JOptionPane.ERROR_MESSAGE);
            } catch (SecurityException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Project file malformed. Check for correct syntax etc.", "Error during XML Parsing", JOptionPane.ERROR_MESSAGE);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Project file malformed. Check for correct syntax etc.", "Error during XML Parsing", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Project file malformed. Check for correct syntax etc.", "Error during XML Parsing", JOptionPane.ERROR_MESSAGE);
            } catch (InstantiationException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Project file malformed. Check for correct syntax etc.", "Error during XML Parsing", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Project file malformed. Check for correct syntax etc.", "Error during XML Parsing", JOptionPane.ERROR_MESSAGE);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Project file malformed. Check for correct syntax etc.", "Error during XML Parsing", JOptionPane.ERROR_MESSAGE);
            }
        }
        return ret;
    }
