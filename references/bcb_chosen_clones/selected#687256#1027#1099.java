    public void addFile(File fileobj, boolean delete) {
        String oldFileName = fileobj.getPath();
        String currFileName = setUpFile(fileobj);
        if (currFileName != null) {
            File f = new File(currFileName);
            int deleteFiles = JOptionPane.CANCEL_OPTION;
            if (oldFileName.equals(currFileName)) {
                currFileName = currFileName.substring(openProject.getPath().length());
                openProject.addFile(currFileName);
                if (f.getName().toLowerCase().endsWith(".exp")) addExpFile(f.getPath());
            } else if (!f.exists() || JOptionPane.OK_OPTION == (deleteFiles = JOptionPane.showConfirmDialog(this, "File" + f.getName() + " Already Exists! Do You Wish To Overwrite That File?" + (f.getName().toLowerCase().endsWith(".exp") ? "\nOverwriting An Expression File Will Delete All Files Which Previously Required The Orginal File" : "")))) {
                try {
                    if (deleteFiles == JOptionPane.OK_OPTION && f.getName().toLowerCase().endsWith(".exp")) {
                        File expF[] = f.getParentFile().listFiles();
                        for (int i = 0; i < expF.length; i++) {
                            while (expF[i].exists()) {
                                expF[i].delete();
                            }
                        }
                        f.getParentFile().delete();
                    }
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                    FileInputStream in = new FileInputStream(fileobj);
                    FileOutputStream out = new FileOutputStream(f);
                    byte[] buffer = new byte[8 * 1024];
                    int count = 0;
                    do {
                        out.write(buffer, 0, count);
                        count = in.read(buffer, 0, buffer.length);
                    } while (count != -1);
                    in.close();
                    out.close();
                    if (delete) fileobj.delete();
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(this, "Error! Could Not Add " + fileobj.getName() + " To Project");
                }
                currFileName = currFileName.substring(currFileName.lastIndexOf(openProject.getName()) + openProject.getName().length() + 1);
                openProject.addFile(currFileName);
                if (f.getName().toLowerCase().endsWith(".exp")) addExpFile(f.getPath());
            }
        } else {
            String message = "Error! Could Not Add " + fileobj.getName() + " To Project\n";
            if (fileobj.getName().endsWith(".gprj")) {
                message += "You May Not Add A Project File To An Existing Project";
            } else if (fileobj.getName().toLowerCase().endsWith(".ds_store")) {
                message = "";
            } else if (fileobj.getName().toLowerCase().endsWith(".txt")) {
                message = "";
            } else if (fileobj.getName().toLowerCase().endsWith(".gif")) {
                message = "";
            } else if (fileobj.getName().toLowerCase().endsWith(".jpeg")) {
                message = "";
            } else if (fileobj.getName().toLowerCase().endsWith(".jpg")) {
                message = "";
            } else if (fileobj.getName().toLowerCase().endsWith(".info")) {
                message = "";
            } else if (fileobj.getName().toLowerCase().endsWith(".html")) {
                message = "";
            } else if (fileobj.getName().toLowerCase().endsWith(".db")) {
                message = "";
            } else if (fileobj.getName().toLowerCase().endsWith(".raw")) {
                message = "";
            } else if (fileobj.getName().toLowerCase().endsWith(".cdt")) {
                message = "";
            } else if (fileobj.getName().toLowerCase().endsWith(".gtr")) {
                message = "";
            } else if (fileobj.getName().toLowerCase().endsWith(".jtv")) {
                message = "";
            } else message += "File Extension Unknown. Please Check The File To Ensure It Has The Correct Extension";
            if (!message.equals("")) JOptionPane.showMessageDialog(this, message);
        }
    }
