    private int getInsertionPoint(JobOffer jo) {
        HSSFRow row = null;
        int low = 1;
        int high = this.jobLastRow;
        int mid = 0;
        int sheetCodeValue = 0;
        int newCodeValue = Integer.parseInt(jo.getCode().trim());
        while (low <= high) {
            mid = (low + high) / 2;
            row = this.jobSheet.getRow(mid);
            sheetCodeValue = (int) row.getCell((short) 8).getNumericCellValue();
            if (newCodeValue < sheetCodeValue) {
                high = mid - 1;
            } else if (newCodeValue > sheetCodeValue) {
                low = mid + 1;
            } else {
                return -1;
            }
        }
        if (newCodeValue < sheetCodeValue) {
            return mid;
        }
        if (newCodeValue > sheetCodeValue) {
            return mid + 1;
        }
        return mid;
    }
