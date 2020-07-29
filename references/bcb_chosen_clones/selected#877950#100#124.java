    public static InputStream getExcelInputStream(ExcelModule excelModule, String templeteFile) throws Exception {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(templeteFile);
        ByteArrayInputStream bas = null;
        HSSFWorkbook wb = new HSSFWorkbook(fis);
        cellStyle = wb.createCellStyle();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            HSSFSheet sheet = wb.getSheetAt(i);
            if (sheet == null || sheet.getPhysicalNumberOfRows() == 0) {
                continue;
            }
            if (StringUtil.isNotEmpty(excelModule.getSheetName(i))) {
                wb.setSheetName(i, excelModule.getSheetName(i));
            }
            int strNumRow = getRowNum(sheet);
            setOnceValue(wb, sheet, i, excelModule, strNumRow);
            setMultiValue(wb, sheet, i, excelModule, strNumRow);
        }
        wb.write(bao);
        byte[] ba = bao.toByteArray();
        bas = new ByteArrayInputStream(ba);
        bao.close();
        fis.close();
        return bas;
    }
