package com.clevercollege.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Location;
import com.clevercollege.model.User;
import com.google.gson.Gson;

@RestController
public class CheckInCheckOutController {

	private static final String readQRCodeUrl = "http://api.qrserver.com/v1/read-qr-code/";
	private static final String createQRCodeUrl = "http://api.qrserver.com/v1/create-qr-code/";

	@PostMapping("/process-qrcode")
	public String processQRCode(HttpServletRequest request, String imageDataURL) {
		if (request.getSession().getAttribute("user") == null || imageDataURL == null) {
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
				String responseAsString = EntityUtils.toString(responseEntity);
				return responseAsString;
			}
			client.close();
			response.close();

		} catch (IOException e) {
		}
		return null;
	}

	@PostMapping("create-location-qrcode")
	public void createQRCode(HttpServletRequest request, Location location) {
		User user = (User) request.getSession().getAttribute("user");
		String userType = (String) request.getSession().getAttribute("user_type");
		if (user == null || !("admin").equals(userType))
			return;
		if (location == null)
			return;

		String locationJSON = new Gson().toJson(location);

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(createQRCodeUrl);

		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("data", locationJSON));
		postParameters.add(new BasicNameValuePair("size", "150x150"));
		postParameters.add(new BasicNameValuePair("ecc", "M"));

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
			CloseableHttpResponse response = client.execute(httpPost);
			
			System.out.println(response.getEntity().getContentType());
			System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (IOException e) {
			return;
		}
	}
}
