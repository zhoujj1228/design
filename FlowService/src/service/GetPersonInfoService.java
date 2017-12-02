package service;

import java.util.HashMap;

import fs._interface.IFlowService;
import fs.domain.Result;

public class GetPersonInfoService implements IFlowService{
	public void call1(HashMap paramMap, Result result){
		StringBuffer doResult = new StringBuffer();
		if(result != null){
			System.out.println("result != null:" + result.get());
		}
		doResult.append("1");
		result.set(doResult);
		paramMap.put("doService_call1", result);
		System.out.println("call1:" + result + paramMap);
	}
	
	public void call2(HashMap paramMap, Result result){
		StringBuffer doResult = new StringBuffer();
		if(result != null){
			System.out.println("result != null:" + result.get());
		}
		doResult.append("2");
		result.set(doResult);
		paramMap.put("doService_call2", result);
		System.out.println("call2:" + result + paramMap);
	}
	
	public void call3(HashMap paramMap, Result result){
		StringBuffer doResult = new StringBuffer();
		if(result != null){
			System.out.println("result != null:" + result.get());
		}
		doResult.append("3");
		result.set(doResult);
		paramMap.put("doService_call3", result);
		System.out.println("call3:" + result + paramMap);
	}
}
