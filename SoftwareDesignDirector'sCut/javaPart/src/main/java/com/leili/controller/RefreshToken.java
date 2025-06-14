package com.leili.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class RefreshToken {
    @PostMapping("/refresh-token")
    public void refresh() {}
}
