package br.com.petconnect.boarding.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String teste(){
        return "Hello PET TESTE";
    }
}
