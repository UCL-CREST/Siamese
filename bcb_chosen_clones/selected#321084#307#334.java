    private void copy(String imgPath, String path) {
        try {
            File input = new File(imgPath);
            File output = new File(path, input.getName());
            if (output.exists()) {
                if (!MessageDialog.openQuestion(getShell(), "Overwrite", "There is already an image file " + input.getName() + " under the package.\n Do you really want to overwrite it?")) return;
            }
            byte[] data = new byte[1024];
            FileInputStream fis = new FileInputStream(imgPath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output));
            int length;
            while ((length = bis.read(data)) > 0) {
                bos.write(data, 0, length);
                bos.flush();
            }
            bos.close();
            fis.close();
            IJavaProject ijp = VisualSwingPlugin.getCurrentProject();
            if (ijp != null) {
                ijp.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
                view.refresh();
                view.expandAll();
            }
        } catch (Exception e) {
            VisualSwingPlugin.getLogger().error(e);
        }
    }
