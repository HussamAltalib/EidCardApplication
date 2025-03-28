package com.example.eidcardapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
public class EidCardController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/generate")
    public String generate(@RequestParam String name,
                           @RequestParam String template,
                           Model model) throws IOException, FontFormatException {
        // Pick the image dynamically
        BufferedImage card = ImageIO.read(new File("src/main/resources/static/cards/" + template + ".png"));
        Graphics2D g = card.createGraphics();

        // Set smooth rendering for better quality
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(20, 60, 80)); // Text color

        // Load Amiri font from resources
        InputStream fontStream = getClass().getResourceAsStream("/fonts/Amiri-Regular.ttf");
        Font amiriFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, 72); // You can tweak size
        g.setFont(amiriFont);

        // Handle Arabic right-to-left if needed
        String displayText = name;
        // Calculate centered position
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(displayText);
        int x = (card.getWidth() - textWidth) / 2;
        int y = 1550;

        // Draw the name
        g.drawString(displayText, x, y);
        g.dispose();

        // Save generated image
        String filename = UUID.randomUUID() + ".png";
        File output = new File("src/main/resources/static/generated/" + filename);
        output.getParentFile().mkdirs();
        ImageIO.write(card, "png", output);

        model.addAttribute("cardUrl", "/generated/" + filename);
        return "card";
    }



}
