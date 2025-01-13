package app.controller;

import app.model.Client;
import app.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public String getClients(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return "clients"; // возвращает шаблон clients.html
    }

    @PostMapping("/clients")
    public String addClient(@RequestParam String name) {
        clientService.save(new Client(null, name, null, null));
        return "redirect:/clients"; // Перенаправление на /clients
    }
}
