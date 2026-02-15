package mybabycare.api.parent.controller;

import javax.validation.Valid;
import mybabycare.api.parent.dto.ParentRequest;
import mybabycare.api.parent.dto.ParentResponse;
import mybabycare.api.parent.service.ParentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/parents")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping
    public List<ParentResponse> parent() {
        return parentService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParentResponse createParent(@Valid @RequestBody ParentRequest request) {
        return parentService.create(request);
    }

    @GetMapping("/{parentId}")
    public ParentResponse findParent(@PathVariable Long parentId) {
        return parentService.findById(parentId);
    }

    @PatchMapping("/{parentId}")
    public ParentResponse updateParent(@PathVariable Long parentId, @Valid @RequestBody ParentRequest request) {
        return parentService.update(parentId, request);
    }

    @DeleteMapping("/{parentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteParent(@PathVariable Long parentId) {
        parentService.delete(parentId);
    }
}
