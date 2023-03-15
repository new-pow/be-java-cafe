package kr.codesqaud.cafe.controller;

import kr.codesqaud.cafe.domain.User;
import kr.codesqaud.cafe.dto.UserJoinRequestDto;
import kr.codesqaud.cafe.dto.UserListResponseDto;
import kr.codesqaud.cafe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private Logger LOG = LoggerFactory.getLogger(UserController.class.getName());

    @GetMapping("/user/form")
    public String addForm() {
        return "user/form";
    }

    @PostMapping("/users")
    public String addUser(@ModelAttribute UserJoinRequestDto dto) throws IllegalAccessException {
        userService.join(dto.toEntity());
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String list(Model model) {
        List<UserListResponseDto> collect = userService.findAll().stream()
                .map(UserListResponseDto::new)
                .collect(Collectors.toList());

        model.addAttribute("list", collect);
        model.addAttribute("size", collect.size());
        return "user/list";
    }

    @GetMapping("/users/{userId}")
    public String profile(@PathVariable Long userId, Model model) {
        User user = userService.findUser(userId);
        model.addAttribute("profile", new UserListResponseDto(user));
        return "user/profile";
    }

}
