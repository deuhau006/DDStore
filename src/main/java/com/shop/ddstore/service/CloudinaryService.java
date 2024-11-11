package com.shop.ddstore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class CloudinaryService {

	Cloudinary cloudinary;
	
	public CloudinaryService() {
		Map<String, String> valueMap = new HashMap<>();
		valueMap.put("cloud_name", "dpyyocz2b");
		valueMap.put("api_key", "136685717912799");
		valueMap.put("api_secret", "tX_-LtAAonBcyUGffg5kJ5Wektk");
		cloudinary = new Cloudinary(valueMap);
	}
	
	public Map upload(MultipartFile multipartFile) throws IOException {
		File file = convert(multipartFile);
		Map result = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "product"));
		if (!Files.deleteIfExists(file.toPath())) {
			throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
		}
		return result;
	}
	
	public Map delete(String cloudinaryImageId) throws IOException {
		return cloudinary.uploader().destroy(cloudinaryImageId, ObjectUtils.emptyMap());
	}
	
	private File convert(MultipartFile multipartFile) throws IOException {
		File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
		FileOutputStream fo = new FileOutputStream(file);
		fo.write(multipartFile.getBytes());
		fo.close();
		return file;
	}
}
