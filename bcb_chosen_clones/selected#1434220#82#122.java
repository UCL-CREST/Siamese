    @Override
    public void invoke(WorkflowContext context, ProgressMonitor monitor, Issues issues) {
        if (!check(issues)) {
            return;
        }
        try {
            byte[] buf = new byte[0xFFFF];
            ZipInputStream zin = new ZipInputStream(new FileInputStream(sourceFile));
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetFile));
            ZipEntry entry = zin.getNextEntry();
            while (entry != null) {
                out.putNextEntry(new ZipEntry(entry.getName()));
                int len;
                while ((len = zin.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                entry = zin.getNextEntry();
            }
            zin.close();
            File file = new File(fileToAdd);
            InputStream in = new FileInputStream(file);
            out.putNextEntry(new ZipEntry(newEntry));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
            out.close();
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            pw.close();
            try {
                sw.close();
            } catch (IOException e1) {
            }
            issues.addError("Exception occured!\n" + sw.toString());
        }
    }
