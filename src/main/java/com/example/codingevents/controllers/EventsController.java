package com.example.codingevents.controllers;

import com.example.codingevents.data.EventCategoryRepository;
import com.example.codingevents.data.EventRepository;
import com.example.codingevents.data.TagRepository;
import com.example.codingevents.models.Event;
import com.example.codingevents.models.EventCategory;
import com.example.codingevents.models.Tag;
import com.example.codingevents.models.dto.EventTagDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping("events")
public class EventsController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public String displayAllEvents(@RequestParam(required = false) Integer categoryId, Model model){
        if(categoryId == null) {
            model.addAttribute("title", "All Events");
            model.addAttribute("events", eventRepository.findAll());
        } else {
            Optional<EventCategory> result = eventCategoryRepository.findById(categoryId);
            if (result.isEmpty()){
                model.addAttribute("title", "Invalid Category ID: " + categoryId);
            } else {
                EventCategory eventCategory = result.get();
                model.addAttribute("title", "Events in category: " + eventCategory.getName());
                model.addAttribute("events", eventCategory.getEvents());
            }

        }
        return "events/index";
    }

    @GetMapping("create")
    public String renderCreateEventForm(Model model){
        model.addAttribute("title", "Create event");
        model.addAttribute(new Event());
        model.addAttribute("categories", eventCategoryRepository.findAll());
        return "events/create";
    }

    @PostMapping("create")
    public String createEvent(@ModelAttribute @Valid Event newEvent, Errors errors, Model model){
        if(errors.hasErrors()){
            model.addAttribute("title", "Create event");
            return "events/create";
        }
        eventRepository.save(newEvent);
        return "redirect:/events";
    }

    @GetMapping("details")
    public String displayEventDetails(@RequestParam Integer eventId, Model model){
        Optional<Event> result = eventRepository.findById(eventId);
        if(result.isEmpty()){
            model.addAttribute("title", "Event doesn't exist in the database");
        } else {
            Event event = result.get();
            model.addAttribute("title", event.getName());
            model.addAttribute("event", event);
            model.addAttribute("tags", event.getTags());
        }
        return "events/details";
    }

    @GetMapping("delete")
    public String displayDeleteEventForm(Model model){
        model.addAttribute("title", "Delete Events");
        model.addAttribute("events", eventRepository.findAll());
        return "events/delete";
    }

    @PostMapping("delete")
    public String deleteEvents(@RequestParam(required = false) int[] eventIds){
        if(eventIds != null) {
            for (int id : eventIds) {
                eventRepository.deleteById(id);
            }
        }
        return "redirect:/events";
    }

    @GetMapping("add-tag")
    public String displayAddTagForm(@RequestParam Integer eventId, Model model){
        Optional<Event> result = eventRepository.findById(eventId);
        Event event = result.get();
        model.addAttribute("title", "Add tag to: " + event.getName());
        model.addAttribute("tags", tagRepository.findAll());
        EventTagDTO eventTag = new EventTagDTO();
        eventTag.setEvent(event);
        model.addAttribute("eventTag", eventTag);
        return "events/add-tag";
    }

    @PostMapping("add-tag")
    public String processAddTagForm(@ModelAttribute @Valid EventTagDTO eventTag, Model model, Errors errors){
        if(!errors.hasErrors()){
            Event event = eventTag.getEvent();
            Tag tag = eventTag.getTag();
            if(!event.getTags().contains(tag)){
                event.getTags().add(tag);
                eventRepository.save(event); //it will update it
            }
            return "redirect:/events/details?eventId=" + event.getId();
        }
        return "redirect:/events/add-tag";
    }
}
