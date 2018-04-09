package com.school.DAO;

import com.school.Entity.CityDTO;

import java.util.List;

public interface ISpiderEnumDao {
	List<CityDTO> selectSupportedCities();

}
