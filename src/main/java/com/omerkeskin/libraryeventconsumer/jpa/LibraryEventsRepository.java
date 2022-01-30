package com.omerkeskin.libraryeventconsumer.jpa;

import com.omerkeskin.libraryeventconsumer.entity.LibraryEvent;
import org.springframework.data.repository.CrudRepository;

public interface LibraryEventsRepository extends CrudRepository<LibraryEvent, Integer> {
}
