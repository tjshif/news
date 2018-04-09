package com.school.Entity;

public class CityDTO {
	private String cityName;
	private String cityCode;

	public String getCityName()
	{
		return this.cityName;
	}
	public String getCityCode()
	{
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}
