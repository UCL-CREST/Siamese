    public static File enregistrerFichier(String fileName, File file, String path, String fileMime) throws Exception {
        if (file != null) {
            try {
                HttpServletRequest request = ServletActionContext.getRequest();
                HttpSession session = request.getSession();
                String pathFile = session.getServletContext().getRealPath(path) + File.separator + fileName;
                File outfile = new File(pathFile);
                String[] nomPhotoTab = fileName.split("\\.");
                String extension = nomPhotoTab[nomPhotoTab.length - 1];
                StringBuffer pathResBuff = new StringBuffer(nomPhotoTab[0]);
                for (int i = 1; i < nomPhotoTab.length - 1; i++) {
                    pathResBuff.append(".").append(nomPhotoTab[i]);
                }
                String pathRes = pathResBuff.toString();
                String nomPhoto = fileName;
                for (int i = 0; !outfile.createNewFile(); i++) {
                    nomPhoto = pathRes + "_" + +i + "." + extension;
                    pathFile = session.getServletContext().getRealPath(path) + File.separator + nomPhoto;
                    outfile = new File(pathFile);
                }
                logger.debug(" enregistrerFichier - Enregistrement du fichier : " + pathFile);
                FileChannel in = null;
                FileChannel out = null;
                try {
                    in = new FileInputStream(file).getChannel();
                    out = new FileOutputStream(outfile).getChannel();
                    in.transferTo(0, in.size(), out);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                        }
                    }
                }
                return outfile;
            } catch (IOException e) {
                logger.error("Erreur lors de l'enregistrement de l'image ", e);
                throw new Exception("Erreur lors de l'enregistrement de l'image ");
            }
        }
        return null;
    }
