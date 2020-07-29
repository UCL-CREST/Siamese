    public void uploadFile(ActionEvent event) throws IOException {
        InputFile inputFile = (InputFile) event.getSource();
        synchronized (inputFile) {
            ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String fileNewPath = arrangeUplodedFilePath(context.getRealPath(""), inputFile.getFile().getName());
            File file = new File(fileNewPath);
            System.out.println(fileNewPath);
            DataInputStream inStream = new DataInputStream(new FileInputStream(inputFile.getFile()));
            DataOutputStream outStream = new DataOutputStream(new FileOutputStream(file));
            int i = 0;
            byte[] buffer = new byte[512];
            while ((i = inStream.read(buffer, 0, 512)) != -1) outStream.write(buffer, 0, i);
        }
    }
