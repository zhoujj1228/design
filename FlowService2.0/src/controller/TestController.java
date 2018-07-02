package controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import fs.ServicesContainer;
import fs.domain.Result;
import service.GetPersonInfoService;

public class TestController {

	public static void main(String[] args) {
		TestController tc = new TestController();
		tc.getPersonInfo();
	}
	
	public String getPersonInfo(){
		Result<StringBuffer> result = new Result<StringBuffer>();
		HashMap paramMap = new HashMap();
		paramMap.put("name", "Jay");
		Result<StringBuffer> realResult = null;
		realResult = ServicesContainer.getInstance().callService("getPersonInfo", paramMap, result);
		System.out.println("c-getPersonInfo" +  realResult.get().toString());
		return result.get().toString();
	}
}
