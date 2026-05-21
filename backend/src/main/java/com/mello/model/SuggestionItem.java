package com.mello.model;

public class SuggestionItem {
    private final String title;
    private final String description;
    private final String emojiUrl;
    private final String emojiAlt;

    public SuggestionItem(String title, String description, String emojiUrl, String emojiAlt) {
        this.title = title;
        this.description = description;
        this.emojiUrl = emojiUrl;
        this.emojiAlt = emojiAlt;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getEmojiUrl() {
        return emojiUrl;
    }

    public String getEmojiAlt() {
        return emojiAlt;
    }
}
