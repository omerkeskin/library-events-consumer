package com.omerkeskin.libraryeventconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omerkeskin.libraryeventconsumer.entity.LibraryEvent;
import com.omerkeskin.libraryeventconsumer.jpa.LibraryEventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LibraryEventsService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LibraryEventsRepository libraryEventsRepository;

    public void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        LibraryEvent libraryEvent = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
        log.info("libraryEvent is : {} ", libraryEvent);

        switch (libraryEvent.getLibraryEventType()){
            case NEW:
                save(libraryEvent);
                //save operation
                break;
            case UPDATE:
                //validate library event
                validate(libraryEvent);
                save(libraryEvent);
                break;
            default:
                log.info("Invalid libraryEvent type.");
        }
    }

    private void validate(LibraryEvent libraryEvent) {
        if(libraryEvent.getLibraryEventId() == null){
            throw  new IllegalArgumentException("Library Event id is missing");
        }

        Optional<LibraryEvent> optionalLibraryEvent = libraryEventsRepository.findById(libraryEvent.getLibraryEventId());
        if(!optionalLibraryEvent.isPresent()){
            throw new IllegalArgumentException("Not a valid library event");
        }
        log.info("Validation is successfull for the library event {} ", optionalLibraryEvent.get());
    }

    private void save(LibraryEvent libraryEvent) {
        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        libraryEventsRepository.save(libraryEvent);
        log.info("Successfully saved {} ", libraryEvent);
    }


}
