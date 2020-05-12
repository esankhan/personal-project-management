package com.esan.mohammad.ppmtool.services;

import com.esan.mohammad.ppmtool.domain.Backlog;
import com.esan.mohammad.ppmtool.domain.Project;
import com.esan.mohammad.ppmtool.domain.User;
import com.esan.mohammad.ppmtool.exceptions.ProjectIdException;
import com.esan.mohammad.ppmtool.exceptions.ProjectNotFoundException;
import com.esan.mohammad.ppmtool.repositories.BacklogRepository;
import com.esan.mohammad.ppmtool.repositories.ProjectRepository;
import com.esan.mohammad.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject (Project project,String username){

        if(project.getId()!=0){
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if(existingProject!=null &&(!existingProject.getProjectLeader().equals(username))){
                throw new ProjectNotFoundException("Project Not Found in your account");
            }else if(existingProject==null){
                throw new ProjectNotFoundException("Project with ID "+project.getProjectIdentifier()+" can not be updated because it does not exist");
            }
        }

        try {
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectIdentifier(user.getUsername());
            project.setProjectIdentifier((project.getProjectIdentifier().toUpperCase()));
            if( project.getId()==0){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            if(project.getId()!=0){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        } catch (Exception ex){
            throw new ProjectIdException("Project ID "+project.getProjectIdentifier().toUpperCase()+" already exist");
        }
    }

    public Project findProjectByIdentifier(String projectId,String username){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project==null){
            throw new ProjectIdException("Project with "+projectId.toUpperCase()+" does not exist");
        }
        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project does not exist in your account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId,String username){
        projectRepository.delete(findProjectByIdentifier(projectId,username));
    }
}
