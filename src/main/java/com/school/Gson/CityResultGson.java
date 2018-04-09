package com.school.Gson;

import com.school.Entity.CityDTO;

import java.util.List;

public class CityResultGson extends RetResultGson {
	private List<CityDTO> cityDTOS;

	public CityResultGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public List<CityDTO> getCityDTOS() {
		return cityDTOS;
	}

	public void setCityDTOS(List<CityDTO> cityDTOS) {
		this.cityDTOS = cityDTOS;
	}
}
