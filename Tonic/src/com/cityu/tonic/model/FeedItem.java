package com.cityu.tonic.model;

public class FeedItem {
	
	public String user_pic_url;
	public String username;
	public String datetime;
	public String audio_url;
	public String num_like;
	
	public FeedItem(String user_pic_url, String username, String datetime, String audio_url, String num_like){
		this.user_pic_url = user_pic_url;
		this.username = username;
		this.datetime = datetime;
		this.audio_url = audio_url;
		this.num_like = num_like;
	}
	public FeedItem(){};
}
