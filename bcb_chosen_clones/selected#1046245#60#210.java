    public byte[] getWaitingListStats(String username, EntityManager em, int year, int waitingListId, UserInfo userInfo, PropertiesResourcesFactory factory) throws Throwable {
        try {
            String path = this.getClass().getResource("/").getPath().replaceAll("%20", " ");
            String xlsFile = path + "waitingpeoplestats_" + factory.getResources().getLanguageId() + ".xls";
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(xlsFile));
            CustomWorkbook wb = new CustomWorkbook(fs);
            HSSFRow row = null;
            HSSFCell cell = null;
            HSSFSheet s = wb.getSheetAt(0);
            HSSFSheet newsheet = null;
            cell = s.getRow(3).getCell(4);
            cell.setCellValue(cell.getStringCellValue() + " " + userInfo.getFacilitiesCity());
            cell = s.getRow(23).getCell(4);
            cell.setCellValue(cell.getStringCellValue() + " " + userInfo.getFacilitiesCity());
            cell = s.getRow(43).getCell(4);
            cell.setCellValue(cell.getStringCellValue() + " " + userInfo.getFacilitiesCity());
            cell = s.getRow(3).getCell(7);
            cell.setCellValue(cell.getStringCellValue() + " " + userInfo.getFacilitiesCity());
            cell = s.getRow(23).getCell(7);
            cell.setCellValue(cell.getStringCellValue() + " " + userInfo.getFacilitiesCity());
            cell = s.getRow(43).getCell(7);
            cell.setCellValue(cell.getStringCellValue() + " " + userInfo.getFacilitiesCity());
            cell = s.getRow(63).getCell(9);
            cell.setCellValue(cell.getStringCellValue() + " " + userInfo.getFacilitiesCity());
            cell = s.getRow(83).getCell(9);
            cell.setCellValue(cell.getStringCellValue() + " " + userInfo.getFacilitiesCity());
            cell = s.getRow(103).getCell(9);
            cell.setCellValue(cell.getStringCellValue() + " " + userInfo.getFacilitiesCity());
            Object[][] data = getWaitingListStats(username, em, year, waitingListId, userInfo.getFacilitiesCountry(), userInfo.getFacilitiesCity(), null);
            int r = 4;
            int[] totals = new int[8];
            for (int i = 0; i < 12; i++) {
                row = s.getRow(r);
                for (int j = 0; j < 8; j++) {
                    cell = row.getCell(j + 1);
                    if (data[i][j] != null) {
                        cell.setCellValue((Integer) data[i][j]);
                        totals[j] += (Integer) data[i][j];
                    }
                }
                r++;
            }
            row = s.getRow(r);
            for (int j = 0; j < 8; j++) {
                cell = row.getCell(j + 1);
                if (j != 2) cell.setCellValue(totals[j]);
            }
            data = getWaitingListStats(username, em, year, waitingListId, userInfo.getFacilitiesCountry(), userInfo.getFacilitiesCity(), "M");
            r = 24;
            totals = new int[8];
            for (int i = 0; i < 12; i++) {
                row = s.getRow(r);
                for (int j = 0; j < 8; j++) {
                    cell = row.getCell(j + 1);
                    if (data[i][j] != null) {
                        cell.setCellValue((Integer) data[i][j]);
                        totals[j] += (Integer) data[i][j];
                    }
                }
                r++;
            }
            row = s.getRow(r);
            for (int j = 0; j < 8; j++) {
                cell = row.getCell(j + 1);
                if (j != 2) cell.setCellValue(totals[j]);
            }
            data = getWaitingListStats(username, em, year, waitingListId, userInfo.getFacilitiesCountry(), userInfo.getFacilitiesCity(), "F");
            r = 44;
            totals = new int[8];
            for (int i = 0; i < 12; i++) {
                row = s.getRow(r);
                for (int j = 0; j < 8; j++) {
                    cell = row.getCell(j + 1);
                    if (data[i][j] != null) {
                        cell.setCellValue((Integer) data[i][j]);
                        totals[j] += (Integer) data[i][j];
                    }
                }
                r++;
            }
            row = s.getRow(r);
            for (int j = 0; j < 8; j++) {
                cell = row.getCell(j + 1);
                if (j != 2) cell.setCellValue(totals[j]);
            }
            data = getWaitingListStatsPerSexAgeCountry(username, em, year, waitingListId, userInfo.getFacilitiesCountry(), userInfo.getFacilitiesCity(), null);
            r = 64;
            totals = new int[11];
            for (int i = 0; i < 12; i++) {
                row = s.getRow(r);
                for (int j = 0; j < 11; j++) {
                    cell = row.getCell(j + 1);
                    if (data[i][j] != null) {
                        cell.setCellValue((Integer) data[i][j]);
                        totals[j] += (Integer) data[i][j];
                    }
                }
                r++;
            }
            row = s.getRow(r);
            if (row != null) for (int j = 0; j < 11; j++) {
                cell = row.getCell(j + 1);
                cell.setCellValue(totals[j]);
            }
            data = getWaitingListStatsPerSexAgeCountry(username, em, year, waitingListId, userInfo.getFacilitiesCountry(), userInfo.getFacilitiesCity(), "M");
            r = 84;
            totals = new int[11];
            for (int i = 0; i < 12; i++) {
                row = s.getRow(r);
                for (int j = 0; j < 11; j++) {
                    cell = row.getCell(j + 1);
                    if (data[i][j] != null) {
                        cell.setCellValue((Integer) data[i][j]);
                        totals[j] += (Integer) data[i][j];
                    }
                }
                r++;
            }
            row = s.getRow(r);
            if (row != null) for (int j = 0; j < 11; j++) {
                cell = row.getCell(j + 1);
                cell.setCellValue(totals[j]);
            }
            data = getWaitingListStatsPerSexAgeCountry(username, em, year, waitingListId, userInfo.getFacilitiesCountry(), userInfo.getFacilitiesCity(), "F");
            r = 104;
            totals = new int[11];
            for (int i = 0; i < 12; i++) {
                row = s.getRow(r);
                for (int j = 0; j < 11; j++) {
                    cell = row.getCell(j + 1);
                    if (data[i][j] != null) {
                        cell.setCellValue((Integer) data[i][j]);
                        totals[j] += (Integer) data[i][j];
                    }
                }
                r++;
            }
            row = s.getRow(r);
            if (row != null) for (int j = 0; j < 11; j++) {
                cell = row.getCell(j + 1);
                cell.setCellValue(totals[j]);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            out.close();
            return out.toByteArray();
        } catch (Throwable ex) {
            Logger.error(null, ex.getMessage(), ex);
            throw ex;
        }
    }
