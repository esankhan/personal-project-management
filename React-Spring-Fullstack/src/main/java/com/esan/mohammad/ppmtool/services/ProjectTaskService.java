package com.esan.mohammad.ppmtool.services;

import com.esan.mohammad.ppmtool.domain.Backlog;
import com.esan.mohammad.ppmtool.domain.Project;
import com.esan.mohammad.ppmtool.domain.ProjectTask;
import com.esan.mohammad.ppmtool.exceptions.ProjectNotFoundException;
import com.esan.mohammad.ppmtool.repositories.BacklogRepository;
import com.esan.mohammad.ppmtool.repositories.ProjectRepository;
import com.esan.mohammad.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;



    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask,String username){


            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier,username).getBacklog(); //backlogRepository.findByProjectIdentifier((projectIdentifier));
            projectTask.setBacklog(backlog);
            //Project Sequence
            Integer BacklogSequence = backlog.getPTSequence();
            BacklogSequence++;
            backlog.setPTSequence(BacklogSequence);
            //Add to sequence to project task-
            projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            // Setting Priority
            if(projectTask.getPriority()==null){
                projectTask.setPriority(3);
            }
            //Setting Status
            if(projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);

    }



    public Iterable<ProjectTask> findBacklogById(String id,String username){

        projectService.findProjectByIdentifier(id,username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }


    public ProjectTask findProjectTaskByProjectSequence (String backlog_id, String pt_id,String username){

        projectService.findProjectByIdentifier(backlog_id,username);

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask==null){
            throw new ProjectNotFoundException("Project Task "+pt_id+" does not exist");
        }
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task "+pt_id+" does not exist in project: "+backlog_id);
        }

        return  projectTask;
    }


    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id,String pt_id,String username){
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id,pt_id,username);
        projectTask = updatedTask;
        return projectTaskRepository.save(projectTask);
    }


    public void deleteProjectByProjectSequence(String backlog_id,String pt_id,String username){
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id,pt_id,username);
        projectTaskRepository.delete(projectTask);
    }

}
