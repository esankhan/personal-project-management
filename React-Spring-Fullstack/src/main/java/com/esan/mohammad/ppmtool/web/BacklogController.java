package com.esan.mohammad.ppmtool.web;

import com.esan.mohammad.ppmtool.domain.Project;
import com.esan.mohammad.ppmtool.domain.ProjectTask;
import com.esan.mohammad.ppmtool.services.MapValidationErrorService;
import com.esan.mohammad.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {
    @Autowired
    private ProjectTaskService projectTaskService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;


    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                     BindingResult result, @PathVariable String backlog_id,
                                                     Principal principal){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;
        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id,projectTask,principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectBacklogs(@PathVariable String backlog_id,Principal principal){
        return (projectTaskService.findBacklogById(backlog_id,principal.getName()));
    }

    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id,
                                            @PathVariable String pt_id,
                                            Principal principal){
        ProjectTask projectTask = projectTaskService.findProjectTaskByProjectSequence(backlog_id,pt_id,
                principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask,HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                               @PathVariable String backlog_id, @PathVariable String pt_id,Principal principal){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;
        ProjectTask updatedProjectTask = projectTaskService.updateByProjectSequence(projectTask,backlog_id,pt_id,principal.getName());
        return new ResponseEntity<ProjectTask>(updatedProjectTask,HttpStatus.OK);
    }
    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id,Principal principal){
        projectTaskService.deleteProjectByProjectSequence(backlog_id,pt_id,principal.getName());
        return new ResponseEntity<String>("Project Task "+pt_id+" sucessfully deleted",HttpStatus.OK);
    }
}
