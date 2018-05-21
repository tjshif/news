package com.school.resouce;

import com.school.Constants.EnvConst;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Gson.AvatarResultGson;
import com.school.Utils.AppProperites;
import com.school.Utils.GsonUtil;
import com.school.service.UserService;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.io.FileUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

@Component
@Path("/file")
public class ImageFileResouce {
	Logger logger = Logger.getLogger(ImageFileResouce.class.getName());

	@InjectParam
	private UserService userService;

	@InjectParam
	private AppProperites appProperites;

	@POST
	@Path("/{userID}/uploadfile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadFile(@PathParam("userID") Long userID,
							 @FormDataParam("file")InputStream inputStream,
							 @FormDataParam("file") FormDataContentDisposition disposition)
	{
		if (userID == null || inputStream == null || disposition == null)
		{
			logger.error("uploadFile can't be empty!");
			AvatarResultGson resultGson = new AvatarResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}

		AvatarResultGson resultGson = new AvatarResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		String imagePathName = appProperites.getRoot_folder() + appProperites.getAvatar_image_path() + userID.toString() + "/" + disposition.getFileName();
		File file = new File(imagePathName);
		try {
			logger.info("file Path:" + file.getAbsolutePath());
			FileUtils.copyInputStreamToFile(inputStream, file);
			//changePermission(file);
			String relativePath =  appProperites.getAvatar_image_path() + userID.toString() + "/" + disposition.getFileName();
			resultGson = userService.updateUserAvatarPath(userID, relativePath);
			if (resultGson.getRetCode()!= RetCode.RET_CODE_OK)
			{
				userService.removeFile(relativePath);
			}
		}
		catch (Exception ex)
		{
			logger.error("",ex);
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return GsonUtil.toJson(resultGson);
	}

	private void changePermission(File dirFile) throws IOException {
		Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
		perms.add(PosixFilePermission.OTHERS_EXECUTE);
		perms.add(PosixFilePermission.OTHERS_READ);
		try {
			java.nio.file.Path path = Paths.get(dirFile.getAbsolutePath());
			Files.setPosixFilePermissions(path, perms);
		} catch (Exception ex) {
			logger.error(ex + "\n" + dirFile.getAbsolutePath());
		}
	}
	@GET
	@Path("/downloadimage")
	@Produces("image/*")
	public Response getImage(@QueryParam("imagename") String imageName)
	{
		String imagePathName =  appProperites.getRoot_folder() + imageName;
		File file = new File(imagePathName);
		if (!file.exists())
		{
			throw new WebApplicationException(404);
		}
		String mt = new MimetypesFileTypeMap().getContentType(file);
		return Response.ok(file, mt).header("", "").build();
	}
}
