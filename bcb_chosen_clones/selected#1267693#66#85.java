	@Override
	public final ArrayList<Occurrence> findPattern(final String regExPatternToFind, final String stringToParse)
		{
		final Pattern COMPARISON_PATTERN = Pattern.compile(regExPatternToFind);
		final Matcher COMPARATOR = COMPARISON_PATTERN.matcher(stringToParse);

		final ArrayList<Occurrence> OCCURRENCE_LIST = new ArrayList<>();

		while (COMPARATOR.find())
			{
			final String foundString = COMPARATOR.group();
			final int startPosition = COMPARATOR.start();
			final int endPosition = COMPARATOR.end();

			final Occurrence occurrence = new Occurrence(foundString, startPosition, endPosition);

			OCCURRENCE_LIST.add(occurrence);
			}
		return OCCURRENCE_LIST;
		}
