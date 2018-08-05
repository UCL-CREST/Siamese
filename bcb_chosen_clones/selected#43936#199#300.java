    public File createZip(AnnotationFileList afList, int type, String zip_file_name) {
        File temp_zip_dir = new File(TEMP_CO);
        if (!temp_zip_dir.isDirectory()) {
            temp_zip_dir.mkdirs();
        }
        String absolute_zip_file = TEMP_CO + File.separator + zip_file_name;
        byte b[] = new byte[512];
        File zip_file = null;
        FileOutputStream zip_stream = null;
        try {
            zip_file = new File(absolute_zip_file);
            zip_file.createNewFile();
            zip_stream = new FileOutputStream(absolute_zip_file);
        } catch (java.io.FileNotFoundException fnfe) {
            derr(" createZip: '" + absolute_zip_file + "' not found. ");
        } catch (java.io.IOException ioe) {
            derr(" createZip: IOException when creating new file '" + absolute_zip_file + "' ");
        }
        if (zip_stream == null) {
            return null;
        } else {
            try {
                ZipOutputStream zout = new ZipOutputStream(zip_stream);
                zout.setComment(afList.size() + " Files in " + zip_file_name);
                String fl = "";
                int fc = 0;
                derr(" createZip: zipping " + afList.size() + " files... ");
                for (Iterator i = afList.iterator(); i.hasNext(); ) {
                    AnnotationFile af = (AnnotationFile) i.next();
                    File sourceFile = af.getSourceFile();
                    String sourceFileZipName = af.getSourceFileZipName();
                    File annotationFile = null;
                    try {
                        annotationFile = af.getAnnotationFile(type);
                    } catch (InvalidAnnotationFileTypeException iafte) {
                        iafte.printStackTrace();
                        derr(" createZip: " + iafte.getMessage());
                    }
                    String annotationFileZipName = af.getAnnotationFileZipName();
                    InputStream in = null;
                    if (sourceFile != null && sourceFile.exists() && sourceFile.canRead()) {
                        try {
                            in = new FileInputStream(sourceFile);
                            ZipEntry e = new ZipEntry(sourceFileZipName);
                            e.setTime(sourceFile.lastModified());
                            e.setComment("Source File (" + af.getSourceFileID() + ")");
                            zout.putNextEntry(e);
                            int len = 0;
                            while ((len = in.read(b)) != -1) {
                                zout.write(b, 0, len);
                            }
                            zout.closeEntry();
                            fl += e + ((i.hasNext()) ? ", " : " ");
                            fc++;
                        } catch (java.io.FileNotFoundException fnfe) {
                            derr(" createZip: Source file not found: '" + sourceFile.getAbsolutePath() + "'");
                            fnfe.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        derr(" createZip: Problem with source file:" + ((sourceFile != null) ? sourceFile.getAbsolutePath() : sourceFile.toString()));
                        if (sourceFile != null) {
                            derr(" createZip: exists:" + sourceFile.exists() + " canRead:" + sourceFile.canRead());
                        }
                    }
                    if (annotationFile != null && annotationFile.exists() && annotationFile.canRead()) {
                        try {
                            in = new FileInputStream(annotationFile);
                            ZipEntry e = new ZipEntry(annotationFileZipName);
                            e.setTime(sourceFile.lastModified());
                            e.setComment("Annotation File (pwh" + af.getPreviousWorkflowHistoryID() + "/wh" + af.getWorkflowHistoryID() + ")");
                            zout.putNextEntry(e);
                            int len = 0;
                            while ((len = in.read(b)) != -1) {
                                zout.write(b, 0, len);
                            }
                            zout.closeEntry();
                            fl += e + ((i.hasNext()) ? ", " : " ");
                            fc++;
                        } catch (java.io.FileNotFoundException fnfe) {
                            derr(" createZip: Source file not found: '" + annotationFile.getAbsolutePath() + "'");
                            fnfe.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        derr(" createZip: Problem with source file: " + ((annotationFile != null) ? annotationFile.getAbsolutePath() : "[null] type:" + type + " af_history_valid:" + af.validHistory()));
                        if (annotationFile != null) {
                            derr(" createZip: exists:" + annotationFile.exists() + " canRead:" + annotationFile.canRead());
                        }
                    }
                }
                dmsg(" createZip: User:(" + user + ") Zipped " + fc + " files : \n'" + fl + "' .");
                zout.close();
            } catch (java.io.IOException ioe) {
                derr(" createZip: IOException when zipping to '" + absolute_zip_file + "' ");
                ioe.printStackTrace();
            }
        }
        return zip_file;
    }
