package test;

import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;



public class MyHelloWorld {  
    
    /**�����������*/  
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();  
      
    /**�������̶���*/  
    //@Test  
    public void deployProcessDefinition(){  
          
        Deployment deployment = processEngine.getRepositoryService()//��ȡ���̶���Ͳ�����ص�Service  
                .createDeployment()//�����������  
                .addClasspathResource("diagrams/myHelloWorld.bpmn")  
                .addClasspathResource("diagrams/myHelloWorld.png")  
                .deploy();//��ɲ���  
          
        System.out.println("����ID��" + deployment.getId());//����ID:1  
        System.out.println("����ʱ�䣺" + deployment.getDeploymentTime());//����ʱ��  
    }
    
    /**��������ʵ��*/  
    @Test  
    public void startProcessInstance(){  
          
        String processDefinitionKey = "myHelloWorld";//���̶����key,Ҳ����bpmn�д��ڵ�ID  
          
        ProcessInstance pi = processEngine.getRuntimeService()//��������ʵ����ִ�ж���Ҳ���Ǳ�ʾ����ִ�еĲ���  
                .startProcessInstanceByKey(processDefinitionKey);////�������̶����key��������ʵ��  
                  
        System.out.println("����ʵ��ID��" + pi.getId());//����ʵ��ID��101  
        System.out.println("����ʵ��ID��" + pi.getProcessInstanceId());//����ʵ��ID��101  
        System.out.println("����ʵ��ID:" + pi.getProcessDefinitionId());//myMyHelloWorld:1:4  
    } 
    
    //@Test  
    public void findPersonnelTaskList(){  
        String assignee = "admin";//��ǰ���������  
        List<Task> tasks = processEngine.getTaskService()//��������ص�Service  
                .createTaskQuery()//����һ�������ѯ����  
                .taskAssignee(assignee)  
                .list();  
        if(tasks !=null && tasks.size()>0){  
            for(Task task:tasks){  
                System.out.println("����ID:"+task.getId());  
                System.out.println("����İ�����:"+task.getAssignee());  
                System.out.println("��������:"+task.getName());  
                System.out.println("����Ĵ���ʱ��:"+task.getCreateTime());  
                System.out.println("����ID:"+task.getId());  
                System.out.println("����ʵ��ID:"+task.getProcessInstanceId());  
                System.out.println("#####################################");  
            }  
        }  
    } 
    
    // ��ѯ���̶���  
    //@Test  
    public void findProcessDifinitionList() {  
        List<ProcessDefinition> list = processEngine.getRepositoryService()  
                .createProcessDefinitionQuery()  
                // ��ѯ����  
                .processDefinitionKey("myMyHelloWorld")// �������̶����key  
                // .processDefinitionId("helloworld")//�������̶����ID  
                .orderByProcessDefinitionVersion().desc()// ����  
                // ���ؽ��  
                // .singleResult()//����Ωһ�����  
                // .count()//���ؽ��������  
                // .listPage(firstResult, maxResults)  
                .list();// ��������  
          
        if(list!=null && list.size()>0){  
            for(ProcessDefinition pd:list){  
                System.out.println("���̶����ID��"+pd.getId());  
                System.out.println("���̶�������ƣ�"+pd.getName());  
                System.out.println("���̶����Key��"+pd.getKey());  
                System.out.println("���̶���Ĳ���ID��"+pd.getDeploymentId());  
                System.out.println("���̶������Դ���ƣ�"+pd.getResourceName());  
                System.out.println("���̶���İ汾��"+pd.getVersion());  
                System.out.println("########################################################");  
            }  
        }  
      
    } 
    
    //@Test  
    public void setProcessVariables(){  
        String processInstanceId = "12501";//����ʵ��ID  
        String assignee = "admin";//���������  
        
        Deployment deployment = processEngine.getRepositoryService()//��ȡ���̶���Ͳ�����ص�Service  
                .createDeployment()//�����������  
                .addClasspathResource("diagrams/myHelloWorld.bpmn")  
                .addClasspathResource("diagrams/myHelloWorld.png")  
                .deploy();//��ɲ���
        
        TaskService taskService = processEngine.getTaskService();//��ȡ�����Service�����úͻ�ȡ���̱���  
          
        //��ѯ��ǰ�����˵�����ID  
        Task task = taskService.createTaskQuery()  
                .processInstanceId(deployment.getId())//ʹ������ʵ��ID  
                .taskAssignee(assignee)//���������  
                .singleResult();  
          
        //�������̱������������͡�  
        taskService.setVariable(task.getId(), "�����", assignee);  
        taskService.setVariableLocal(task.getId(), "�������",3);  
        taskService.setVariable(task.getId(), "�������", new Date());  
          
          
    }  
}  
