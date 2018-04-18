package com.school.DAO;

import com.school.Entity.VersionDTO;

import java.util.List;

public interface IVersionDao {
	List<VersionDTO> isLatestVersion(Long id);
}
