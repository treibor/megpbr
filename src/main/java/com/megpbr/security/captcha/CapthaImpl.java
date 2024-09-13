package com.megpbr.security.captcha;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
public class CapthaImpl implements Captcha {
    private static final int COUNT_NUM = 6;
    private static final int WIDTH_IMG = 200;
    private static final int HEIGHT_IMG = 40;
    private String genStr;
    private String charsInImg = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
    private Random random = new Random(System.nanoTime());

    private void getRandomString() {
        char[] chars = charsInImg.toCharArray();
        StringBuilder tmpStr = new StringBuilder(COUNT_NUM);

        // Ensure at least one character from each category
        String numbers = "1234567890";
        String letters = "QWERTYUIOPASDFGHJKLZXCVBNM";

        // Add at least one number and one letter
        tmpStr.append(numbers.charAt(random.nextInt(numbers.length())));
        tmpStr.append(letters.charAt(random.nextInt(letters.length())));

        // Fill the rest of the string with random characters from the full set
        for (int i = 2; i < COUNT_NUM; i++) {
            tmpStr.append(chars[random.nextInt(chars.length)]);
        }

        // Shuffle the result to ensure randomness
        List<Character> charsList = new ArrayList<>();
        for (char c : tmpStr.toString().toCharArray()) {
            charsList.add(c);
        }
        Collections.shuffle(charsList);
        StringBuilder shuffledStr = new StringBuilder();
        for (char c : charsList) {
            shuffledStr.append(c);
        }
        
        genStr = shuffledStr.toString();
    }

    @Override
    public BufferedImage getCaptchaBufferedImage() {
        getRandomString();
        BufferedImage bufferedImage = new BufferedImage(WIDTH_IMG, HEIGHT_IMG, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        Font font = new Font("Charlemagne Std", Font.BOLD, 25);
        graphics.setFont(font);
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.addRenderingHints(hints);
        GradientPaint blackToGray = new GradientPaint(50, 50, Color.BLACK,300, 100, Color.LIGHT_GRAY);
        graphics.setPaint(blackToGray);
        graphics.fillRect(0,0, WIDTH_IMG, HEIGHT_IMG);
        graphics.setColor(new Color(25, 159, 110));
        int x = 0;
        int y;
        for (int i = 0; i < genStr.length(); i++) {
            x += 10 + (Math.abs(random.nextInt()) % 15);
            y = 20 + Math.abs(random.nextInt()) % 20;
            graphics.drawChars(genStr.toCharArray(), i, 1, x, y);
        }
        graphics.dispose();
        return bufferedImage;
    }

    @Override
    public boolean checkUserAnswer(String userAnswer) {
        return genStr.equals(userAnswer);
    }

    @Override
    public Image getCaptchaImg(){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(getCaptchaBufferedImage(), "png", bos);
            StreamResource streamResource = new StreamResource("img.png", () -> new ByteArrayInputStream(bos.toByteArray()));
            return new Image(streamResource, "img.png");
        }catch (IOException e){
            return null;
        }
    }
}