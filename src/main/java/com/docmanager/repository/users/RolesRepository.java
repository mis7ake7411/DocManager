package com.docmanager.repository.users;

import com.docmanager.model.entity.Roles;
import com.docmanager.repository.BaseRepository;
import java.util.List;
import java.util.Set;

public interface RolesRepository extends BaseRepository<Roles, Long> {

  List<Roles> findByIdIn(Set<Long> roleIds);
}