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
	// �����������
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/** �������̶���+��������ʵ�� 
	 * @throws FileNotFoundException */
	@Test
	public void deployementAndStartProcess() throws FileNotFoundException {
		
		//��ȡ��Ŀ·��
		String path = System.getProperty("user.dir")+"/src/main/resources/";
		
		//��ȡ��Դ����·��  
	    String bpmnPath = path+"diagrams/receiveTask.bpmn";  
	    String pngPath = path+"diagrams/receiveTask.png";  
	      
	    //��ȡ��Դ��Ϊһ��������  
	    FileInputStream inputStreamBpmn = new FileInputStream(bpmnPath);
	    FileInputStream inputStreamPng = new FileInputStream(pngPath); 
	    
		// 1.�������̶���
		Deployment deployment = processEngine.getRepositoryService().createDeployment()// �����������
				.addInputStream("receiveTask.bpmn", inputStreamBpmn)
				.addInputStream("receiveTask.png", inputStreamPng).deploy();
		System.out.println("����ID��" + deployment.getId());

		// 2.��������ʵ��
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("receiveTask");// ʹ�����̶����key�����°汾��������
		System.out.println("����ʵ��ID:" + pi.getId());
		System.out.println("���̶���ID��" + pi.getProcessDefinitionId());

		// 3.��ѯִ�ж����,ʹ������ʵ��ID�͵�ǰ������ƣ�receivetask1��
		String processInstanceId = pi.getId();// �õ�����ʵ��ID
		Execution execution1 = processEngine.getRuntimeService().createExecutionQuery()
				.processInstanceId(processInstanceId)// ����ʵ��ID
				.activityId("receivetask1")// ��ǰ�������
				.singleResult();

		// 4.ʹ�����̱������õ��յ����۶�
		processEngine.getRuntimeService().setVariable(execution1.getId(), "�������۶�", 20000);

		// 5.���ִ��һ��
		processEngine.getRuntimeService().signal(execution1.getId());

		// 6.��ѯִ�ж����,ʹ������ʵ��ID�͵�ǰ������ƣ�receivetask2��
		Execution execution2 = processEngine.getRuntimeService().createExecutionQuery()
				.processInstanceId(processInstanceId).activityId("receivetask2").singleResult();

		// 7.��ȡ���̱���,���ϰ巢�Ͷ���
		Integer value = (Integer) processEngine.getRuntimeService().getVariable(execution2.getId(), "�������۶�");
		System.out.println("���ϰ巢�Ͷ��ţ����ݣ��������۶" + value);

		// 8.���ִ��һ��
		processEngine.getRuntimeService().signal(execution2.getId());

		// 9.�ж������Ƿ����
		ProcessInstance nowPi = processEngine.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(pi.getId()).singleResult();
		if (nowPi == null) {
			System.out.println("���̽���");
		}
	}

}
