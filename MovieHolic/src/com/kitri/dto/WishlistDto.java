package com.kitri.dto;

import java.sql.Date;

public class WishlistDto {	//위시리스트

	private String userId;	//ID
	private int code;	//code
	private Date postDate;	//등록일자
	private String movieCodeNaver;	//영화코드

	
	///// 생성자 /////
	
	public WishlistDto() {
		super();
	}

	
	
	///// getter & setter /////
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public String getMovieCodeNaver() {
		return movieCodeNaver;
	}

	public void setMovieCodeNaver(String movieCodeNaver) {
		this.movieCodeNaver = movieCodeNaver;
	}



	@Override
	public String toString() {
		return "WishlistDto [userId=" + userId + ", code=" + code + ", postDate=" + postDate + ", movieCodeNaver="
				+ movieCodeNaver + "]";
	}

	
	
}