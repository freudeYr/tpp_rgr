package tpp.city.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import tpp.city.repo.ApartmentRepository;
import tpp.city.repo.CityRepository;
import tpp.city.repo.HouseRepository;
import tpp.city.repo.StreetRepository;

@Controller
public class AuthController {

    private final CityRepository cityRepository;
    private final StreetRepository streetRepository;
    private final HouseRepository houseRepository;
    private final ApartmentRepository apartmentRepository;

    public AuthController(CityRepository cityRepository, StreetRepository streetRepository,
                          HouseRepository houseRepository, ApartmentRepository apartmentRepository) {
        this.cityRepository = cityRepository;
        this.streetRepository = streetRepository;
        this.houseRepository = houseRepository;
        this.apartmentRepository = apartmentRepository;
    }

    // Перехід на сторінку логіну при доступі до кореневої сторінки
    @GetMapping("/")
    public String redirectToLogin() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Перехід на сторінку index
    @GetMapping("/index")
    public String index(Model model, Authentication authentication) {
        // Перевірка, чи авторизований користувач
        if (authentication != null) {
            String username = authentication.getName();
            model.addAttribute("username", username);

            // Додавання даних для відображення таблиць на index
            model.addAttribute("cities", cityRepository.getAllCities());
            model.addAttribute("streets", streetRepository.getAllStreets());
            model.addAttribute("houses", houseRepository.getAllHouses());
            model.addAttribute("apartments", apartmentRepository.getAllApartments());

            // Перевірка ролі користувача
            if (authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))) {
                model.addAttribute("isUser", true); // Якщо роль USER
            } else {
                model.addAttribute("isUser", false); // Якщо роль ADMIN
            }
        }
        return "index";
    }

    @GetMapping("/403")
    public String error403() {
        return "error";
    }
}
