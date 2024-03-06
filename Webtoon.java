package com.java.bean;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

public class Webtoon extends Episode{
	int id;
	private String webtoonTitle;
	private String webtoonDescription;
	private String genre;
	private String creatorName;
	private String outputImage;
	private MultipartFile inputImage;
	private Date releaseDate;
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date date) {
		this.releaseDate = date;
	}
	public String getOutputImage() {
		return outputImage;
	}
	public void setOutputImage(String outputImage) {
		this.outputImage = outputImage;
	}
	public MultipartFile getInputImage() {
		return inputImage;
	}
	public void setInputImage(MultipartFile inputImage) {
		this.inputImage = inputImage;
	}	
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWebtoonTitle() {
		return webtoonTitle;
	}
	public void setWebtoonTitle(String webtoonTitle) {
		this.webtoonTitle = webtoonTitle;
	}
	public String getWebtoonDescription() {
		return webtoonDescription;
	}
	public void setWebtoonDescription(String webtoonDescription) {
		this.webtoonDescription = webtoonDescription;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
}
