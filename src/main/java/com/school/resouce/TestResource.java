package com.school.resouce;

import com.school.DAO.IBookDao;
import com.sun.jersey.api.core.InjectParam;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("/english")
public class TestResource
{
	@InjectParam
	private IBookDao bookDao;

	Logger logger = Logger.getLogger(TestResource.class.getName());
	@GET
	@Path("/getbooks")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String getbooks()
	{

		return  "abc";
	}
}
