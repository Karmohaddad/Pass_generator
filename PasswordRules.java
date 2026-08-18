/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.password.generator.model;

/**
 * تمثل القواعد والمحددات المطلوبة لتوليد كلمة المرور.
 * تم تصميم الفئة باستخدام Builder Pattern وهي غير قابلة للتعديل (Immutable).
 */
public final class PasswordRules {

    private final int length;
    private final boolean includeUppercase;
    private final boolean includeLowercase;
    private final boolean includeDigits;
    private final boolean includeSpecialChars;
    private final boolean excludeSimilarChars; // تجنب الحروف المتشابهة مثل (1, l, O, 0)

    // الباني خاص (private) لمنع إنشاء الكائن مباشرة؛ ويجبر على استخدام Builder
    private PasswordRules(Builder builder) {
        this.length = builder.length;
        this.includeUppercase = builder.includeUppercase;
        this.includeLowercase = builder.includeLowercase;
        this.includeDigits = builder.includeDigits;
        this.includeSpecialChars = builder.includeSpecialChars;
        this.excludeSimilarChars = builder.excludeSimilarChars;
    }

    // --- دوال القراءة (Getters) ---

    public int getLength() {
        return length;
    }

    public boolean isIncludeUppercase() {
        return includeUppercase;
    }

    public boolean isIncludeLowercase() {
        return includeLowercase;
    }

    public boolean isIncludeDigits() {
        return includeDigits;
    }

    public boolean isIncludeSpecialChars() {
        return includeSpecialChars;
    }

    public boolean isExcludeSimilarChars() {
        return excludeSimilarChars;
    }

    // --- الفئة البانية الداخلية (Static Inner Builder Class) ---

    public static class Builder {

        // قيم افتراضية آمنة (Default Values)
        private int length = 12;
        private boolean includeUppercase = true;
        private boolean includeLowercase = true;
        private boolean includeDigits = true;
        private boolean includeSpecialChars = true;
        private boolean excludeSimilarChars = false;

        public Builder setLength(int length) {
            this.length = length;
            return this;
        }

        public Builder setIncludeUppercase(boolean includeUppercase) {
            this.includeUppercase = includeUppercase;
            return this;
        }

        public Builder setIncludeLowercase(boolean includeLowercase) {
            this.includeLowercase = includeLowercase;
            return this;
        }

        public Builder setIncludeDigits(boolean includeDigits) {
            this.includeDigits = includeDigits;
            return this;
        }

        public Builder setIncludeSpecialChars(boolean includeSpecialChars) {
            this.includeSpecialChars = includeSpecialChars;
            return this;
        }

        public Builder setExcludeSimilarChars(boolean excludeSimilarChars) {
            this.excludeSimilarChars = excludeSimilarChars;
            return this;
        }

        /**
         * تجمع الخيارات وتحقق من صحتها وتنشئ كائن PasswordRules.
         */
        public PasswordRules build() {
            validateRules();
            return new PasswordRules(this);
        }

        private void validateRules() {
            if (length < 4 || length > 128) {
                throw new IllegalArgumentException("Password length must be between 4 and 128 characters.");
            }

            // التأكد من اختيار نوع واحد على الأقل من المحارف
            if (!includeUppercase && !includeLowercase && !includeDigits && !includeSpecialChars) {
                throw new IllegalArgumentException("At least one character type (Uppercase, Lowercase, Digits, Special) must be selected.");
            }
        }
    }
}