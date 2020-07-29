    public void export() {
        Iterator<?> it = getCollection().iterator();
        try {
            Document document = new Document();
            OutputStream outputStream = getHttpServletResponse().getOutputStream();
            getHttpServletResponse().reset();
            getHttpServletResponse().setHeader("content-disposition", "attachment;filename=" + getC().getSimpleName() + ".pdf");
            getHttpServletResponse().setContentType("APPLICATION/pdf");
            PdfWriter.getInstance(document, outputStream);
            document.open();
            List<String> header = new ArrayList<String>();
            Method[] ms = getC().getMethods();
            for (int i = 0; i < ms.length; i++) {
                String name = ms[i].getName();
                if (name.startsWith("set")) {
                    header.add(name.substring(3));
                }
            }
            PdfPTable table = new PdfPTable(header.size());
            for (String s : header) {
                table.addCell(s);
            }
            while (it.hasNext()) {
                Object o = getC().cast(it.next());
                for (Method m : ms) {
                    String name = m.getName();
                    if (name.startsWith("get") && !name.startsWith("getClass")) {
                        table.addCell(m.invoke(o) == null ? ("") : (m.invoke(o).toString()));
                    }
                }
            }
            document.add(table);
            document.close();
            getHttpServletResponse().flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
