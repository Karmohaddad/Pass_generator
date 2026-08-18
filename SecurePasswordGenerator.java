/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.password.generator.service;

import com.password.generator.model.PasswordRules;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * المحرك الرئيسي المسؤول عن توليد كلمات المرور العشوائية المشفرة
 * بناءً على المحددات الممررة في فئة PasswordRules.
 */
public class SecurePasswordGenerator {

    // مجموعات المحارف الأساسية
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    // المحارف المتشابهة بصرياً (إزالتها اختيارية لمنع الالتباس عند القراءة)
    private static final String SIMILAR_CHARS = "iIl1oO0";

    private final SecureRandom random;

    public SecurePasswordGenerator() {
        this.random = new SecureRandom(); // استخدام المولد المشفر تلقائياً
    }

    /**
     * توليد كلمة مرور عشوائية معقدة بناءً على القواعد المحددة.
     *
     * @param rules قواعد وإعدادات كلمة المرور
     * @return كلمة المرور المولدة كنص (String)
     */
    public String generatePassword(PasswordRules rules) {
        if (rules == null) {
            throw new IllegalArgumentException("Password rules cannot be null.");
        }

        StringBuilder characterPool = new StringBuilder();
        List<Character> mandatoryChars = new ArrayList<>();

        // 1. بناء طقم الحروف المتاحة وضمان محرف واحد على الأقل لكل فئة مفعّلة
        if (rules.isIncludeLowercase()) {
            String pool = filterSimilarIfNeeded(LOWERCASE, rules.isExcludeSimilarChars());
            characterPool.append(pool);
            mandatoryChars.add(getRandomChar(pool));
        }

        if (rules.isIncludeUppercase()) {
            String pool = filterSimilarIfNeeded(UPPERCASE, rules.isExcludeSimilarChars());
            characterPool.append(pool);
            mandatoryChars.add(getRandomChar(pool));
        }

        if (rules.isIncludeDigits()) {
            String pool = filterSimilarIfNeeded(DIGITS, rules.isExcludeSimilarChars());
            characterPool.append(pool);
            mandatoryChars.add(getRandomChar(pool));
        }

        if (rules.isIncludeSpecialChars()) {
            String pool = filterSimilarIfNeeded(SPECIAL_CHARS, rules.isExcludeSimilarChars());
            characterPool.append(pool);
            mandatoryChars.add(getRandomChar(pool));
        }

        String fullPool = characterPool.toString();
        List<Character> passwordChars = new ArrayList<>(mandatoryChars);

        // 2. إكمال بقية طول كلمة المرور باختيار محارف عشوائية من مجموع الطقم الكامل
        int remainingLength = rules.getLength() - passwordChars.size();
        for (int i = 0; i < remainingLength; i++) {
            passwordChars.add(getRandomChar(fullPool));
        }

        // 3. خلط عناصر القائمة عشوائياً لمنع أي نمط محدد في البداية
        Collections.shuffle(passwordChars, random);

        // 4. تحويل قائمة المحارف List<Character> إلى نص نهايئ String
        StringBuilder finalPassword = new StringBuilder();
        for (char ch : passwordChars) {
            finalPassword.append(ch);
        }

        return finalPassword.toString();
    }

    // --- دوال مساعدة خاصة (Private Helper Methods) ---

    private char getRandomChar(String charPool) {
        int index = random.nextInt(charPool.length());
        return charPool.charAt(index);
    }

    private String filterSimilarIfNeeded(String source, boolean exclude) {
        if (!exclude) {
            return source;
        }
        StringBuilder filtered = new StringBuilder();
        for (char c : source.toCharArray()) {
            if (SIMILAR_CHARS.indexOf(c) == -1) {
                filtered.append(c);
            }
        }
        return filtered.toString();
    }

    /**
     * دالة مساعدة لحساب حجم مجموعة المحارف المتاحة (Pool Size) لاستخدامها لاحقاً في قياس الإنتروبيا.
     */
    public int calculatePoolSize(PasswordRules rules) {
        int size = 0;
        if (rules.isIncludeLowercase()) size += filterSimilarIfNeeded(LOWERCASE, rules.isExcludeSimilarChars()).length();
        if (rules.isIncludeUppercase()) size += filterSimilarIfNeeded(UPPERCASE, rules.isExcludeSimilarChars()).length();
        if (rules.isIncludeDigits()) size += filterSimilarIfNeeded(DIGITS, rules.isExcludeSimilarChars()).length();
        if (rules.isIncludeSpecialChars()) size += filterSimilarIfNeeded(SPECIAL_CHARS, rules.isExcludeSimilarChars()).length();
        return size;
    }
}
