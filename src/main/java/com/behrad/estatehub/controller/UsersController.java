package com.behrad.estatehub.controller;

import com.behrad.estatehub.entity.Users;
import com.behrad.estatehub.entity.UsersType;
import com.behrad.estatehub.service.UsersService;
import com.behrad.estatehub.service.UsersTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final UsersTypeService usersTypeService;

    @GetMapping("/register")
    public String register(@RequestParam(value = "role", required = false) String role, Model model) {

        List<UsersType> usersTypes = usersTypeService.getAll();
        model.addAttribute("getAllTypes", usersTypes);
        Users user =  new Users();

        if (role != null) {
            usersTypes.stream()
                    .filter(t -> t.getUserTypeName().equalsIgnoreCase(role))
                    .findFirst()
                    .ifPresent(user::setUserTypeId);
        }
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid @ModelAttribute("user") Users users, BindingResult bindingResult, Model model) {

        Optional<Users> optionalUsers = usersService.getUserByEmail(users.getEmail());

        if (optionalUsers.isPresent()){
            bindingResult.rejectValue(
                    "email",
                    "duplicate.email");
        }

        if (bindingResult.hasErrors()) {
            List<UsersType> usersTypes = usersTypeService.getAll();
            model.addAttribute("getAllTypes", usersTypes);
            return "register";
        }

        usersService.addNew(users);
        return "redirect:/dashboard/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }
}
