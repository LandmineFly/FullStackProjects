package com.leili.controller;

import com.leili.bean.Type;
import com.leili.bean.vo.PortalVo;
import com.leili.service.HeadlineService;
import com.leili.service.TypeService;
import com.leili.utils.JwtUtil;
import com.leili.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("portal")
@CrossOrigin
public class PortalController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private HeadlineService headlineService;

    @GetMapping("findAllTypes")
    public Result<List<Type>> findAllTypes(){
        return typeService.findAllTypes();
    }

    @PostMapping("findNewsPage")
    public Result<Map<String,Object>> findNewsPage(@RequestBody PortalVo portalVo){
        return headlineService.findNewsPage(portalVo);
    }

    @PostMapping("showHeadlineDetail")
    public Result<Map<String, Object>> showHeadlineDetail(Integer hid){
        return headlineService.showHeadlineDetail(hid);
    }
}
