package com.saveit.bff.notes.web.dto;

import java.util.Objects;

public record TagDto(String tagId, String name, String color, String description) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagDto other)) return false;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}