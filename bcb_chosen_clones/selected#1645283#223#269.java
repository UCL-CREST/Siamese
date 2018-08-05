    public void saveAttributes(Attributes a) throws IOException {
        String extension = fileResourceExtent.getUpperCaseExtension();
        if ("XML".equals(extension)) {
            this.saveExtentsXML(a);
        } else {
            File file = new File(fileResourceExtent.getAbsoluteFilename());
            FileOutputStream fos = new FileOutputStream(file);
            ZipOutputStream zout = new ZipOutputStream(fos);
            zout.putNextEntry(new ZipEntry("Extents"));
            DataOutputStream out = new DataOutputStream(zout);
            saveExtentsBinary(out, a);
            out.close();
            fos.close();
        }
        extension = fileResourceMatrix.getUpperCaseExtension();
        if ("XML".equals(extension)) {
        } else {
            File file = new File(fileResourceMatrix.getAbsoluteFilename());
            FileOutputStream fos = new FileOutputStream(file);
            ZipOutputStream zout = new ZipOutputStream(fos);
            zout.putNextEntry(new ZipEntry("Matrix"));
            DataOutputStream out = new DataOutputStream(zout);
            saveMatrixBinary(out, a);
            out.close();
            fos.close();
        }
        extension = fileResourceAttributes.getUpperCaseExtension();
        if ("XML".equals(extension)) {
            this.saveAttributeXML(a);
        } else {
            File file = new File(fileResourceAttributes.getAbsoluteFilename());
            FileOutputStream fos = new FileOutputStream(file);
            ZipOutputStream zout = new ZipOutputStream(fos);
            zout.putNextEntry(new ZipEntry("Attributes"));
            DataOutputStream out = new DataOutputStream(zout);
            saveAttributesBinary(out, a);
            out.close();
            fos.close();
        }
        String extent_tree_filename_stem = "extent_tree_";
        for (int i = 0; i < a.getExtents().size(); i++) {
            try {
                saveXMLDocument(extent_tree_filename_stem + i + ".xml", a.getExtents().at(i).getExtentXML());
            } catch (Exception e) {
            }
        }
    }
