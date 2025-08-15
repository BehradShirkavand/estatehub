package com.behrad.estatehub.controller;

import com.behrad.estatehub.entity.SellerProfile;
import com.behrad.estatehub.entity.Users;
import com.behrad.estatehub.repository.UsersRepository;
import com.behrad.estatehub.service.SellerProfileService;
import com.behrad.estatehub.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/seller-profile")
public class SellerProfileController {

    private final UsersRepository usersRepository;
    private final SellerProfileService sellerProfileService;

    @GetMapping("/")
    public String sellerProfile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();

            Users user = usersRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Could not found user with email: " + currentUsername));

            Optional<SellerProfile> sellerProfile = sellerProfileService.getOne(user.getUserId());

            if (!sellerProfile.isEmpty()) {
                model.addAttribute("profile", sellerProfile.get());
            }

        }
        return "seller_profile";
    }

    @PostMapping("addNew")
    public String addNew(SellerProfile sellerProfile, @RequestParam("image")MultipartFile multipartFile, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();

            Users users = usersRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Could not found user with email: " + currentUsername));

            sellerProfile.setUserId(users);
            sellerProfile.setUserAccountId(users.getUserId());
        }
        model.addAttribute("profile", sellerProfile);

        String fileName = "";
        if (!multipartFile.getOriginalFilename().equals("")) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            sellerProfile.setProfilePhoto(fileName);
        }
        SellerProfile savedUser = sellerProfileService.addNew(sellerProfile);

        String uploadDir = "photos/seller/" + savedUser.getUserAccountId();

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/dashboard/";
    }
}
