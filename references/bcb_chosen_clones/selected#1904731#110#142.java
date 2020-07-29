    public void copyFilesIntoProject(HashMap<String, String> files) {
        Set<String> filenames = files.keySet();
        for (String key : filenames) {
            String realPath = files.get(key);
            if (key.equals("fw4ex.xml")) {
                try {
                    FileReader in = new FileReader(new File(realPath));
                    FileWriter out = new FileWriter(new File(project.getLocation() + "/" + bundle.getString("Stem") + STEM_FILE_EXETENSION));
                    int c;
                    while ((c = in.read()) != -1) out.write(c);
                    in.close();
                    out.close();
                } catch (FileNotFoundException e) {
                    Activator.getDefault().showMessage("File " + key + " not found... Error while moving files to the new project.");
                } catch (IOException ie) {
                    Activator.getDefault().showMessage("Error while moving " + key + " to the new project.");
                }
            } else {
                try {
                    FileReader in = new FileReader(new File(realPath));
                    FileWriter out = new FileWriter(new File(project.getLocation() + "/" + key));
                    int c;
                    while ((c = in.read()) != -1) out.write(c);
                    in.close();
                    out.close();
                } catch (FileNotFoundException e) {
                    Activator.getDefault().showMessage("File " + key + " not found... Error while moving files to the new project.");
                } catch (IOException ie) {
                    Activator.getDefault().showMessage("Error while moving " + key + " to the new project.");
                }
            }
        }
    }
