package com.example.codingevents.data;

import com.example.codingevents.models.EventCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCategoryRepository extends CrudRepository<EventCategory, Integer> {
}
