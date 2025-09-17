package com.docmanager.controller;

import com.docmanager.model.base.PageResponse;
import com.docmanager.model.dto.FolderQueryReqDTO;
import com.docmanager.model.dto.FolderReorderDTO;
import com.docmanager.model.dto.FolderReorderParentDTO;
import com.docmanager.model.dto.FolderUpsertReqDTO;
import com.docmanager.model.entity.Folder;
import com.docmanager.model.vo.FolderTreeVO;
import com.docmanager.model.vo.FolderVO;
import com.docmanager.service.folder.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public FolderVO createFolder(@Valid @RequestBody FolderUpsertReqDTO folderUpsertReqDTO) {
        return folderService.createFolder(folderUpsertReqDTO);
    }

    @PutMapping("/update")
    public FolderVO updateFolder(@Valid @RequestBody FolderUpsertReqDTO folderUpsertReqDTO) {
        return folderService.updateFolder(folderUpsertReqDTO);
    }

    @PutMapping("/reorder")
    public FolderTreeVO reorderFolders(@RequestBody FolderReorderDTO reorderDTO) {
      return folderService.updateFolder(reorderDTO);
    }

    @PutMapping("/reorderParent")
    public List<FolderTreeVO> reorderFoldersParent(@RequestBody FolderReorderParentDTO reorderDTO) {
      return folderService.updateFolder(reorderDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteFolder(@PathVariable Long id) {
        folderService.deleteFolder(id);
    }
}
