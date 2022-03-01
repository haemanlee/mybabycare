package mybabycare.api.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/children")
public class ChildController {

    @GetMapping
    public String child(){
        return "get children";
    }

    @PostMapping
    public String Child() {
        return "post child";
    }

    @GetMapping("/{childId}")
    public String findChild(@PathVariable String childId){
        return "get childId=" + childId;
    }

    @PatchMapping("/{childId}")
    public String updateChild(@PathVariable String childId){
        return "update childId=" + childId;
    }

    @DeleteMapping("/{childId}")
    public String deleteChild(@PathVariable String childId){
        return "delete childId=" + childId;
    }
}
