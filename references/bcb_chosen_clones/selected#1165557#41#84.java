    @Override
    protected void doExport(IProgressReporter progressReporter) throws Exception {
        long total = getTotalTasks();
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(getFile()));
        document.open();
        int columns = tableModel.getColumns();
        float[] widths = new float[columns];
        int index = 0;
        java.util.Iterator<IColumnModel<E>> it = tableModel.getColumnModels();
        while (it.hasNext()) {
            IColumnModel<E> columnModel = it.next();
            widths[index] = columnModel.getWidth();
            index++;
        }
        PdfPTable table = new PdfPTable(widths);
        it = tableModel.getColumnModels();
        while (it.hasNext()) {
            IColumnModel<E> columnModel = it.next();
            PdfPCell cell = new PdfPCell(new Phrase(this.columnTranslations.get(columnModel.getPropertyPath())));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);
        }
        document.add(table);
        for (long i = 1; i <= total; i++) {
            if (progressReporter == null || progressReporter.isCanceled()) break;
            if (progressReporter != null) {
                progressReporter.setCurrentTask(i);
                progressReporter.setMessage("Exporting record " + i + " of " + total);
            }
            Thread.sleep(1);
            table = new PdfPTable(widths);
            E bean = pageableProvider.current();
            pageableProvider.next();
            it = tableModel.getColumnModels();
            while (it.hasNext()) {
                IColumnModel<E> columnModel = it.next();
                Object value = ReflectionUtils.getPropertyValue(bean, columnModel.getPropertyPath());
                if (value != null) table.addCell(value.toString()); else table.addCell("");
            }
            document.add(table);
        }
        document.close();
    }
