package in.bugr.controller;

import in.bugr.common.entity.Device;
import in.bugr.common.entity.Gather;
import in.bugr.common.entity.GatherPerson;
import in.bugr.common.entity.Person;
import in.bugr.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/20 下午9:13
 **/
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @NotNull
    private final AdminService adminService;

    @GetMapping("/gather")
    public List<Gather> getGathers() {
        return adminService.getGathers();
    }

    @PostMapping("/gather")
    public Gather createGather(@RequestBody Gather gather) {
        return adminService.createGather(gather);
    }

    @PutMapping("/gather/{id}")
    public Gather updateGather(@PathVariable Long id, @RequestBody Gather gather) {
        return adminService.updateGather(id, gather);
    }

    @PostMapping("/gather/{gatherId}/manager/{personId}")
    public GatherPerson addGatherManager(@PathVariable Long gatherId, @PathVariable Long personId) {
        return adminService.addGatherManager(gatherId, personId);
    }

    @DeleteMapping("/gather/{gatherId}/manager/{personId}")
    public void delGatherManager(@PathVariable Long gatherId, @PathVariable Long personId) {
        adminService.delGatherManager(gatherId, personId);
    }

    @PutMapping("/person/{id}")
    public Person updatePerson(@PathVariable Long id, @RequestBody Person person) {
        return adminService.updatePerson(id, person);
    }

    @GetMapping("/device")
    public List<Device> getDevices() {
        return adminService.getDevices();
    }

    @DeleteMapping("/device/{id}")
    public void delDevice(@PathVariable Long id) {
         adminService.delDevice(id);
    }

    @PostMapping("/device")
    public Device createDevice(@RequestBody Device device){
        return adminService.createDevice(device);
    }
    @PostMapping("/device/{id}/disable")
    public Device disableDevice(@PathVariable Long id){
        return adminService.disableDevice(id);
    }

}
