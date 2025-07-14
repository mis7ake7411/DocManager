package com.docmanager.repository.folderManager;

import com.docmanager.model.entity.Folder;
import com.docmanager.repository.BaseRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FolderRepository extends BaseRepository<Folder, Long> {
  /**
   * 查父層級資料夾
   * @return 父層級資料夾列表
   */
  Set<Folder> findByParentIdIsNullAndDeleteFlagFalseOrderBySortOrderAsc();

  /**
   * 查子層級資料夾
   * @param parents 父層級資料夾ID列表
   * @return 子層級資料夾列表
   */
  Set<Folder> findByParentIdInAndDeleteFlagFalseOrderBySortOrderAsc(Collection<Long> parents);

  /**
   * 查子層級資料夾
   * @param parent_id 父層級資料夾ID
   * @return 子層級資料夾列表
   */
  List<Folder> findByParentIdAndDeleteFlagFalseOrderBySortOrderAsc(Long parent_id);

  boolean existsByFolderNameAndParentIdAndDeleteFlagFalse(String folderName, Long aLong);

  Folder findByParentIdAndDeleteFlagFalse(Long aLong);

  Optional<Folder> findByIdAndDeleteFlagFalse(Long aLong);
}