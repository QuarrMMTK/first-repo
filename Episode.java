package com.java.bean;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

public class Episode {
	private int id;
	private String episodeTitle;
	private int episodeNumber;
	private String file;
	private int totalImages;
	private int webtoon_id;
	public int getWebtoon_id() {
		return webtoon_id;
	}
	public void setWebtoon_id(int webtoon_id) {
		this.webtoon_id = webtoon_id;
	}
	private Date date;// Store image files as a list of file paths or byte arrays

	 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEpisodeTitle() {
		return episodeTitle;
	}
	public void setEpisodeTitle(String episodeTitle) {
		this.episodeTitle = episodeTitle;
	}
	public int getEpisodeNumber() {
		return episodeNumber;
	}
	public void setEpisodeNumber(int episodeNumber) {
		this.episodeNumber = episodeNumber;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getTotalImages() {
		return totalImages;
	}
	public void setTotalImages(int totalImages) {
		this.totalImages = totalImages;
	}

	
	
}
