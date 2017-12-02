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
import fs.domain.Flow;
import fs.domain.Result;


public class ServicesContainer {
	public static void main(String[] args){
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
	private static HashMap<String, IFlowService> classContainer = new HashMap<String, IFlowService>();
	//private static HashMap<String, Flow> FlowContainer = new HashMap<String, Flow>();
	private static HashMap<String, List<Flow>> serviceFlowMehodsContainer = new HashMap<String, List<Flow>>();
	private static HashMap<String, Flow> serviceFlowContainer = new HashMap<String, Flow>();
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
		initClassContainer();
		initServiceFlowMehodsContainer();
	}
	
	/**
	 * 初始化流程服务方法容器
	 */
	public void initServiceFlowMehodsContainer(){
		for(String name : serviceFlowContainer.keySet()){
			if(!serviceFlowMehodsContainer.containsKey(name)){
				Flow flow = serviceFlowContainer.get(name);
				List<Flow> list = itorFlowMethod(flow);
				serviceFlowMehodsContainer.put(name, list);
			}
		}
	}
	
	/**
	 * 初始化服务实例容器
	 */
	private void initClassContainer() {
		try {
			IFlowService fs = (IFlowService)Class.forName("service.GetPersonInfoService").newInstance();
			classContainer.put("GetPersonInfoService", fs);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
	private List<Flow> itorFlowMethod(Flow flow) {
		List<Flow> result = new ArrayList<Flow>();
		String type = flow.getType();
		String name = flow.getName();
		if(type.equals("method")){
			//System.out.println("method:" + flow.getName());
			result.add(flow);
		}else if(type.equals("service")){
			ArrayList<Flow> subFlows = flow.getSubFlows();
			for(Flow subFlow : subFlows){
				List<Flow> subMethodFlows = itorFlowMethod(subFlow);
				result.addAll(subMethodFlows);
			}
		}else if(type.equals("mapping")){
			//System.out.println("method-mapping:" + flow.getMappingId());
			List<Flow> list;
			if(serviceFlowMehodsContainer.containsKey(name)){
				list = serviceFlowMehodsContainer.get(name);
			}else{
				Flow mappingFlow = serviceFlowContainer.get(name);
				list = itorFlowMethod(mappingFlow);
				serviceFlowMehodsContainer.put(name, list);
			}
			result.addAll(list);
		}
		return result;
	}

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
				flow.addSubFlow(subFlow);
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
		}
		return flow;
	}

	private File getConfigFile() {
		String projectPath = System.getProperty("user.dir");
		String configDir = projectPath + "\\src\\Flow_Service_Config.xml";
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

	public static void callService(String serviceName) {
		
	}

	public List<Flow> getService(String serviceName) {
		if(serviceFlowMehodsContainer.containsKey(serviceName)){
			return serviceFlowMehodsContainer.get(serviceName);
		}else{
			Flow flow = serviceFlowContainer.get(serviceName);
			if(flow != null){
				List<Flow> methodFlows = itorFlowMethod(flow);
				return methodFlows;
			}else{
				System.out.println("服务容器无此服务:" + serviceName);
				return null;
			}
		}
	}

	public static Result callService(String serviceName, HashMap paramMap, Result result) {
		try {
			List<Flow> list = serviceFlowMehodsContainer.get(serviceName);
			for(Flow flow : list){
				String flowMthod = flow.getMethod();
				String classId = flow.getClassId();
				System.out.println("正在执行:" + serviceName + "\t" + flowMthod);
				IFlowService fs = classContainer.get(classId);
				Method method1 = fs.getClass().getDeclaredMethod(flowMthod, HashMap.class, Result.class);
				method1.invoke(fs, paramMap, result);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
