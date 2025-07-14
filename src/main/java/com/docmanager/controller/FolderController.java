package com.docmanager.controller;

import com.docmanager.model.base.PageResponse;
import com.docmanager.model.dto.FolderQueryReqDTO;
import com.docmanager.model.dto.FolderReqDTO;
import com.docmanager.model.entity.Folder;
import com.docmanager.model.vo.FolderTreeVO;
import com.docmanager.model.vo.FolderVO;
import com.docmanager.service.folder.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folder")
@Validated
public class FolderController {
    private final FolderService folderService;

    @GetMapping("/folderTree")
    public List<FolderTreeVO> getAllFolders() {
        return folderService.getFolderTree();
    }

    @GetMapping("/{id}")
    public Folder getFolder(@PathVariable Long id) {
        return folderService.findById(id).orElse(null);
    }

    @PostMapping("/query")
    public PageResponse<FolderVO> queryFolders(@RequestBody FolderQueryReqDTO folderReqDTO) {
        return folderService.queryFolders(folderReqDTO);
    }

    @PostMapping("/create")
    public FolderVO createFolder(@Valid @RequestBody FolderReqDTO folderReqDTO) {
        return folderService.createFolder(folderReqDTO);
    }

    @PutMapping("/update")
    public FolderVO updateFolder(@Valid @RequestBody FolderReqDTO folderReqDTO) {
        return folderService.updateFolder(folderReqDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteFolder(@PathVariable Long id) {
        folderService.deleteFolder(id);
    }
}
