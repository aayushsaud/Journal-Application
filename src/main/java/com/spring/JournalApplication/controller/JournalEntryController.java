package com.spring.JournalApplication.controller;

import com.spring.JournalApplication.entity.JournalEntry;
import com.spring.JournalApplication.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAll() {
        return journalEntryService.getAllJournalEntries();
    }

    @GetMapping("/id/{id}")
    public JournalEntry getbyID(@PathVariable ObjectId id) {
        return journalEntryService.getJournalEntryById(id).orElse(null);
    }

    @PostMapping
    public String createEntry(@RequestBody JournalEntry entry) {
        entry.setDate(LocalDateTime.now());
        journalEntryService.saveJournalEntry(entry);
        return "Entry Saved Successfully!";
    }

    @DeleteMapping("/id/{id}")
    public String deleteEntry(@PathVariable ObjectId id) {
        journalEntryService.deleteJournalEntryById(id);
        return "Journal Deleted Successfully!";
    }

    @PutMapping("/id/{id}")
    public String updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry) {
        JournalEntry oldEntry = journalEntryService.getJournalEntryById(id).orElse(null);

        if (oldEntry == null) {
            return "Entry not found!";
        }

        if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
            oldEntry.setTitle(newEntry.getTitle());
        }
        if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
            oldEntry.setContent(newEntry.getContent());
        }

        journalEntryService.saveJournalEntry(oldEntry);

        return "Entry Successfully Updated!";
    }
}