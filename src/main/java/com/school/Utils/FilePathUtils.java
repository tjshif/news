package com.school.Utils;

import com.sun.jersey.api.core.InjectParam;
import org.springframework.stereotype.Component;

@Component
public class FilePathUtils {
	@InjectParam
	private AppProperites appProperites;

	public String getMsgImageFullPath(Long userID, String fileName)
	{
		String imageFolder = getMsgImageFolder(userID);
		return imageFolder + fileName;
	}

	public String getMsgImageFolder(Long userID)
	{
		return appProperites.getRoot_folder() + appProperites.getMsg_image_path() + userID.toString() + "/";
	}

}
