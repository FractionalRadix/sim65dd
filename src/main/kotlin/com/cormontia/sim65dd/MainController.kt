package com.cormontia.sim65dd

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {
    @GetMapping("/")
    fun empty(): String {
        return "LDA #\$FF"
    }
}