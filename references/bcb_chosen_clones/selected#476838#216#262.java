    public void ajouterAuZip(String nomFile, Vector nomFilms, int nbFilm) throws FileNotFoundException {
        FileOutputStream fichierZip = new FileOutputStream(nomFile);
        ZipOutputStream zin = new ZipOutputStream(fichierZip);
        try {
            ZipEntry base = new ZipEntry("Base.xml");
            zin.putNextEntry(base);
            ZipEntry[] photo = new ZipEntry[nbFilm];
            zin.setLevel(9);
            byte[] buffer = new byte[512 * 1024];
            int nbLecture;
            java.io.FileInputStream sourceFile = new java.io.FileInputStream(new File("Base.xml"));
            while ((nbLecture = sourceFile.read(buffer)) != -1) {
                zin.write(buffer, 0, nbLecture);
            }
            zin.closeEntry();
            base = new ZipEntry("film.xsl");
            zin.putNextEntry(base);
            nbLecture = 0;
            sourceFile = new java.io.FileInputStream(new File("film.xsl"));
            while ((nbLecture = sourceFile.read(buffer)) != -1) {
                zin.write(buffer, 0, nbLecture);
            }
            zin.closeEntry();
            int i = 0;
            while (i < nbFilm) {
                File image = new File("image/" + nomFilms.get(i).toString() + ".jpg");
                if (image.exists()) {
                    java.io.FileInputStream photoFile = new java.io.FileInputStream(image);
                    photo[i] = new ZipEntry("image/" + nomFilms.get(i).toString() + ".jpg");
                    zin.putNextEntry(photo[i]);
                    while ((nbLecture = photoFile.read(buffer)) != -1) {
                        zin.write(buffer, 0, nbLecture);
                    }
                    zin.closeEntry();
                    photoFile.close();
                }
                i++;
            }
        } catch (java.io.FileNotFoundException fe) {
        } catch (java.io.IOException e) {
        } finally {
            try {
                zin.close();
            } catch (Exception e) {
            }
        }
    }
