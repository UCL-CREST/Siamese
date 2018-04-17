    public void doWrite() {
        System.out.print("\nSerializing...");
        try {
            IFile ifile = null;
            File file = null;
            if (null != method.getResource()) {
                ifile = method.getJavaProject().getResource().getProject().getFile(Statics.CFG_DIR + Statics.SEPARATOR + path.substring(path.lastIndexOf(Statics.SEPARATOR)));
            }
            file = new File(path);
            if (file.exists()) {
                boolean ans = MessageDialog.openQuestion(wb.getSite().getShell(), "Flow Plug-in", "File already exists. Do you want to overwrite it?");
                if (ans) {
                    file.delete();
                }
            }
            if (!file.exists()) {
                FileOutputStream fos = new FileOutputStream(path);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                BufferedOutputStream bo = new BufferedOutputStream(oos);
                oos.writeObject(anode);
                oos.flush();
                oos.close();
                InputStream is = new FileInputStream(path);
                if (null != ifile) {
                    if (ifile.exists()) ifile.delete(true, null);
                    ifile.create(is, IResource.NONE, null);
                }
                Path fullpath = new Path(path);
                IDE.openEditorOnFileStore(wb.getSite().getPage(), EFS.getLocalFileSystem().getStore(fullpath));
                method.getResource().refreshLocal(10, null);
                System.out.println("Serializing ...Done!");
            }
        } catch (PartInitException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }
