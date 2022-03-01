package mybabycare.api.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/parents")
public class ParentController {

    @GetMapping
    public String parent(){
        return "get parents";
    }

    @PostMapping
    public String Parent() {
        return "post parents";
    }

    @GetMapping("/{parentId}")
    public String findParent(@PathVariable String parentId){
        return "get parentId=" + parentId;
    }

    @PatchMapping("/{parentId}")
    public String updateParent(@PathVariable String parentId){
        return "update parentId=" + parentId;
    }

    @DeleteMapping("/{parentId}")
    public String deleteParent(@PathVariable String parentId){
        return "delete parentId=" + parentId;
    }
}
