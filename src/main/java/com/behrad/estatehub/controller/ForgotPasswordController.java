package com.behrad.estatehub.controller;

import com.behrad.estatehub.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        boolean success = forgotPasswordService.sendResetPasswordEmail(email);
        if (success) {
            model.addAttribute("message", "Send reset password link successfully");
        } else {
            model.addAttribute("error", "Email not found or already sent link");
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {

        if (!forgotPasswordService.isValidResetToken(token)) {
            model.addAttribute("error", "Invalid or expired link");
            return "reset-password";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       Model model) {
        boolean success = forgotPasswordService.resetPassword(token, password);
        if (success) {
            model.addAttribute("message", "Password reset successfully");
        } else {
            model.addAttribute("error", "Invalid or expired link");
        }
        return "reset-password";
    }

}
