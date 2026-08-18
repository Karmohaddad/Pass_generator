/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.password.generator.service;

/**
 * المحرك المسؤول عن حساب الإنتروبيا (Password Entropy) وتقييم قوة كلمات المرور.
 */
public class PasswordEvaluator {

    /**
     * Enum يمثل مستويات قوة كلمة المرور المختلفة.
     */
    public enum StrengthLevel {
        VERY_WEAK("🔴 ضعيفة جداً (Very Weak)", "عرضة للكسر اللحظي عبر التخمين البسيط."),
        WEAK("🟠 ضعيفة (Weak)", "سهلة التخمين وخطر استخدامها في الحسابات المهمة."),
        MEDIUM("🟡 متوسطة (Medium)", "مقبولة للحسابات العادية ولكن يفضل تقويتها."),
        STRONG("🟢 قوية (Strong)", "آمنة جداً وممتازة للحماية ضد معظم الهجمات الحديثة."),
        VERY_STRONG("🛡️ قوية جداً (Very Strong)", "حماية قصوى بمستوى التشفير العسكري العالي.");

        private final String label;
        private final String description;

        StrengthLevel(String label, String description) {
            this.label = label;
            this.description = description;
        }

        public String getLabel() {
            return label;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * نتيجة تقييم كلمة المرور متضمنة الإنتروبيا والمستوى والوصف.
     */
    public static final class EvaluationResult {
        private final double entropyBits;
        private final StrengthLevel strengthLevel;

        public EvaluationResult(double entropyBits, StrengthLevel strengthLevel) {
            this.entropyBits = entropyBits;
            this.strengthLevel = strengthLevel;
        }

        public double getEntropyBits() {
            return entropyBits;
        }

        public StrengthLevel getStrengthLevel() {
            return strengthLevel;
        }

        @Override
        public String toString() {
            return String.format("Entropy: %.2f Bits | Strength: %s\nDetails: %s",
                    entropyBits, strengthLevel.getLabel(), strengthLevel.getDescription());
        }
    }

    /**
     * حساب إنتروبيا كلمة مرور معينة بناءً على طولها وحجم طقم المحارف المستخدم فيها فعلياً.
     *
     * @param password كلمة المرور المراد تقييمها
     * @return كائن EvaluationResult يحتوي على التفاصيل والتقييم
     */
    public EvaluationResult evaluate(String password) {
        if (password == null || password.isEmpty()) {
            return new EvaluationResult(0.0, StrengthLevel.VERY_WEAK);
        }

        int poolSize = calculateDynamicPoolSize(password);
        int length = password.length();

        // حساب الإنتروبيا: E = L * log2(R)
        double entropyBits = calculateEntropy(length, poolSize);
        StrengthLevel level = determineStrengthLevel(entropyBits);

        return new EvaluationResult(entropyBits, level);
    }

    /**
     * دالة رياضية لحساب الإنتروبيا بالبت (Bits).
     */
    public double calculateEntropy(int length, int poolSize) {
        if (length <= 0 || poolSize <= 0) {
            return 0.0;
        }
        // تطبيق تحويل اللوغاريتم للأساس 2: Math.log(R) / Math.log(2)
        double log2R = Math.log(poolSize) / Math.log(2);
        return length * log2R;
    }

    /**
     * فحص محتوى كلمة المرور ديناميكياً لتحديد حجم طقم الحروف المتاحة R.
     */
    private int calculateDynamicPoolSize(String password) {
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }

        int poolSize = 0;
        if (hasLower) poolSize += 26;
        if (hasUpper) poolSize += 26;
        if (hasDigit) poolSize += 10;
        if (hasSpecial) poolSize += 32; // حجم الرموز الخاصة المعتاد

        return poolSize;
    }

    /**
     * تحديد مستوى القوة بناءً على نطاق قيمة البت (Bits).
     */
    private StrengthLevel determineStrengthLevel(double entropyBits) {
        if (entropyBits < 28) {
            return StrengthLevel.VERY_WEAK;
        } else if (entropyBits < 36) {
            return StrengthLevel.WEAK;
        } else if (entropyBits < 60) {
            return StrengthLevel.MEDIUM;
        } else if (entropyBits < 128) {
            return StrengthLevel.STRONG;
        } else {
            return StrengthLevel.VERY_STRONG;
        }
    }
}