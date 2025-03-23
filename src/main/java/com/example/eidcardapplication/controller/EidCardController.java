package com.example.eidcardapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class EidCardController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/generate")
    public String generate(@RequestParam String name, Model model) throws IOException {
        BufferedImage card = ImageIO.read(new File("src/main/resources/static/cards/template1.png"));
        Graphics2D g = card.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(20, 60, 80));
        g.setFont(new Font("Arial", Font.BOLD, 50));

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(name);
        int x = (card.getWidth() - textWidth) / 2;
        int y = 1550;

        g.drawString(name, x, y);
        g.dispose();

        String filename = UUID.randomUUID() + ".png";
        File output = new File("src/main/resources/static/generated/" + filename);
        output.getParentFile().mkdirs();
        ImageIO.write(card, "png", output);

        model.addAttribute("cardUrl", "/generated/" + filename);
        return "card";
    }
}
