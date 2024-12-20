package com.example.codingevents.models.dto;

import com.example.codingevents.models.Event;
import com.example.codingevents.models.Tag;
import jakarta.validation.constraints.NotNull;

public class EventTagDTO {
    @NotNull
    private Event event;

    @NotNull
    private Tag tag;

    public EventTagDTO(){

    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
