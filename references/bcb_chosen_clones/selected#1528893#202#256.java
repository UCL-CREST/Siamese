    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF", "pdf", "PDF");
        fc.setFileFilter(filter);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fc.getSelectedFile();
            String ruta = file.getPath();
            ruta = (ruta.toLowerCase().endsWith(".pdf")) ? ruta : (ruta + ".pdf");
            Document document = new Document(PageSize.A4);
            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(ruta));
                writer.setPdfVersion(PdfWriter.VERSION_1_6);
                document.addTitle("Reporte_Inventario");
                document.open();
                Image foto = Image.getInstance("/home/kreuz_katze/Imágenes/DCED_logo.png");
                foto.scaleToFit(500, 500);
                foto.setAlignment(Chunk.ALIGN_CENTER);
                document.add(foto);
                Paragraph par = new Paragraph("Reporte de articulos en el Inventario", FontFactory.getFont("arial", 14, BaseColor.BLACK));
                par.setIndentationLeft(par.ALIGN_JUSTIFIED_ALL);
                par.setAlignment(par.TITLE);
                par.getExtraParagraphSpace();
                int columnas = jTable1.getColumnCount();
                int filas = jTable1.getRowCount();
                String numCadena[] = new String[300];
                int y = 0;
                for (int n = 0; n < filas; n++) {
                    for (int m = 0; m < columnas; m++) {
                        numCadena[y] = String.valueOf(jTable1.getValueAt(n, m));
                        y++;
                    }
                }
                String enc[] = new String[10];
                for (int m = 0; m < columnas; m++) {
                    enc[m] = String.valueOf(jTable1.getColumnModel().getColumn(m).getHeaderValue());
                }
                PdfPTable tabla = new PdfPTable(columnas);
                for (int i = 0; i < columnas; i++) {
                    tabla.addCell(new Paragraph(enc[i], FontFactory.getFont("arial", 8, BaseColor.BLACK)));
                }
                int total = columnas * filas;
                for (int i = 0; i < total; i++) {
                    tabla.addCell(new Paragraph(numCadena[i], FontFactory.getFont("arial", 6, BaseColor.DARK_GRAY)));
                }
                document.add(par);
                document.add(new Paragraph("  "));
                document.add(new Paragraph("  "));
                document.add(tabla);
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
