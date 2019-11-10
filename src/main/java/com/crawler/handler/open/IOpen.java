package com.crawler.handler.open;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Storage;

public interface IOpen {
	
	void init(OpenUnit openUnit,Storage storage,DriverEntity driverEntity) throws Exception;
	
}
