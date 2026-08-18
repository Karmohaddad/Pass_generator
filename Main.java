/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.password.generator;

import com.password.generator.model.PasswordRules;
import com.password.generator.service.PasswordEvaluator;
import com.password.generator.service.SecurePasswordGenerator;

import java.util.Scanner;

/**
 * الواجهة الرئيسية والتفاعلية عبر سطر الأوامر? (CLI) لاختبار وتجربة المشروع.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final SecurePasswordGenerator generator = new SecurePasswordGenerator();
    private static final PasswordEvaluator evaluator = new PasswordEvaluator();

    public static void main(String[] args) {
        printHeader();

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("👉 اختر الخيار المناسب (1-4): ");
            String choice = scanner.nextLine().trim();

            System.out.println();
            switch (choice) {
                case "1" -> handleCustomPasswordGeneration();
                case "2" -> handleQuickStrongPassword();
                case "3" -> handlePasswordEvaluation();
                case "4" -> {
                    System.out.println("👋 شكراً لاستخدامك مولد كلمات المرور الآمن. إلى اللقاء!");
                    running = false;
                }
                default -> System.out.println("❌ خيار غير صحيح! يرجى إدخال رقم من 1 إلى 4.\n");
            }
        }
        scanner.close();
    }

    private static void printHeader() {
        System.out.println("=================================================");
        System.out.println("    🛡️  Secure Password Generator & Evaluator  🛡️   ");
        System.out.println("       مشروع مولد ومحلل كلمات المرور الآمن       ");
        System.out.println("=================================================\n");
    }

    private static void printMenu() {
        System.out.println("-------------------------------------------------");
        System.out.println("1️⃣  توليد كلمة مرور آمنة (مع ميزات مخصصة)");
        System.out.println("2️⃣  توليد سريع لكلمة مرور فائقة القوة (Quick Ultra-Strong)");
        System.out.println("3️⃣  فحص وتحليل إنتروبيا وقوة كلمة مرور خاصة بك");
        System.out.println("4️⃣  خروج من البرنامج (Exit)");
        System.out.println("-------------------------------------------------");
    }

    /**
     * معالجة خيار التوليد المخصص مع قراءة محددات المستخدم.
     */
    private static void handleCustomPasswordGeneration() {
        System.out.println("⚙️ --- تخصيص قواعد كلمة المرور ---");
        try {
            System.out.print("📌 أدخل طول كلمة المرور (مثال: 16): ");
            int length = Integer.parseInt(scanner.nextLine().trim());

            boolean includeUpper = readBooleanChoice("هل تريد تضمين حروف كبيرة (A-Z)؟ (y/n): ");
            boolean includeLower = readBooleanChoice("هل تريد تضمين حروف صغيرة (a-z)؟ (y/n): ");
            boolean includeDigits = readBooleanChoice("هل تريد تضمين أرقام (0-9)؟ (y/n): ");
            boolean includeSpecial = readBooleanChoice("هل تريد تضمين رموز خاصة (!@#$)؟ (y/n): ");
            boolean excludeSimilar = readBooleanChoice("هل تريد استبعاد الحروف المتشابهة بصرياً (1, l, O, 0)؟ (y/n): ");

            // تطبيق Builder Pattern
            PasswordRules rules = new PasswordRules.Builder()
                    .setLength(length)
                    .setIncludeUppercase(includeUpper)
                    .setIncludeLowercase(includeLower)
                    .setIncludeDigits(includeDigits)
                    .setIncludeSpecialChars(includeSpecial)
                    .setExcludeSimilarChars(excludeSimilar)
                    .build();

            String generatedPassword = generator.generatePassword(rules);
            PasswordEvaluator.EvaluationResult evaluation = evaluator.evaluate(generatedPassword);

            printResult(generatedPassword, evaluation);

        } catch (NumberFormatException e) {
            System.out.println("❌ خطأ: يرجى إدخال رقم صحيح للطول!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ خطأ في القواعد: " + e.getMessage() + "\n");
        }
    }

    /**
     * إعداد سريع لكلمة مرور بتطبيق أفضل معايير الأمان مباشرة.
     */
    private static void handleQuickStrongPassword() {
        System.out.println("⚡ --- توليد سريع لكلمة مرور فائقة القوة ---");
        PasswordRules rules = new PasswordRules.Builder()
                .setLength(20)
                .setIncludeUppercase(true)
                .setIncludeLowercase(true)
                .setIncludeDigits(true)
                .setIncludeSpecialChars(true)
                .setExcludeSimilarChars(true)
                .build();

        String generatedPassword = generator.generatePassword(rules);
        PasswordEvaluator.EvaluationResult evaluation = evaluator.evaluate(generatedPassword);

        printResult(generatedPassword, evaluation);
    }

    /**
     * فحص كلمة مرور يدخلها المستخدم لحساب إنتروبيتها وتصنيفها.
     */
    private static void handlePasswordEvaluation() {
        System.out.println("🔍 --- فحص وتقييم كلمة مرور ---");
        System.out.print("🔑 أدخل كلمة المرور المراد فحصها: ");
        String customPassword = scanner.nextLine();

        PasswordEvaluator.EvaluationResult evaluation = evaluator.evaluate(customPassword);

        System.out.println("\n📊 --- نتيجة التقييم والتحليل ---");
        System.out.println(evaluation);
        System.out.println();
    }

    private static boolean readBooleanChoice(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toLowerCase();
        return input.startsWith("y") || input.startsWith("ن") || input.startsWith("1");
    }

    private static void printResult(String password, PasswordEvaluator.EvaluationResult evaluation) {
        System.out.println("\n🎉 --- تم توليد كلمة المرور بنجاح ---");
        System.out.println("🔑 كلمة المرور المولدة: " + password);
        System.out.println("📊 إنتروبيا الأمان: " + String.format("%.2f Bits", evaluation.getEntropyBits()));
        System.out.println("🏷️  مستوى القوة: " + evaluation.getStrengthLevel().getLabel());
        System.out.println("💡 التفاصيل: " + evaluation.getStrengthLevel().getDescription());
        System.out.println();
    }
}