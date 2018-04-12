package com.school.DAO;

import com.school.Entity.CityDTO;
import com.school.Entity.SpiderEnumDTO;

import java.util.List;

public interface ISpiderEnumDao {
	List<CityDTO> selectSupportedCities();

	List<SpiderEnumDTO> selectEnumValuesByType(String enumType);
}
