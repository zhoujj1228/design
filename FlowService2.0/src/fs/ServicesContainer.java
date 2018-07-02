package fs;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import fs._interface.IFlowService;
import fs.domain.FSConstant;
import fs.domain.Flow;
import fs.domain.FlowType;
import fs.domain.Result;


@SuppressWarnings({"rawtypes","unchecked"})
public class ServicesContainer {
	public void main(String[] args) throws InvocationTargetException{
		Result<StringBuffer> result = new Result<StringBuffer>();
		HashMap paramMap = new HashMap();
		paramMap.put("name", "Jay");
		Result<StringBuffer> realResult = ServicesContainer.getInstance().callService("getPersonInfo", paramMap, result);
		System.out.println("c-getPersonInfo" +  realResult.get().toString());
		
		/*ServicesContainer sc = new ServicesContainer();
		for(String name : serviceFlowMehodsContainer.keySet()){
			List<Flow> service = serviceFlowMehodsContainer.get(name);
			System.out.println("--" + name);
			for(Flow flow : service){
				System.out.println(flow.getName());
			}
		}*/
		/*List<Flow> service = sc.getService("getPersonInfo");
		for(Flow flow : service){
			System.out.println(flow.getName());
		}*/
	}
	
	private static ServicesContainer instance;
	private HashMap<String, IFlowService> classContainer = new HashMap<String, IFlowService>();
	//private HashMap<String, Flow> FlowContainer = new HashMap<String, Flow>();
	private HashMap<String, Flow> serviceFlowContainer = new HashMap<String, Flow>();
	private static Object lockObj = new Object();
	public static ServicesContainer getInstance(){
		if(instance == null){
			synchronized(lockObj){
				if(instance == null){
					instance = new ServicesContainer();
				}
			}
		}
		return instance;
	}
	
	private ServicesContainer(){
		initServicesContainer();
	}
	
	
	/**
	 * 初始化流程服务容器
	 */
	private void initServicesContainer() {
		File configFile = getConfigFile();
		if(configFile == null){
			return;
		}
		try {
			loadConfigFile(configFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 加载配置文件
	 * @param configFile
	 * @throws DocumentException 
	 */
	private void loadConfigFile(File configFile) throws DocumentException {
		SAXReader sd = new SAXReader();
		Document doc = sd.read(configFile);
		Element rootElement = doc.getRootElement();
		List<Element> elements = rootElement.elements();
		for(Element ele : elements){
			String eleName = ele.getName();
			if(eleName.equals("service")){
				List<Element> flowEles = ele.elements();
				for(Element flowEle : flowEles){
					itorLoadFlowElement(flowEle);
				}
			}else if(eleName.equals("service-classes")){
				List<Element> classEles = ele.elements();
				for(Element classEle : classEles){
					LoadClassElement(classEle);
				}
			}else if(eleName.equals("ext-config-file")){
				String extConfigFilePath = ele.attributeValue("path");
				String parentPath = configFile.getParent();
				String extConfigFileFullPath = parentPath + File.separator + extConfigFilePath;
				if(extConfigFilePath.startsWith("\\") || extConfigFilePath.startsWith("/") || extConfigFilePath.indexOf(":") > -1){
					extConfigFileFullPath = extConfigFilePath;
				}
				File extFile = new File(extConfigFileFullPath);
				if(extFile.exists()) {
					loadConfigFile(extFile);
				}else {
					System.out.println("无法加载配置文件，该配置文件不存在:" + extConfigFileFullPath);
				}
			}else{
				System.out.println("该节点不在FlowService配置范围：" + eleName);
			}
		}
		
	}

	/**
	 * 得到Flow里所有的MethodFlow
	 * @param flow
	 * @return
	 */

	/**
	 * 加载服务实例配置
	 * @param ele
	 */
	private void LoadClassElement(Element ele) {
		String classId = ele.attributeValue("id");
		String classPath = ele.attributeValue("class");
		try {
			IFlowService fs = (IFlowService)Class.forName(classPath).newInstance();
			classContainer.put(classId, fs);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("加载完成:" + ele.attributeValue("id"));
	}

	/**
	 * 加载Flow配置
	 * @param flowElement
	 */
	private Flow itorLoadFlowElement(Element flowElement) {
		String name = flowElement.attributeValue("name");
		String type = flowElement.attributeValue("type");
		if(name == null){
			System.out.println("flow配置中name属性不能为空，跳过当前标签加载:" + flowElement);
			return null;
		}
		if(type == null){
			System.out.println("flow配置中type属性不能为空，跳过当前标签加载:" + name);
			return null;
		}
		//System.out.println(name);
		Flow flow = new Flow();
		flow.setName(name);
		flow.setType(type);
		if(type.equals("service")){
			List<Element> elements = flowElement.elements();
			for(Element ele : elements){
				Flow subFlow = itorLoadFlowElement(ele);
				if(subFlow != null){
					flow.addSubFlow(subFlow);
				}
			}
			/*List<Flow> methodFlows = itorFlowMethod(flow);
			flowMehodsContainer.put(name, methodFlows);*/
			serviceFlowContainer.put(name, flow);
		}else if(type.equals("method")){
			String classId = flowElement.attributeValue("classId");
			String method = flowElement.attributeValue("method");
			if(classId == null){
				System.out.println("flow配置中classId属性不能为空，跳过当前标签加载:"+name);
				return null;
			}
			flow.setClassId(classId);
			flow.setMethod(method);
		}else if(type.equals("mapping")){
			String mappingId = flowElement.attributeValue("mappingId");
			if(mappingId == null){
				System.out.println("flow配置中mappingId属性不能为空，跳过当前标签加载:"+name);
				return null;
			}
			flow.setMappingId(mappingId);
		}else if(type.equals("if_struct")){
			loadIfStructElement(flowElement, flow);
		}else if(type.equals("if")){
			boolean isSuccess = loadIfFlowElemet(flowElement, flow);
			if(!isSuccess){
				return null;
			}
		}else if(type.equals("for")){
			boolean isSuccess = loadForFlowElemet(flowElement, flow);
			if(!isSuccess){
				return null;
			}
		}
		return flow;
	}

	private void loadIfStructElement(Element flowElement, Flow flow) {
		List<Element> elements = flowElement.elements();
		for(Element ele : elements){
			Flow subFlow = itorLoadFlowElement(ele);
			if(subFlow != null){
				subFlow.setSupFlow(flow);
				if(subFlow.getType().equals("if")){
					flow.addIfFlow(subFlow);
				}else if(subFlow.getType().equals("else")){
					if(flow.getElseFlow() == null){
						flow.setElseFlow(subFlow);
					}else{
						System.out.println("一个if_struct中不能有多个else,跳过该标签加载：" + subFlow.getName());
					}
				}
			}
		}}

	private boolean loadForFlowElemet(Element flowElement, Flow flow) {
		String classId = flowElement.attributeValue("classId");
		String condition = flowElement.attributeValue("condition");
		if(classId == null){
			System.out.println("flow配置中classId属性不能为空，跳过当前标签加载:"+flow.getName());
			return false;
		}
		flow.setClassId(classId);
		flow.setCondition(condition);
		List<Element> elements = flowElement.elements();
		for(Element ele : elements){
			Flow subFlow = itorLoadFlowElement(ele);
			if(subFlow != null){
				subFlow.setSupFlow(flow);
				if(subFlow.getType().equals("init")){
					flow.addInitFlow(subFlow);
				}else if(subFlow.getType().equals("loop")){
					flow.addLoopFlow(subFlow);
				}else{
					flow.addSubFlow(subFlow);
				}
			}
		}
		return true;
	
	}

	private boolean loadIfFlowElemet(Element flowElement, Flow flow) {
		String classId = flowElement.attributeValue("classId");
		String condition = flowElement.attributeValue("condition");
		if(classId == null){
			System.out.println("flow配置中classId属性不能为空，跳过当前标签加载:"+flow.getName());
			return false;
		}
		flow.setClassId(classId);
		flow.setCondition(condition);
		List<Element> elements = flowElement.elements();
		for(Element ele : elements){
			Flow subFlow = itorLoadFlowElement(ele);
			if(subFlow != null){
				flow.addSubFlow(subFlow);
			}
		}
		/*List<Flow> methodFlows = itorFlowMethod(flow);
		flowMehodsContainer.put(name, methodFlows);*/
		return true;
	}

	private File getConfigFile() {
		String projectPath = System.getProperty("user.dir");
		String configDir = projectPath + "\\conf\\Flow_Service_Config.xml";
		File configFile = new File(configDir);
		if(!configFile.exists()){
			System.out.println("无法找到对应配置文件:" + configDir);
			return null;
		}
		if(configFile.isDirectory()){
			System.out.println("配置文件不能为目录:" + configDir);
			return null;
		}
		return configFile;
	}



	
	
	
	public Result callService(String serviceName, HashMap paramMap, Result result){
		Flow serviceFlow = serviceFlowContainer.get(serviceName);
		try {
			invokeFlow(serviceFlow, paramMap, result);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void invokeFlow(Flow flow, HashMap paramMap, Result result) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, SecurityException {
		System.out.println("开始执行:" + flow.getType() + "\t" +  flow.getName());
		String status = checkProcess(flow, paramMap, result);
		if(!status.equals(FSConstant.PROCESS)){
			System.out.println("can not process:" + flow.getType() + "\t" +  flow.getName());
			return;
		}
		String type = flow.getType();
		if (type.equals(FlowType.SERVICE)) {
			invokeServiceFlow(flow, paramMap, result);
		} else if (type.equals(FlowType.METHOD)) {
			invokeMethodFlow(flow, paramMap, result);
		} else if (type.equals(FlowType.MAPPING)) {
			invokeMappingFlow(flow, paramMap, result);
		} else if (type.equals(FlowType.IFSTRUCT)) {
			invokeIfStructFlow(flow, paramMap, result);
		} else if (type.equals(FlowType.IF)) {
			invokeIfFlow(flow, paramMap, result);
		} else if (type.equals(FlowType.ELSE)) {
			invokeElseFlow(flow, paramMap, result);
		} else if (type.equals(FlowType.FOR)) {
			invokeForFlow(flow, paramMap, result);
		} else if (type.equals(FlowType.INIT)) {
			invokeMethodFlow(flow, paramMap, result);
		} else if (type.equals(FlowType.LOOP)) {
			invokeMethodFlow(flow, paramMap, result);
		}
			
	
	}

	private void invokeForFlow(Flow flow, HashMap paramMap, Result result) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, SecurityException {
		ArrayList<Flow> initFlows = flow.getInitFlows();
		ArrayList<Flow> subFlows = flow.getSubFlows();
		ArrayList<Flow> loopFlows = flow.getLoopFlows();
		
		invokeFlows(initFlows, paramMap, result);
		boolean isTrue = invokeFlowCondition(flow, paramMap, result);
		loop:while(isTrue){
			for(Flow subFlow : subFlows){
				String status = checkProcess(flow, paramMap, result);
				if(status.equals(FSConstant.CONTINUE)){
					invokeFlows(loopFlows, paramMap, result);
					isTrue = invokeFlowCondition(flow, paramMap, result);
					continue loop;
				}
				if(status.equals(FSConstant.BREAK)){
					break loop;
				}
				invokeFlow(subFlow, paramMap, result);
			}
			invokeFlows(loopFlows, paramMap, result);
			isTrue = invokeFlowCondition(flow, paramMap, result);
		}
	}

	private boolean checkIsContinue(Flow flow, HashMap paramMap, Result result) {
		if(result.getContinueTo() != null){
			if(flow.getName().equals(result.getContinueTo())){
				result.resetContinueTo();
				return true;
			}
		}
		return false;
	}

	private void invokeElseFlow(Flow flow, HashMap paramMap, Result result) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, SecurityException {
		ArrayList<Flow> subFlows = flow.getSubFlows();
		invokeFlows(subFlows, paramMap, result);
	}

	private void invokeFlows(ArrayList<Flow> subFlows, HashMap paramMap, Result result) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, SecurityException {
		for(Flow subFlow : subFlows){
			invokeFlow(subFlow, paramMap, result);
		}
	}

	private String checkProcess(Flow flow, HashMap paramMap, Result result) {
		if(result.getBreakTo() != null){
			if(flow.getName().equals(result.getBreakTo())){
				result.resetBreakTo();
			}
			return "break";
		}
		if(result.getContinueTo() != null){
			if(flow.getName().equals(result.getContinueTo())){
				result.resetContinueTo();
				return "continue";
			}else{
				return "break";
			}
		}
		return "process";
	}

	private void invokeIfFlow(Flow flow, HashMap paramMap, Result result) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		boolean isTrue = invokeFlowCondition(flow, paramMap, result);
		if(isTrue){
			String supFlowName = flow.getSupFlow().getName();
			result.setBreakTo(supFlowName);
			ArrayList<Flow> subFlows = flow.getSubFlows();
			invokeFlows(subFlows, paramMap, result);
		}
	}

	private void invokeIfStructFlow(Flow flow, HashMap paramMap, Result result) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, SecurityException {
		ArrayList<Flow> ifFlows = flow.getIfFlows();
		Flow elseFlow = flow.getElseFlow();
		for(Flow subFlow : ifFlows){
			invokeFlow(subFlow, paramMap, result);
		}
		invokeFlow(elseFlow, paramMap, result);
	}

	private boolean invokeFlowCondition(Flow flow, HashMap paramMap, Result result) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String conditionMthod = flow.getCondition();
		String classId = flow.getClassId();
		IFlowService fs = classContainer.get(classId);
		if(fs == null) {
			System.out.println("服务实例容器无法找到该实例:" + classId);
		}
		Method method = fs.getClass().getDeclaredMethod(conditionMthod, HashMap.class, Result.class);
		boolean flag = (boolean) method.invoke(fs, paramMap, result);
		return flag;
	}

	private void invokeMappingFlow(Flow flow, HashMap paramMap, Result result) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, SecurityException {
		Flow serviceFlow = serviceFlowContainer.get(flow.getMappingId());
		invokeFlow(serviceFlow, paramMap, result);
	}

	private void invokeMethodFlow(Flow flow, HashMap paramMap, Result result) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String flowMthod = flow.getMethod();
		String classId = flow.getClassId();
		IFlowService fs = classContainer.get(classId);
		if(fs == null) {
			System.out.println("服务实例容器无法找到该实例:" + classId);
		}
		Method method = fs.getClass().getDeclaredMethod(flowMthod, HashMap.class, Result.class);
		method.invoke(fs, paramMap, result);
	}

	private void invokeServiceFlow(Flow flow, HashMap paramMap, Result result) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, SecurityException {
		ArrayList<Flow> subFlows = flow.getSubFlows();
		invokeFlows(subFlows, paramMap, result);
	}
	
}
