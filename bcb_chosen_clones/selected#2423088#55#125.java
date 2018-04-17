    private int parseTag(String html, Tag tag) {
        logger.debug("Step In at:" + tag.getTagType());
        int curIndex = 0;
        Matcher mTagHeadOrFoot;
        if (tag.getTagType().isOneTextChildOnly()) {
            String sPattern = String.format("<\\s*/\\s*%s\\s*>", tag.getTagName());
            Pattern pTheTagFoot = Pattern.compile(sPattern, Pattern.CASE_INSENSITIVE);
            mTagHeadOrFoot = pTheTagFoot.matcher(html);
        } else mTagHeadOrFoot = pTagHeadOrFoot.matcher(html);
        while (mTagHeadOrFoot.find()) {
            tagCount++;
            logger.debug("TAG_NUM:" + tagCount);
            String textBlock = html.substring(curIndex, mTagHeadOrFoot.start());
            if (textBlock.trim().length() > 0) {
                Tag newTag = new Tag(TagType.TEXT_BLOCK);
                newTag.setBody(textBlock);
                tag.addChild(newTag);
                logger.info("[TXT BODY]" + Tag.indents(tag.getDepth() - 1, "  ") + Tag.removeCR(textBlock));
                if (textBlock.matches("<.*>")) {
                    warnningCount++;
                    logger.warn("**problem text block[" + textBlock + "] near:" + getNearSegment(html, curIndex));
                }
            }
            String tagHeadOrFoot = mTagHeadOrFoot.group(0);
            logger.debug("tag:" + tagHeadOrFoot);
            if (!Tag.isStartTag(tagHeadOrFoot)) {
                String previousTagType = tag.getTagName();
                String currentTagType = Tag.extractTagName(tagHeadOrFoot);
                if (!previousTagType.equalsIgnoreCase(currentTagType)) {
                    warnningCount++;
                    String previousParentTagType = "";
                    try {
                        previousParentTagType = ((Tag) tag.getParent()).getTagName();
                    } catch (NullPointerException ex) {
                    }
                    logger.warn("***** tag expect:" + previousTagType + " but meet: " + currentTagType);
                    if (previousParentTagType.equalsIgnoreCase(currentTagType)) {
                        String fakeFoot = "</" + tag.getTagName() + ">";
                        logger.warn("[TAG FOOT]" + Tag.indents(tag.getDepth() - 1, "$$") + fakeFoot + "(AutoGenerate) near:" + getNearSegment(html, curIndex));
                        curIndex = mTagHeadOrFoot.start();
                        tag.setFoot(fakeFoot);
                        break;
                    } else {
                        logger.warn("[TAG FOOT]" + Tag.indents(tag.getDepth() - 1, "$$") + "</" + currentTagType + "> (AutoRemove), near:" + getNearSegment(html, curIndex));
                        curIndex = mTagHeadOrFoot.end();
                    }
                } else {
                    curIndex = mTagHeadOrFoot.end();
                    logger.info("[TAG FOOT]" + Tag.indents(tag.getDepth() - 1, "  ") + tagHeadOrFoot);
                    tag.setFoot(tagHeadOrFoot);
                    break;
                }
            } else {
                logger.info("[TAG HEAD]" + Tag.indents(tag.getDepth(), "  ") + Tag.removeCR(tagHeadOrFoot));
                Tag newTag = new Tag(tagHeadOrFoot);
                tag.addChild(newTag);
                if (newTag.getTagType().isSolo() || newTag.isStartAndEndTag()) {
                    logger.debug("TAG HAS NO BODY:" + newTag.getTagName());
                    curIndex = mTagHeadOrFoot.end();
                    continue;
                } else {
                    curIndex = mTagHeadOrFoot.end() + parseTag(html.substring(mTagHeadOrFoot.end()), newTag);
                    String content = html.substring(mTagHeadOrFoot.start(), curIndex);
                    newTag.setContent(content);
                    mTagHeadOrFoot.region(curIndex, html.length());
                }
            }
        }
        logger.debug("Step Out at:" + tag.getTagType());
        return curIndex;
    }
