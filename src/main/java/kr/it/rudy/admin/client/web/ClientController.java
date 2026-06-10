package kr.it.rudy.admin.client.web;

import jakarta.validation.Valid;
import kr.it.rudy.admin.client.application.AuthServerClientService;
import kr.it.rudy.admin.client.dto.ClientCreateRequest;
import kr.it.rudy.admin.client.dto.ClientDetail;
import kr.it.rudy.admin.client.dto.ClientUpdateRequest;
import kr.it.rudy.admin.client.dto.SecretRevealResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final AuthServerClientService clientService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return "client/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new ClientCreateRequest());
        return "client/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") ClientCreateRequest form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "client/form";
        }
        SecretRevealResponse created = clientService.create(form);
        redirectAttributes.addFlashAttribute("revealedSecret", created);
        return "redirect:/clients/" + created.id();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable String id, Model model) {
        ClientDetail client = clientService.getDetail(id);
        model.addAttribute("client", client);
        if (!model.containsAttribute("updateForm")) {
            model.addAttribute("updateForm", toUpdateRequest(client));
        }
        return "client/detail";
    }

    @PostMapping("/{id}/update")
    public String update(
            @PathVariable String id,
            @Valid @ModelAttribute("updateForm") ClientUpdateRequest form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("client", clientService.getDetail(id));
            return "client/detail";
        }
        clientService.update(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "클라이언트 정보가 수정되었습니다.");
        return "redirect:/clients/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        clientService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "클라이언트가 삭제되었습니다.");
        return "redirect:/clients";
    }

    @PostMapping("/{id}/regenerate-secret")
    public String regenerateSecret(@PathVariable String id, RedirectAttributes redirectAttributes) {
        SecretRevealResponse response = clientService.regenerateSecret(id);
        redirectAttributes.addFlashAttribute("revealedSecret", response);
        return "redirect:/clients/" + id;
    }

    private ClientUpdateRequest toUpdateRequest(ClientDetail client) {
        ClientUpdateRequest form = new ClientUpdateRequest();
        form.setClientName(client.clientName());
        form.setLoginPageUri(client.loginPageUri() != null ? client.loginPageUri() : "");
        form.setGrantTypes(client.grantTypes());
        form.setScopes(client.scopes());
        form.setRedirectUrisRaw(client.redirectUrisRaw() != null ? client.redirectUrisRaw() : "");
        form.setPostLogoutRedirectUrisRaw(client.postLogoutRedirectUrisRaw() != null ? client.postLogoutRedirectUrisRaw() : "");
        form.setRequirePkce(client.requirePkce());
        form.setAccessTokenTtlMinutes(client.accessTokenTtlMinutes());
        form.setRefreshTokenTtlDays(client.refreshTokenTtlDays());
        return form;
    }
}
