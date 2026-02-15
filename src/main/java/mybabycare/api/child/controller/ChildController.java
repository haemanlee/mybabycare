package mybabycare.api.child.controller;

import javax.validation.Valid;
import mybabycare.api.child.dto.ChildRequest;
import mybabycare.api.child.dto.ChildResponse;
import mybabycare.api.child.service.ChildService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/children")
public class ChildController {

    private final ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    @GetMapping
    public List<ChildResponse> child() {
        return childService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChildResponse createChild(@Valid @RequestBody ChildRequest request) {
        return childService.create(request);
    }

    @GetMapping("/{childId}")
    public ChildResponse findChild(@PathVariable Long childId) {
        return childService.findById(childId);
    }

    @PatchMapping("/{childId}")
    public ChildResponse updateChild(@PathVariable Long childId, @Valid @RequestBody ChildRequest request) {
        return childService.update(childId, request);
    }

    @DeleteMapping("/{childId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChild(@PathVariable Long childId) {
        childService.delete(childId);
    }
}
