	public static String convertDateFormat(String date, String srcFormat, String destFormat) throws ParseException {
		// Parse date string into a Date object using the source format
		DateFormat sFormat = new SimpleDateFormat(srcFormat);
		Date srcDate = sFormat.parse(date);
		
		// Format the data object to produce a string using the destination format
		DateFormat dFormat = new SimpleDateFormat(destFormat);
		String retval = dFormat.format(srcDate);
		
		// Return
		return retval;
	}
