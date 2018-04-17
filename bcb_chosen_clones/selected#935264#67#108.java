    public void exportarCarpeta(Component componente) {
        CarpetaTematica carpeta = (CarpetaTematica) componente.getAttribute("carpeta");
        if (carpeta != null) {
            try {
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ZipOutputStream zipOutput = new ZipOutputStream(byteOut);
                zipOutput.setLevel(6);
                FacadeConsultasConjuntosCassiaCore facade = SessionUtil.getFacadeConsultasConjuntosCassiaCore();
                ArrayList<Conjunto> contenido = new ArrayList(facade.consultarConjuntos(carpeta.getId(), SessionUtil.getUsuario(componente.getDesktop().getSession())));
                HashMap<Long, PerfilDocumentacion> perfiles = new HashMap<Long, PerfilDocumentacion>();
                for (Conjunto conjuntoHijo : contenido) {
                    conjuntoHijo = SessionUtil.getFacadeConsultasConjuntosCassiaCore().consultarConjunto(conjuntoHijo.getId());
                    PerfilDocumentacion perfil = perfiles.get(conjuntoHijo.getPerfil().getId());
                    if (perfil == null) {
                        perfil = SessionUtil.getFacadeConsultasPerfilesCassiaCore().consultarPerfil(conjuntoHijo.getPerfil());
                        perfiles.put(perfil.getId(), perfil);
                    }
                    String xml = SessionUtil.getFacadeConsultasConjuntosCassiaCore().exportarConjunto(conjuntoHijo, perfil);
                    byte buf[] = xml.getBytes();
                    CRC32 crc = new CRC32();
                    ZipEntry zipEntry = new ZipEntry(conjuntoHijo.getNombre() + ".xml");
                    zipEntry.setSize(buf.length);
                    crc.reset();
                    crc.update(buf);
                    zipEntry.setCrc(crc.getValue());
                    zipOutput.putNextEntry(zipEntry);
                    zipOutput.write(buf, 0, buf.length);
                }
                zipOutput.finish();
                zipOutput.close();
                Filedownload filedownload = new Filedownload();
                filedownload.save(new ByteArrayInputStream(byteOut.toByteArray()), "application/zip", "Conjuntos " + carpeta.getNombre() + ".zip");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Messagebox.show(Labels.getLabel("msg_carpeta_noseleccionado"), Labels.getLabel("msg_titulo_carpeta_seleccionado"), Messagebox.OK, Messagebox.QUESTION);
            } catch (InterruptedException e) {
            }
        }
    }
