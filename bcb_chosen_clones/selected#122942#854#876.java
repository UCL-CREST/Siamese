    public void zipSubProject(ZipOutputStream zos, ProjectHolder aHolder) {
        for (Enumeration e = aHolder.getMembers().elements(); e.hasMoreElements(); ) {
            Member aMember = (Member) e.nextElement();
            if (!(aMember instanceof ProjectHolder)) {
                try {
                    File aFile = new File(aMember.getPath());
                    FileInputStream fr = new FileInputStream(aFile);
                    byte fileBuffer[] = new byte[(int) aFile.length()];
                    int count = fr.read(fileBuffer);
                    String pathName = aFile.getCanonicalPath();
                    zos.putNextEntry(new ZipEntry((pathName.substring(pathName.indexOf(System.getProperty("file.separator")) + 1)).replace(System.getProperty("file.separator").charAt(0), '/')));
                    zos.write(fileBuffer, 0, fileBuffer.length);
                    fr.close();
                } catch (Exception ex) {
                    System.err.println("Error creating zip project : " + ex);
                }
            }
        }
        for (Enumeration e = aHolder.getSubprojects().elements(); e.hasMoreElements(); ) {
            ProjectHolder aMember = (ProjectHolder) e.nextElement();
            zipSubProject(zos, aMember);
        }
    }
