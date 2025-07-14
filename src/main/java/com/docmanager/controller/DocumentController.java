package com.docmanager.controller;

import com.docmanager.model.entity.Document;
import com.docmanager.repository.folderManager.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/document")
public class DocumentController {
    private final DocumentRepository documentRepository;

    @GetMapping
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Document getDocument(@PathVariable Long id) {
        return documentRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Document createDocument(@RequestBody Document document) {
        return documentRepository.save(document);
    }

    @PutMapping("/{id}")
    public Document updateDocument(@PathVariable Long id, @RequestBody Document document) {
        document.setId(id);
        return documentRepository.save(document);
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Long id) {
        documentRepository.deleteById(id);
    }
}

