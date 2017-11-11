package com.example.linukey.data.model.remote;

import com.example.linukey.data.model.local.Sight;

import java.io.Serializable;

public class WebSight extends WebTaskClassify  implements Serializable {

	public WebSight(Sight sight){
		setTitle(sight.getTitle());
		setContent(sight.getContent());
		setUserId(sight.getUserId());
		setSelfId(sight.getSelfId());
		setIsdelete(sight.getIsdelete());
	}

	public WebSight() {}
	
}
