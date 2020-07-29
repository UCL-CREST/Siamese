    private void wrapExcel(String fileName, HttpServletResponse response, ZipOutputStream zipout, OutputStream out, Paginable<E> list, String subName) throws ExcelException {
        flushBuffer(response);
        HSSFWorkbook workbook = ExcelUtil.createWorkBook(fileName, getFieldMap(), list.getData());
        flushBuffer(response);
        InputStream inputStream = null;
        BufferedInputStream origin = null;
        try {
            inputStream = ExcelUtil.convertToInputStream(workbook);
            flushBuffer(response);
            zipout.putNextEntry(new ZipEntry(subName + ".xls"));
            byte[] buf = new byte[2048];
            origin = new BufferedInputStream(inputStream, 2048);
            int len;
            int icount = 0;
            while ((len = origin.read(buf, 0, 2048)) != -1) {
                zipout.write(buf, 0, len);
                icount++;
                if (icount > 10) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        logger.error("���EXCEL˯��ʱ�쳣", e);
                    } finally {
                        icount = 0;
                        zipout.flush();
                        out.flush();
                        flushBuffer(response);
                    }
                }
            }
            zipout.flush();
            out.flush();
            flushBuffer(response);
        } catch (IOException e) {
            throw new ExcelException("���EXCEL�����������쳣", e);
        } finally {
            if (origin != null) {
                try {
                    origin.close();
                } catch (IOException e) {
                    logger.error("���ѹ����ʱ�������ر��쳣", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("���ѹ���������ر��쳣", e);
                }
            }
            origin = null;
            inputStream = null;
        }
    }
