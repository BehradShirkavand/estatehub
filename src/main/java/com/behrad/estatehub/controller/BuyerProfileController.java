package com.behrad.estatehub.controller;

import com.behrad.estatehub.entity.BuyerProfile;
import com.behrad.estatehub.entity.Users;
import com.behrad.estatehub.repository.UsersRepository;
import com.behrad.estatehub.service.BuyerProfileService;
import com.behrad.estatehub.service.UsersService;
import com.behrad.estatehub.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/buyer-profile")
@RequiredArgsConstructor
public class BuyerProfileController {

    private final UsersService usersService;
    private final BuyerProfileService buyerProfileService;

    @GetMapping("/")
    public String buyerProfile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();

            Users user = usersService.findByEmail(currentUsername);

            Optional<BuyerProfile> buyerProfile = buyerProfileService.getOne(user.getUserId());

            buyerProfile.ifPresent(profile -> model.addAttribute("profile", profile));
        }
        return "buyer-profile";
    }

    @PostMapping("/addNew")
    public String addNew(BuyerProfile buyerProfile, @RequestParam("image") MultipartFile multipartFile, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();

            Users users = usersService.findByEmail(currentUsername);

            buyerProfile.setUserId(users);
            buyerProfile.setUserAccountId(users.getUserId());
        }
        model.addAttribute("profile", buyerProfile);

        String fileName = "";
        if (!Objects.equals(multipartFile.getOriginalFilename(), "")) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            buyerProfile.setProfilePhoto(fileName);
        }
        BuyerProfile savedUser = buyerProfileService.addNew(buyerProfile);

        String uploadDir = "photos/buyer/" + savedUser.getUserAccountId();

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/dashboard/";
    }

    @GetMapping("/{id}")
    public String showBuyerProfile(@PathVariable Integer id, Model model) {

        BuyerProfile profile = buyerProfileService.getOne(id).orElse(new BuyerProfile());

        model.addAttribute("profile", profile);

        return "buyer-profile";
    }
}
