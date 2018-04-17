    public void createZip(File zipFileName, Vector<File> selected) {
        try {
            byte[] buffer = new byte[4096];
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName), 8096));
            out.setLevel(Deflater.BEST_COMPRESSION);
            out.setMethod(ZipOutputStream.DEFLATED);
            for (int i = 0; i < selected.size(); i++) {
                FileInputStream in = new FileInputStream(selected.get(i));
                String file = selected.get(i).getPath();
                if (file.indexOf("\\") != -1) file = file.substring(file.lastIndexOf(fs) + 1, file.length());
                ZipEntry ze = new ZipEntry(file);
                out.putNextEntry(ze);
                int len;
                while ((len = in.read(buffer)) > 0) out.write(buffer, 0, len);
                out.closeEntry();
                in.close();
                selected.get(i).delete();
            }
            out.close();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
