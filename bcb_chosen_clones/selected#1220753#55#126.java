    AnnotationSearchResult match(String path, SegmentedTranscription st, SegmentedTier tier, String expression) {
        AnnotationSearchResult result = new AnnotationSearchResult();
        String transcriptionName = st.getHead().getMetaInformation().getTranscriptionName();
        String tierID = tier.getID();
        String speakerID = tier.getSpeaker();
        String speakerAbb = "";
        if (speakerID != null) {
            try {
                speakerAbb = st.getHead().getSpeakertable().getSpeakerWithID(speakerID).getAbbreviation();
            } catch (JexmaraldaException je) {
            }
        }
        Pattern pattern = Pattern.compile(expression);
        Vector matchingAnnotations = new Vector();
        for (int pos = 0; pos < tier.size(); pos++) {
            Object o = tier.elementAt(pos);
            if (!(o instanceof Annotation)) continue;
            Annotation a = (Annotation) o;
            String annotationName = a.getName();
            for (int annotationCount = 0; annotationCount < a.getNumberOfSegments(); annotationCount++) {
                Object o2 = a.elementAt(annotationCount);
                if (!(o2 instanceof TimedAnnotation)) continue;
                TimedAnnotation ta = (TimedAnnotation) o2;
                Matcher matcher = pattern.matcher(ta.getDescription());
                matcher.reset();
                while (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();
                    Object[] matchThingy = { annotationName, ta, new Integer(start), new Integer(end - start) };
                    matchingAnnotations.addElement(matchThingy);
                }
            }
        }
        Vector mappings = new Vector();
        for (int pos = 0; pos < tier.size(); pos++) {
            Object o = tier.elementAt(pos);
            if (!(o instanceof Segmentation)) continue;
            Segmentation s = (Segmentation) o;
            Hashtable mapping = new Hashtable();
            for (int segmentCount = 0; segmentCount < s.getNumberOfSegments(); segmentCount++) {
                Object o2 = s.elementAt(segmentCount);
                if (!(o2 instanceof TimedSegment)) continue;
                TimedSegment ts = (TimedSegment) o2;
                mapping.putAll(ts.indexTLIs());
            }
            mappings.addElement(mapping);
        }
        for (int pos = 0; pos < matchingAnnotations.size(); pos++) {
            Object[] matchThingy = ((Object[]) (matchingAnnotations.elementAt(pos)));
            TimedAnnotation annotation = (TimedAnnotation) (matchThingy[1]);
            String[] tlis = { annotation.getStart(), annotation.getEnd() };
            TimedSegment correspondingTimedSegment = null;
            for (int i = 0; i < mappings.size(); i++) {
                Hashtable mapping = (Hashtable) (mappings.elementAt(i));
                if (mapping.containsKey(tlis)) {
                    correspondingTimedSegment = (TimedSegment) (mapping.get(tlis));
                    break;
                } else if (mapping.containsKey(annotation.getStart()) && mapping.containsKey(annotation.getEnd())) {
                    TimedSegment ts1 = (TimedSegment) (mapping.get(annotation.getStart()));
                    TimedSegment ts2 = (TimedSegment) (mapping.get(annotation.getEnd()));
                    if (ts1 == ts2) {
                        correspondingTimedSegment = ts1;
                        break;
                    }
                }
            }
            if (correspondingTimedSegment == null) continue;
            AnnotationSearchResultItem asri = new AnnotationSearchResultItem(transcriptionName, path, tierID, speakerID, speakerAbb, correspondingTimedSegment, (TimedAnnotation) (matchThingy[1]), ((Integer) (matchThingy[2])).intValue(), ((Integer) (matchThingy[3])).intValue(), (String) (matchThingy[0]));
            result.addElement(asri);
        }
        return result;
    }
