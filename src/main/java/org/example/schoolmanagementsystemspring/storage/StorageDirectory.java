package org.example.schoolmanagementsystemspring.storage;

import org.springframework.beans.factory.annotation.Value;

public enum StorageDirectory {
    PROFILE_IMAGE,
    TEXTBOOK_COVER
    ;

    @Value("${storage.profile-directory}")
    private String profileDirectory;

    @Value("${storage.book-covers-directory}")
    private String bookCoversDirectory;

    public String getDirectory() {
        return switch (this) {
            case PROFILE_IMAGE -> profileDirectory;
            case TEXTBOOK_COVER -> bookCoversDirectory;
        };
    }

}
