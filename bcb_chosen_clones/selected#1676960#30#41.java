    @RequestMapping("/import")
    public String importPicture(@ModelAttribute PictureImportCommand command) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        URL url = command.getUrl();
        IOUtils.copy(url.openStream(), baos);
        byte[] imageData = imageFilterService.touchupImage(baos.toByteArray());
        String filename = StringUtils.substringAfterLast(url.getPath(), "/");
        String email = userService.getCurrentUser().getEmail();
        Picture picture = new Picture(email, filename, command.getDescription(), imageData);
        pictureRepository.store(picture);
        return "redirect:/picture/gallery";
    }
