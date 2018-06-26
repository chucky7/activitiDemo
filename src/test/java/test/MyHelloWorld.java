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
    
    /**获得流程引擎*/  
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();  
      
    /**部署流程定义*/  
    //@Test  
    public void deployProcessDefinition(){  
          
        Deployment deployment = processEngine.getRepositoryService()//获取流程定义和部署相关的Service  
                .createDeployment()//创建部署对象  
                .addClasspathResource("diagrams/myHelloWorld.bpmn")  
                .addClasspathResource("diagrams/myHelloWorld.png")  
                .deploy();//完成部署  
          
        System.out.println("部署ID：" + deployment.getId());//部署ID:1  
        System.out.println("部署时间：" + deployment.getDeploymentTime());//部署时间  
    }
    
    /**启动流程实例*/  
    @Test  
    public void startProcessInstance(){  
          
        String processDefinitionKey = "myHelloWorld";//流程定义的key,也就是bpmn中存在的ID  
          
        ProcessInstance pi = processEngine.getRuntimeService()//管理流程实例和执行对象，也就是表示正在执行的操作  
                .startProcessInstanceByKey(processDefinitionKey);////按照流程定义的key启动流程实例  
                  
        System.out.println("流程实例ID：" + pi.getId());//流程实例ID：101  
        System.out.println("流程实例ID：" + pi.getProcessInstanceId());//流程实例ID：101  
        System.out.println("流程实例ID:" + pi.getProcessDefinitionId());//myMyHelloWorld:1:4  
    } 
    
    //@Test  
    public void findPersonnelTaskList(){  
        String assignee = "admin";//当前任务办理人  
        List<Task> tasks = processEngine.getTaskService()//与任务相关的Service  
                .createTaskQuery()//创建一个任务查询对象  
                .taskAssignee(assignee)  
                .list();  
        if(tasks !=null && tasks.size()>0){  
            for(Task task:tasks){  
                System.out.println("任务ID:"+task.getId());  
                System.out.println("任务的办理人:"+task.getAssignee());  
                System.out.println("任务名称:"+task.getName());  
                System.out.println("任务的创建时间:"+task.getCreateTime());  
                System.out.println("任务ID:"+task.getId());  
                System.out.println("流程实例ID:"+task.getProcessInstanceId());  
                System.out.println("#####################################");  
            }  
        }  
    } 
    
    // 查询流程定义  
    //@Test  
    public void findProcessDifinitionList() {  
        List<ProcessDefinition> list = processEngine.getRepositoryService()  
                .createProcessDefinitionQuery()  
                // 查询条件  
                .processDefinitionKey("myMyHelloWorld")// 按照流程定义的key  
                // .processDefinitionId("helloworld")//按照流程定义的ID  
                .orderByProcessDefinitionVersion().desc()// 排序  
                // 返回结果  
                // .singleResult()//返回惟一结果集  
                // .count()//返回结果集数量  
                // .listPage(firstResult, maxResults)  
                .list();// 多个结果集  
          
        if(list!=null && list.size()>0){  
            for(ProcessDefinition pd:list){  
                System.out.println("流程定义的ID："+pd.getId());  
                System.out.println("流程定义的名称："+pd.getName());  
                System.out.println("流程定义的Key："+pd.getKey());  
                System.out.println("流程定义的部署ID："+pd.getDeploymentId());  
                System.out.println("流程定义的资源名称："+pd.getResourceName());  
                System.out.println("流程定义的版本："+pd.getVersion());  
                System.out.println("########################################################");  
            }  
        }  
      
    } 
    
    //@Test  
    public void setProcessVariables(){  
        String processInstanceId = "12501";//流程实例ID  
        String assignee = "admin";//任务办理人  
        
        Deployment deployment = processEngine.getRepositoryService()//获取流程定义和部署相关的Service  
                .createDeployment()//创建部署对象  
                .addClasspathResource("diagrams/myHelloWorld.bpmn")  
                .addClasspathResource("diagrams/myHelloWorld.png")  
                .deploy();//完成部署
        
        TaskService taskService = processEngine.getTaskService();//获取任务的Service，设置和获取流程变量  
          
        //查询当前办理人的任务ID  
        Task task = taskService.createTaskQuery()  
                .processInstanceId(deployment.getId())//使用流程实例ID  
                .taskAssignee(assignee)//任务办理人  
                .singleResult();  
          
        //设置流程变量【基本类型】  
        taskService.setVariable(task.getId(), "请假人", assignee);  
        taskService.setVariableLocal(task.getId(), "请假天数",3);  
        taskService.setVariable(task.getId(), "请假日期", new Date());  
          
          
    }  
}  
