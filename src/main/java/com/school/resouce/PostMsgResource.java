package com.school.resouce;

import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Gson.NewsSubjectResultGson;
import com.school.Gson.RetResultGson;
import com.school.Utils.AppProperites;
import com.school.Utils.FilePathUtils;
import com.school.Utils.GsonUtil;
import com.school.service.PostMsgService;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.io.FileUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("/postmsg")
public class PostMsgResource {
	Logger logger = Logger.getLogger(ImageFileResouce.class.getName());

	@InjectParam
	FilePathUtils filePathUtils;

	@InjectParam
	PostMsgService postMsgService;

	@POST
	@Path("/{userID}/uploadcontentimages")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadContentImages(@PathParam("userID") Long userID,
									  @FormDataParam("dto") String dto,
									  FormDataMultiPart form)
	{
		if (userID == null || TextUtils.isEmpty(dto))
		{
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}

		List<FormDataBodyPart> formDataBodyParts = form.getFields("file");

		List<String> msgImageFiles = new ArrayList<>();
		for (FormDataBodyPart formDataBodyPart : formDataBodyParts)
		{
			if (formDataBodyPart == null)
				continue;

			InputStream is = formDataBodyPart.getValueAs(InputStream.class);
			FormDataContentDisposition detail = formDataBodyPart.getFormDataContentDisposition();
			if (is == null || detail == null)
				continue;

			String fullPath = filePathUtils.getMsgImageFullPath(userID, detail.getFileName());
			File file = new File(fullPath);
			try {
				logger.info("file Path:" + file.getAbsolutePath());
				FileUtils.copyInputStreamToFile(is, file);
				msgImageFiles.add(detail.getFileName());
			}
			catch (IOException ex) {
				logger.error(ex);
			}
		}
		RetResultGson resultGson = postMsgService.postMsg(userID, dto, msgImageFiles);
		if (resultGson.getRetCode() != RetCode.RET_CODE_OK)
		{
			for (String imagePath : msgImageFiles)
			{
				String fullPath = filePathUtils.getMsgImageFullPath(userID, imagePath);
				File file = new File(fullPath);
				try {
					FileUtils.forceDelete(file);
				} catch (IOException ex) {
					ex.printStackTrace();
					logger.error(ex);
				}
			}
		}

		return GsonUtil.toJson(resultGson);
	}

}
