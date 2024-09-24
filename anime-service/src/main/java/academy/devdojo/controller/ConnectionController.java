package academy.devdojo.controller;

import external.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/connection")
@RequiredArgsConstructor
public class ConnectionController {

    private final Connection connection;


    @GetMapping
    public ResponseEntity<Connection> getConnection() {
        return ResponseEntity.ok(connection);
    }

}
