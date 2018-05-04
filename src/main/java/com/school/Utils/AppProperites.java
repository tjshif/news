package com.school.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperites {
	@Value("${AVATAR_IMAGE_PATH}")
	private String avatar_image_path;

	@Value("${ROOT_FOLDER}")
	private String root_folder;

	public String getAvatar_image_path() {
		return avatar_image_path;
	}

	public String getRoot_folder() {
		return root_folder;
	}
}
