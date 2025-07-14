package com.docmanager.controller;

import com.docmanager.model.base.PageResponse;
import com.docmanager.model.entity.Document;
import com.docmanager.model.vo.DocumentVO;
import com.docmanager.repository.folderManager.DocumentRepository;
import com.docmanager.service.document.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/document")
@Validated
public class DocumentController {
    private final DocumentRepository documentRepository;
    private final DocumentService documentService;

    @GetMapping("/all")
    public PageResponse<DocumentVO> getAllDocuments(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);
        return documentService.getAllDocuments(pageable);
    }

    @GetMapping("/{id}")
    public Document getDocument(@PathVariable Long id) {
        return documentRepository.findById(id).orElse(null);
    }

    @PostMapping("/create")
    public Document createDocument(@Valid @RequestBody Document document) {
        return documentRepository.save(document);
    }

    @PutMapping("/update")
    public Document updateDocument(@Valid @RequestBody Document document) {
        return documentRepository.save(document);
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Long id) {
        documentRepository.deleteById(id);
    }
}

