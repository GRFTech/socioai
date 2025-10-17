package spring.gr.socioai.interfaces.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/app-exemplo")
public class EndpointSeguroParaTeste {

    @PostMapping("/dashboard")
    public ResponseEntity<String> HelloDaApiSegura() {
        return ResponseEntity.ok().body("Hello da api segura!");
    }

    @PostMapping("/dashboard/adm")
    public ResponseEntity<String> HelloDaApiSeguraDeADM() {
        return ResponseEntity.ok().body("Hello da api segura de administrador!");
    }
}
