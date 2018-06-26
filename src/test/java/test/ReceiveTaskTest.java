package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ReceiveTaskTest {
	// 流程引擎对象
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/** 部署流程定义+启动流程实例 
	 * @throws FileNotFoundException */
	@Test
	public void deployementAndStartProcess() throws FileNotFoundException {
		
		//获取项目路径
		String path = System.getProperty("user.dir")+"/src/main/resources/";
		
		//获取资源绝对路径  
	    String bpmnPath = path+"diagrams/receiveTask.bpmn";  
	    String pngPath = path+"diagrams/receiveTask.png";  
	      
	    //读取资源作为一个输入流  
	    FileInputStream inputStreamBpmn = new FileInputStream(bpmnPath);
	    FileInputStream inputStreamPng = new FileInputStream(pngPath); 
	    
		// 1.部署流程定义
		Deployment deployment = processEngine.getRepositoryService().createDeployment()// 创建部署对象
				.addInputStream("receiveTask.bpmn", inputStreamBpmn)
				.addInputStream("receiveTask.png", inputStreamPng).deploy();
		System.out.println("部署ID：" + deployment.getId());

		// 2.启动流程实例
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("receiveTask");// 使用流程定义的key的最新版本启动流程
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID：" + pi.getProcessDefinitionId());

		// 3.查询执行对象表,使用流程实例ID和当前活动的名称（receivetask1）
		String processInstanceId = pi.getId();// 得到流程实例ID
		Execution execution1 = processEngine.getRuntimeService().createExecutionQuery()
				.processInstanceId(processInstanceId)// 流程实例ID
				.activityId("receivetask1")// 当前活动的名称
				.singleResult();

		// 4.使用流程变量设置当日的销售额
		processEngine.getRuntimeService().setVariable(execution1.getId(), "当日销售额", 20000);

		// 5.向后执行一步
		processEngine.getRuntimeService().signal(execution1.getId());

		// 6.查询执行对象表,使用流程实例ID和当前活动的名称（receivetask2）
		Execution execution2 = processEngine.getRuntimeService().createExecutionQuery()
				.processInstanceId(processInstanceId).activityId("receivetask2").singleResult();

		// 7.获取流程变量,给老板发送短信
		Integer value = (Integer) processEngine.getRuntimeService().getVariable(execution2.getId(), "当日销售额");
		System.out.println("给老板发送短信：内容，当日销售额：" + value);

		// 8.向后执行一步
		processEngine.getRuntimeService().signal(execution2.getId());

		// 9.判断流程是否结束
		ProcessInstance nowPi = processEngine.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(pi.getId()).singleResult();
		if (nowPi == null) {
			System.out.println("流程结束");
		}
	}

}
