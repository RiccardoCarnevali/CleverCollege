package com.clevercollege.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDate;
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

		BufferedImage bufferedImage;

		try {
			bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
			if (bufferedImage == null) {
				return null;
			}
			File outputImage = new File("image.jpg");
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

				System.out.println("id: " + id + "inTime: " + inTime + "date: " + date);

				CheckInCheckOut checkIn = new CheckInCheckOut(id, inTime, null, date, user, checkInLocation);
				DatabaseManager.getInstance().getCheckInCheckOutDao().saveOrUpdate(checkIn);
				DatabaseManager.getInstance().commit();
				if(checkIn != null)
					return "found";

			}
			client.close();
			response.close();

		} catch (Exception e) {	}
		return null;
	}

}
