    public void printReport(boolean portrait) {
        WebResponse response = this.getWebResponse();
        OutputStream out;
        try {
            out = response.getOutputStream(new ContentType("application/pdf"));
            response.setHeader("Content-disposition", "inline; filename=Report.pdf");
        } catch (Exception e) {
            log.error(e, e);
            return;
        }
        Document document = new Document(portrait ? PageSize.A4 : PageSize.A4.rotate(), 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document, out);
            ServletResolver resolver = new ServletResolver(((EquandaBasePage) getPage()).getServletContext());
            Phrase top = new Phrase();
            Image img = Image.getInstance(resolver.getImageRealPath(ResourceUtil.getConfigString(ConfigurationKeys.LOGO_FILE)));
            img.scaleToFit(300, 15);
            Chunk chk = new Chunk(img, 0, 0);
            top.add(chk);
            top.add(getMessages().getMessage("Table") + " " + getMessages().getMessage("table." + getSelectDescription().getTable().getName() + ".label") + " " + getMessages().getMessage("Report") + " " + getMessages().getMessage("select." + getSelectDescription().getTable().getName() + "." + getSelectDescription().getName() + ".label"));
            HeaderFooter header = new HeaderFooter(top, false);
            header.setAlignment(Element.ALIGN_LEFT);
            document.setHeader(header);
            document.setFooter(header);
            document.open();
            List<GMField> displayedFields = getDisplayedColumns(false);
            float[] percents = getReportPercents(displayedFields);
            PdfPTable table = new PdfPTable(percents);
            for (GMField field : displayedFields) {
                if (field instanceof FakeGmField) {
                    table.addCell("UOID");
                } else {
                    table.addCell(field.getName());
                }
            }
            table.setHeaderRows(1);
            List<EquandaProxy> results = getSelectObject().select();
            for (EquandaProxy proxy : results) {
                for (GMField field : displayedFields) {
                    if (field instanceof FakeGmField) {
                        table.addCell(proxy.getUOID().getId());
                    } else {
                        String str = "";
                        Object obj = EquandaProxyAccessor.getField(proxy, field.getName());
                        if (obj != null) {
                            int i = 0;
                            if (obj instanceof Collection) {
                                for (Object el : ((Collection) obj)) {
                                    if (el instanceof EquandaProxy) {
                                        str += EquandaProxyAccessor.getDisplay((EquandaProxy) el);
                                    } else {
                                        str += el.toString();
                                    }
                                    if (i < ((Collection) obj).size() - 1) str += ", ";
                                    i++;
                                }
                            } else if (obj instanceof EquandaProxy) {
                                str = EquandaProxyAccessor.getDisplay((EquandaProxy) obj);
                            } else if (obj instanceof Boolean) {
                                str = getMessages().getMessage(((Boolean) obj) ? "report_yes" : "report_no");
                            } else {
                                str = obj.toString();
                            }
                        } else {
                            str = "";
                        }
                        table.addCell(str);
                    }
                }
            }
            document.add(table);
        } catch (Exception e) {
            log.error(e, e);
        }
        try {
            if (document.isOpen()) document.close();
            out.flush();
        } catch (Exception e) {
            log.error(e, e);
        }
    }
