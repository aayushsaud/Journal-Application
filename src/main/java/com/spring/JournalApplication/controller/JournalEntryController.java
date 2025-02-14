package com.spring.JournalApplication.controller;

import com.spring.JournalApplication.entity.JournalEntry;
import com.spring.JournalApplication.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllEntries() {
        List<JournalEntry> allJournalEntries = journalEntryService.getAllJournalEntries();
        if (allJournalEntries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allJournalEntries, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getJournalByID(@PathVariable ObjectId id) {
        Optional<JournalEntry> journalEntry = journalEntryService.getJournalEntryById(id);
        if (journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry) {
        try {
            journalEntryService.saveJournalEntry(journalEntry);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId id) {
        Optional<JournalEntry> journalEntry = journalEntryService.getJournalEntryById(id);
        if (journalEntry.isPresent()) {
            journalEntryService.deleteJournalEntryById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry) {
        JournalEntry oldEntry = journalEntryService.getJournalEntryById(id).orElse(null);

        if (oldEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
            oldEntry.setTitle(newEntry.getTitle());
        }
        if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
            oldEntry.setContent(newEntry.getContent());
        }

        journalEntryService.saveJournalEntry(oldEntry);
        return new ResponseEntity<>(oldEntry, HttpStatus.OK);
    }
}