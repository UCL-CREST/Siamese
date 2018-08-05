    public static void writeProjectToFile(Project p, File proj_file) throws Exception {
        ZipOutputStream zout = openZipArchive(proj_file);
        String xml = SerializationHelper.getXMLForProject(p);
        writeXMLToZipArchive("quickvol_project.xml", xml, zout);
        if (p.getStack().getLayers() != null) {
            for (int a = 0; a < p.getStack().getLayers().length; a++) {
                String fname = p.getStack().getLayers()[a].getName();
                String entryname = null;
                if (fname.toLowerCase().endsWith(".png")) entryname = fname; else {
                    entryname = fname + ".png";
                }
                ZipEntry entry = new ZipEntry(entryname);
                zout.putNextEntry(entry);
                javax.imageio.ImageIO.write(p.getStack().getLayers()[a].getOrigImage(), "png", zout);
            }
        }
        zout.close();
    }
