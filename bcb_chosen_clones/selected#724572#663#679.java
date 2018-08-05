    @SuppressWarnings("unchecked")
    public static <T extends Date> T parse(String dateString, String dateFormat, Class<T> targetResultType) {
        if (StringUtil.isEmpty(dateString)) {
            return null;
        }
        DateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        try {
            long time = simpleDateFormat.parse(dateString).getTime();
            Date tempDate = targetResultType.getConstructor(long.class).newInstance(time);
            return (T) tempDate;
        } catch (ParseException parseException) {
            String errorInfo = "无法使用格式：[" + dateFormat + "]解析日期字符串：[" + dateString + "]";
            throw new IllegalArgumentException(errorInfo, parseException);
        } catch (Exception exception) {
            throw new IllegalArgumentException("目标结果类型：[" + targetResultType.getName() + "]错误", exception);
        }
    }
