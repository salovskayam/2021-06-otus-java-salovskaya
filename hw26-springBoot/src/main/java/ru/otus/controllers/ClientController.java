package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.crm.model.Client;
import ru.otus.crm.services.DBClientService;

import javax.validation.Valid;
import java.util.List;


@Controller
public class ClientController {

    private final DBClientService clientService;

    public ClientController(DBClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/", "/client/list"})
    public String clientsListView(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new Client());
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public String clientSave(@Valid final Client client, final BindingResult bindingResult, Model model) {
        if (bindingResult.hasFieldErrors()) {
            return "clientCreate";
        }
        client.addPhones();
        model.addAttribute("client", clientService.save(client));
        return "redirect:/";
    }

    @PostMapping("/addPhone")
    public String addPhone(final Client client) {
        client.addPhone();
        return "clientCreate :: phoneList"; // returning the updated section
    }

    @PostMapping("/removePhone")
    public String removePhone(final Client client, @RequestParam("removePhone") int index) {
        client.removePhone(index);
        return "clientCreate :: phoneList"; // returning the updated section
    }
}
