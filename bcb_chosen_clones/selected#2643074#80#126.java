    final void saveProject(Project project, final File file) {
        if (projectsList.contains(project)) {
            if (project.isDirty() || !file.getParentFile().equals(workspaceDirectory)) {
                try {
                    if (!file.exists()) {
                        if (!file.createNewFile()) throw new IOException("cannot create file " + file.getAbsolutePath());
                    }
                    File tmpFile = File.createTempFile("JFPSM", ".tmp");
                    ZipOutputStream zoStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                    zoStream.setMethod(ZipOutputStream.DEFLATED);
                    ZipEntry projectXMLEntry = new ZipEntry("project.xml");
                    projectXMLEntry.setMethod(ZipEntry.DEFLATED);
                    zoStream.putNextEntry(projectXMLEntry);
                    CustomXMLEncoder encoder = new CustomXMLEncoder(new BufferedOutputStream(new FileOutputStream(tmpFile)));
                    encoder.writeObject(project);
                    encoder.close();
                    int bytesIn;
                    byte[] readBuffer = new byte[1024];
                    FileInputStream fis = new FileInputStream(tmpFile);
                    while ((bytesIn = fis.read(readBuffer)) != -1) zoStream.write(readBuffer, 0, bytesIn);
                    fis.close();
                    ZipEntry entry;
                    String floorDirectory;
                    for (FloorSet floorSet : project.getLevelSet().getFloorSetsList()) for (Floor floor : floorSet.getFloorsList()) {
                        floorDirectory = "levelset/" + floorSet.getName() + "/" + floor.getName() + "/";
                        for (MapType type : MapType.values()) {
                            entry = new ZipEntry(floorDirectory + type.getFilename());
                            entry.setMethod(ZipEntry.DEFLATED);
                            zoStream.putNextEntry(entry);
                            ImageIO.write(floor.getMap(type).getImage(), "png", zoStream);
                        }
                    }
                    final String tileDirectory = "tileset/";
                    for (Tile tile : project.getTileSet().getTilesList()) for (int textureIndex = 0; textureIndex < tile.getMaxTextureCount(); textureIndex++) if (tile.getTexture(textureIndex) != null) {
                        entry = new ZipEntry(tileDirectory + tile.getName() + textureIndex + ".png");
                        entry.setMethod(ZipEntry.DEFLATED);
                        zoStream.putNextEntry(entry);
                        ImageIO.write(tile.getTexture(textureIndex), "png", zoStream);
                    }
                    zoStream.close();
                    tmpFile.delete();
                } catch (IOException ioe) {
                    throw new RuntimeException("The project " + project.getName() + " cannot be saved!", ioe);
                }
            }
        } else throw new IllegalArgumentException("The project " + project.getName() + " is not handled by this project set!");
    }
