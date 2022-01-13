package com.clevercollege.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.CheckInCheckOut;
import com.clevercollege.model.Location;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CheckInCheckOutController {

	private static final String readQRCodeUrl = "http://api.qrserver.com/v1/read-qr-code/";

	@PostMapping("/process-qrcode")
	public String processQRCode(HttpServletRequest request, String imageDataURL) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || imageDataURL == null) {
			return null;
		}
		// convert image from base64 to binary, then from binary to jpg
		String base64Image = imageDataURL.split(",")[1];
		byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);
		File outputImage = null;
		BufferedImage bufferedImage;

		try {
			bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
			if (bufferedImage == null) {
				return null;
			}
			outputImage = new File("image.jpg");
			ImageIO.write(bufferedImage, "jpg", outputImage);

			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(readQRCodeUrl);

			// content del post deve essere multipart/form-data
			MultipartEntityBuilder Entitybuilder = MultipartEntityBuilder.create();
			Entitybuilder.addBinaryBody("file", outputImage, ContentType.IMAGE_JPEG, "image.jpg");

			HttpEntity multipart = Entitybuilder.build();
			httpPost.setEntity(multipart);

			CloseableHttpResponse response = client.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {

				ObjectMapper mapper = new ObjectMapper();
				String responseAsString = EntityUtils.toString(responseEntity);

				String data = responseAsString.substring(responseAsString.indexOf("data") + 7,
						responseAsString.indexOf("}\"") + 1);
				data = data.replace("\\", "");

				Location checkInLocation = mapper.readValue(data, Location.class);
				Long id = DatabaseManager.getInstance().getIdBroker().getNextCheckInCheckOutId();
				String inTime = LocalTime.now().toString().substring(0, 8);
				String date = LocalDate.now().toString();

				CheckInCheckOut activeCheckIn = DatabaseManager.getInstance().getCheckInCheckOutDao()
						.findActiveByUser(user.getCf());
				if (activeCheckIn != null) {
					activeCheckIn.setOutTime(LocalDateTime.now().toString());
					DatabaseManager.getInstance().getCheckInCheckOutDao().saveOrUpdate(activeCheckIn);
				}

				CheckInCheckOut checkIn = new CheckInCheckOut(id, inTime, null, date, user, checkInLocation);
				DatabaseManager.getInstance().getCheckInCheckOutDao().saveOrUpdate(checkIn);
				DatabaseManager.getInstance().commit();
				outputImage.delete();
				if (checkIn != null)
					return "found";

			}
			client.close();
			response.close();

		} catch (Exception e) {
			if (outputImage != null)
				outputImage.delete();
		}
		return null;
	}

	@PostMapping("check-in-by-id")
	public void checkInById(HttpServletRequest request, Long locationId) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || locationId == null)
			return;

		try {

			Location checkInLocation = DatabaseManager.getInstance().getLocationDao()
					.findByPrimaryKey(locationId);
			if(checkInLocation == null)
				return;
			
			CheckInCheckOut activeCheckIn = DatabaseManager.getInstance().getCheckInCheckOutDao()
					.findActiveByUser(user.getCf());
			if (activeCheckIn != null) {
				activeCheckIn.setOutTime(LocalDateTime.now().toString());
				DatabaseManager.getInstance().getCheckInCheckOutDao().saveOrUpdate(activeCheckIn);
			}
			
			Long id = DatabaseManager.getInstance().getIdBroker().getNextCheckInCheckOutId();
			String inTime = LocalTime.now().toString().substring(0, 8);
			String date = LocalDate.now().toString();
			
			CheckInCheckOut checkIn = new CheckInCheckOut(id, inTime, null, date, user, checkInLocation);
			DatabaseManager.getInstance().getCheckInCheckOutDao().saveOrUpdate(checkIn);
			
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
